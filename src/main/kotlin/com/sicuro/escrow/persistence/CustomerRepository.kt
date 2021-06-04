package com.sicuro.escrow.persistence

import com.sicuro.escrow.exception.ObjectNotFoundException
import com.sicuro.escrow.model.*
import com.sicuro.escrow.persistence.dao.AddressDao
import com.sicuro.escrow.persistence.dao.CustomerDao
import com.sicuro.escrow.persistence.entity.AddressEntity
import com.sicuro.escrow.persistence.entity.CustomerEntity
import com.sicuro.escrow.util.PaginationUtil
import com.sicuro.escrow.util.security.CustomKeyGeneratorFactoryService
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import javax.transaction.Transactional

@Component
class CustomerRepository(
    private val customerDao: CustomerDao,
    private val countryRepository: CountryRepository,
    private val addressDao: AddressDao
) {

    @Transactional
    fun createCustomer(registration: SignupRequest): Customer{
        val customerNumber = generateCustomerNumber();
        val customer = Customer(
            customerNumber,
            registration.contact.title,
            registration.contact.firstName,
            registration.contact.lastName,
            registration.contact.language,
            registration.contact.email,
            registration.organisation?.name,
            registration.organisation?.taxNumber)

        return Customer.convert(saveCustomer(customer))
    }

    @Transactional
    fun createCustomer(createRequest: CustomerCreateRequest): Customer {
        val customerNumber = generateCustomerNumber();
        val customer = Customer(
            null,
            customerNumber,
            createRequest.contact.title,
            createRequest.contact.firstName,
            createRequest.contact.lastName,
            createRequest.contact.title.gender,
            createRequest.contact.email,
            createRequest.contact.language,
            createRequest.address,
            createRequest.organisation?.name,
            createRequest.organisation?.taxNumber,
            false,
            createRequest.partnerId,
            createRequest.identityNumber
        )

        //applyVat
        val customerWithVat = setCustomerValueAddedTax(customer, customer.address!!.countryIso);

        return Customer.convert(saveCustomer(customerWithVat))
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
            language = newEntity.language,
            organisation = newEntity.organisation,
            taxNumber = newEntity.taxNumber
        )

        return Customer.convert(customerDao.save(entity))
    }

    @Transactional
    fun updateAddress(address: Address, customer: Customer, resolveVat: Boolean = false): Address {
        val entity:AddressEntity;
        if (address.id != null){
            val oldEntity = addressDao.findById(address.id).orElseThrow{
                throw ObjectNotFoundException("Address does not exist")
            }
            entity = oldEntity.copy(
                street = address.street,
                streetExtension = address.streetExtension,
                houseNumber = address.houseNumber,
                city = address.city,
                postalCode = address.postalCode,
                region = address.region,
                phoneNumber = address.phoneNumber,
                countryIso = address.countryIso
            )
        } else {
            entity = address.convert()
        }
        val updatedAddress = Address.convert(addressDao.save(entity))
        val updatedCustomer = customer.copy(address = updatedAddress)

        val updatedCustomerWithVat = if (resolveVat) {
            setCustomerValueAddedTax(updatedCustomer, updatedAddress.countryIso)
        } else  updatedCustomer.copy()

        customerDao.save(updatedCustomerWithVat.convert())
        return updatedAddress
    }

    @Transactional
    fun setCustomerVat(customerId: Long, vat: Boolean):Customer {
        val oldEntity = customerDao.findById(customerId).orElseThrow {
            throw ObjectNotFoundException("Customer does not exist")
        }

        val newEntity = oldEntity.copy(applyVat = vat)
        return Customer.convert(customerDao.save(newEntity))
    }

    @Transactional
    fun changeEmail(customerId: Long, email:String) {
        val customer = getCustomer(customerId)
        val updatedCustomer = customer.copy(email = email);
        customerDao.save(updatedCustomer.convert())
    }

    fun getCustomer(customerId:Long): Customer {
        val customer = customerDao.findById(customerId).orElseThrow {
            throw ObjectNotFoundException("Customer does not exist")
        }
        return Customer.convert(customer);
    }

    fun getCustomers(filter: CustomerFilterRequest): PageResult<List<Customer>> {
        val pageRequest = PageRequest.of(PaginationUtil.getPage(filter.offset, filter.limit), filter.limit, PaginationUtil.sorting(filter.sortOrder, filter.sortField))
        val page = customerDao.findAll(FilterSpecification(filter), pageRequest)
        return PageResult(page.totalElements, page.numberOfElements, convert(page.content))
    }

    fun getCustomerByEmail(email:String): Customer {
        val customer = customerDao.findByEmail(email)?:let{
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

    private fun setCustomerValueAddedTax(customer: Customer, countryIso: String): Customer {
        return customer.copy(applyVat = countryRepository.shouldVatBeApplied(countryIso))
    }

    private fun convert(customers:List<CustomerEntity>) = customers.map { Customer.convert(it) }.toList()

    private class FilterSpecification(val filter:CustomerFilterRequest): Specification<CustomerEntity> {

        override fun toPredicate(root: Root<CustomerEntity>, query: CriteriaQuery<*>, cb: CriteriaBuilder): Predicate? {
            var predicate: Predicate? = null;
            if (!filter.customerNr.isNullOrBlank()) {
                predicate = cb.equal (root.get<String>(""), filter.customerNr)
            }
            if (!filter.firstname.isNullOrBlank()) {
                predicate = predicate?.isNotNull?.let {
                    cb.and(it, cb.equal(root.get<String>(""), filter.firstname))
                } ?: cb.equal(root.get<String>(""), filter.firstname)
            }

            if (!filter.lastname.isNullOrBlank()) {
                predicate = predicate?.isNotNull?.let {
                    cb.and(it, cb.equal(root.get<String>(""), filter.lastname))
                } ?: cb.equal(root.get<String>(""), filter.lastname)
            }

            if (!filter.email.isNullOrBlank()) {
                predicate = predicate?.isNotNull?.let {
                    cb.and(it, cb.equal(root.get<String>(""), filter.email))
                } ?: cb.equal(root.get<String>(""), filter.email)
            }

            if (filter.status != null) {
                predicate = predicate?.isNotNull?.let {
                    cb.and(it, cb.equal(root.get<String>(""), filter.status))
                } ?: cb.equal(root.get<String>(""), filter.status)
            }

            if (!filter.city.isNullOrBlank()) {
                predicate = predicate?.isNotNull?.let {
                    cb.and(it, cb.equal(root.get<String>(""), filter.city))
                } ?: cb.equal(root.get<String>(""), filter.city)
            }

            if (!filter.country.isNullOrBlank()) {
                predicate = predicate?.isNotNull?.let {
                    cb.and(it, cb.equal(root.get<String>(""), filter.country))
                } ?: cb.equal(root.get<String>(""), filter.country)
            }

            if (!filter.language.isNullOrBlank()) {
                predicate = predicate?.isNotNull?.let {
                    cb.and(it, cb.equal(root.get<String>(""), filter.language))
                } ?: cb.equal(root.get<String>(""), filter.language)
            }
            return predicate
        }

        }
}
