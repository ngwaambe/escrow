package com.sicuro.escrow.persistence

import com.sicuro.escrow.exception.ObjectNotFoundException
import com.sicuro.escrow.model.*
import com.sicuro.escrow.persistence.dao.AddressDao
import com.sicuro.escrow.persistence.dao.CustomerDao
import com.sicuro.escrow.persistence.entity.CustomerEntity
import com.sicuro.escrow.util.PaginationUtil
import com.sicuro.escrow.util.security.CustomKeyGeneratorFactoryService
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import javax.transaction.Transactional

@Repository
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
            registration.contact.firstname,
            registration.contact.lastname,
            registration.contact.language,
            registration.contact.email,
            registration.organisation?.name,
            registration.organisation?.taxNumber)

        return Customer.convert(saveCustomer(customer))
    }

    @Transactional
    fun createCustomer(createCustomer: CreateCustomer): Customer {
        val customerNumber = generateCustomerNumber();
        val customer = Customer(
            null,
            customerNumber,
            createCustomer.contact.title,
            createCustomer.contact.firstname,
            createCustomer.contact.lastname,
            createCustomer.contact.title.gender,
            createCustomer.contact.email,
            createCustomer.contact.language,
            createCustomer.address,
            createCustomer.organisation?.name,
            createCustomer.organisation?.taxNumber,
            false,
            createCustomer.partnerId,
            createCustomer.identityNumber
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
            firstname = newEntity.firstname,
            lastname = newEntity.lastname,
            title = newEntity.title,
            gender = newEntity.title.gender,
            language = newEntity.language,
            organisation = newEntity.organisation,
            taxNumber = newEntity.taxNumber
        )

        return Customer.convert(customerDao.save(entity))
    }

    @Transactional
    fun updateAddress(customerId: Long, address: Address): Customer {
        val customerEntity = customerDao.findById(customerId).orElseThrow { ObjectNotFoundException("Customer object does not exist") }
        return address.id?.let { id ->
            if (id != customerEntity.address?.id) {
                throw ObjectNotFoundException("Address does not belong to customer")
            }
            val updatedAddressEntity = customerEntity.address!!.copy(
                street = address.street,
                streetExtension = address.streetExtension,
                houseNumber = address.houseNumber,
                city = address.city,
                postalCode = address.postalCode,
                region = address.region,
                countryIso = address.countryIso,
                phoneNumber = address.phoneNumber
            )
            addressDao.saveAndFlush(updatedAddressEntity)
            getCustomer(customerId)
        } ?: run {
            val updatedAddress = if (customerEntity.address != null) {
                address.copy(id = customerEntity.address?.id!!)
            } else address.copy()

            val addressEntity = addressDao.saveAndFlush(updatedAddress.convert())
            customerEntity.address = addressEntity
            Customer.convert(customerDao.saveAndFlush(customerEntity))
        }
    }

    @Transactional
    fun resolveCustomerVat(customer: Customer): Customer {
        val customerEntity = customerDao.findById(customer.id!!).orElseThrow {
            throw ObjectNotFoundException("Customer does not exist")
        }
        return customerEntity.address?.let {
          val updatedEntity =  customerEntity.copy(applyVat = countryRepository.shouldVatBeApplied(it.countryIso))
          Customer.convert(customerDao.saveAndFlush(updatedEntity))
        } ?: customer
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

    fun getCustomers(filter: CustomerFilter): PageResult<List<Customer>> {
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

    private class FilterSpecification(val filter: CustomerFilter) : Specification<CustomerEntity> {

        override fun toPredicate(root: Root<CustomerEntity>, query: CriteriaQuery<*>, cb: CriteriaBuilder): Predicate? {
            var predicate: Predicate? = null;
            if (!filter.customerNr.isNullOrBlank()) {
                predicate = cb.equal(root.get<String>("customerNumber"), filter.customerNr)
            }

            if (!filter.firstname.isNullOrBlank()) {
                predicate = predicate?.let {
                    cb.and(it, cb.like(cb.lower(root.get("firstname")), append("%",filter.firstname.toLowerCase(),"%")))
                } ?: cb.like(cb.lower(root.get("firstname")), append("%",filter.firstname.toLowerCase(),"%"))
            }

            if (!filter.lastname.isNullOrBlank()) {
                predicate = predicate?.let {
                     cb.and(it, cb.like(cb.lower(root.get<String>("lastname")), append("%",filter.lastname.toLowerCase(),"%")))
                } ?: cb.like(cb.lower(root.get<String>("lastname")), append("%",filter.lastname.toLowerCase(),"%"))
            }

            if (!filter.email.isNullOrBlank()) {
                predicate = predicate?.let {
                    cb.and(it, cb.like(cb.lower(root.get<String>("email")), filter.email.toLowerCase()))
                } ?: cb.like(cb.lower(root.get<String>("email")), filter.email.toLowerCase())
            }
            //TODO remove not available in customer pojo
            if (filter.status != null) {
                predicate = predicate?.isNotNull?.let {
                     cb.and(it, cb.equal(root.get<BaseStatus>("status"), filter.status))
                } ?: cb.equal(root.get<BaseStatus>("status"), filter.status)
            }

            if (!filter.city.isNullOrBlank()) {
                predicate = predicate?.let {
                    cb.and(it, cb.equal(cb.lower(root.get<Address>("address").get<String>("city")), filter.city.toLowerCase()))
                } ?: cb.equal(cb.lower(root.get<Address>("address").get<String>("city")), filter.city.toLowerCase())
            }

            if (!filter.country.isNullOrBlank()) {
                predicate = predicate?.let {
                    cb.and(it, cb.equal(root.get<Address>("address").get<String>("countryIso"), filter.country))
                } ?: cb.equal(root.get<Address>("address").get<String>("countryIso"), filter.country)
            }

            if (!filter.language.isNullOrBlank()) {
                predicate = predicate?.let {
                    cb.and(it, cb.equal(cb.lower(root.get<String>("language")), filter.language.toLowerCase()))
                } ?: cb.equal(cb.lower(root.get<String>("language")), filter.language.toLowerCase())
            }
            return predicate
        }

        fun append(prefix:String, data:String, suffix:String ):String = "$prefix$data$suffix"
    }
}
