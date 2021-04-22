package com.sicuro.escrow.persistence

import com.sicuro.escrow.exception.ObjectNotFoundException
import com.sicuro.escrow.model.Address
import com.sicuro.escrow.model.Customer
import com.sicuro.escrow.model.SignupRequest
import com.sicuro.escrow.persistence.dao.ActivationLinkDao
import com.sicuro.escrow.persistence.dao.AddressDao
import com.sicuro.escrow.persistence.dao.CustomerDao
import com.sicuro.escrow.persistence.entity.ActivationLinkEntity
import com.sicuro.escrow.persistence.entity.AddressEntity
import com.sicuro.escrow.persistence.entity.CustomerEntity
import com.sicuro.escrow.util.security.CustomKeyGeneratorFactoryService
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.*
import javax.transaction.Transactional

@Component
class CustomerRepository(
    private val customerDao: CustomerDao,
    private val activationLinkDao: ActivationLinkDao,
    private val countryRepository: CountryRepository,
    private val addressDao: AddressDao
) {

    @Transactional
    fun createCustomer(registration: SignupRequest): Pair<Customer, String>{
        val customerNumber = generateCustomerNumber();
        val customer = Customer(
            customerNumber,
            registration.contact.title,
            registration.contact.firstName,
            registration.contact.lastName,
            registration.contact.preferredLanguage,
            registration.contact.email,
            registration.organisation?.name,
            registration.organisation?.taxNumber)

        //applyVat
        val customerWithVat = setCustomerVat(customer);

        val savedCustomer = saveCustomer(customerWithVat)

        val uuid = createActivationLink(savedCustomer)

        return Pair(Customer.convert(savedCustomer), uuid);
    }

    @Transactional
    fun updateCustomerDetails(customer: Customer): Customer {
        val oldEntity = customerDao.findById(customer.id!!).orElseThrow {
            throw ObjectNotFoundException("Customer does not exist")
        }

        val newEntity = customer.convert()
        val entity = oldEntity.copy(
            firstName = newEntity.firstName,
            lastName = newEntity.lastName,
            title = newEntity.title,
            gender = newEntity.title.gender,
            preferedLanguage = newEntity.preferedLanguage,
            organisation = newEntity.organisation,
            taxNumber = newEntity.taxNumber
        )

        return Customer.convert(customerDao.save(entity))
    }

    @Transactional
    fun updateAddress(address: Address, customerId: Long): Address {
        val entity:AddressEntity;
        if (address.id != null){
            val oldEntity = addressDao.findById(address.id).orElseThrow{
                throw ObjectNotFoundException("Customer does not exist")
            }
            entity = oldEntity.copy(
                street = address.street,
                streetExtension = address.streetExtension,
                city = address.city,
                postalCode = address.postalCode,
                region = address.region,
                phoneNumber = address.phoneNumber,
                countryIso = address.countryIso
            )
        } else {
            entity = address.convert()
        }
        return Address.convert(addressDao.save(entity))
    }

    fun getCustomer(customerId:Long): Customer {
        val customer = customerDao.findById(customerId).orElseThrow {
            throw ObjectNotFoundException("Customer does not exist")
        }
        return Customer.convert(customer);
    }

    private fun saveCustomer(customer:Customer) = customerDao.save(customer.convert())

    private fun generateCustomerNumber(): String {
        val generator = CustomKeyGeneratorFactoryService.instance.createDefault(OffsetDateTime.now())
        var customerNumber = generator.generateCustomKey()
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

    private fun createActivationLink(customer: CustomerEntity):String {
        return activationLinkDao.save(
            ActivationLinkEntity(
                UUID.randomUUID().toString(),
                true,
                customer
            )
        ).uuid
    }
}
