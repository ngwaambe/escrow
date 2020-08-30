package com.sicuro.escrow.persistence

import com.sicuro.escrow.model.Customer
import com.sicuro.escrow.model.RegistrationDetail
import com.sicuro.escrow.persistence.dao.CustomerDao
import com.sicuro.escrow.service.CustomKeyGeneratorFactoryService
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import javax.transaction.Transactional

@Component
class CustomerRepository( val customerDao: CustomerDao, val countryRepository: CountryRepository) {

    @Transactional
    fun createCustomer(registration: RegistrationDetail): Customer {
        val customerNumber = generateCustomerNumber();
        val customer = Customer(
            customerNumber,
            registration.contact.title,
            registration.contact.firstName,
            registration.contact.lastName,
            registration.contact.preferedLaguage,
            registration.contact.email,
            registration.organisation?.name,
            registration.organisation?.taxNumber);

        //applyVat
        val customerWithVat = setCustomerVat(customer);

        // resolveTransactionParticipations(customerBo);
        val savedCustomer = saveCustomer(customerWithVat)

        //create User
        return savedCustomer;
    }


    private fun saveCustomer(customer:Customer) = Customer.convert(customerDao.save(customer.convert()))

    private fun generateCustomerNumber(): String {
        val generator = CustomKeyGeneratorFactoryService.getInstance().createDefault(OffsetDateTime.now())
        var customerNumber: String = generator.generateCustomKey()
        while (customerDao.countByCustomerNumber(customerNumber)>0) {
            customerNumber = generator.generateCustomKey()
        }
        return customerNumber
    }

    private fun setCustomerVat(customer: Customer): Customer {
        customer.address?.let {
            return customer.copy(applyVat = countryRepository.shouldVatBeApplied(it.countryIso))
        }?: return customer;

    }
}
