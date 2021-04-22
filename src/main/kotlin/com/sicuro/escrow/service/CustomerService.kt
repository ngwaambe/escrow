package com.sicuro.escrow.service

import com.sicuro.escrow.model.ChangeEmailRequest
import com.sicuro.escrow.model.ChangePasswordRequest
import com.sicuro.escrow.model.Customer
import com.sicuro.escrow.model.CustomerDetailRequest
import com.sicuro.escrow.persistence.CustomerRepository
import com.sicuro.escrow.persistence.UserRepository
import org.hibernate.ObjectNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerService @Autowired constructor(val customerRepository: CustomerRepository, val userRepository: UserRepository) {

    fun getCustomer(customerId:Long) = customerRepository.getCustomer(customerId)

    fun updateCustomer(customerId:Long, request: CustomerDetailRequest):Customer {
        val customer = customerRepository.getCustomer(customerId);

        val updatedCustomer = customer.copy(
            title = request.title,
            preferedLanguage = request.preferredLanguage,
            firstName = request.firstName,
            lastName = request.lastName,
            organisation = request.organisation.name,
            taxNumber = request.organisation.taxNumber
        )
        return customerRepository.updateCustomerDetails(updatedCustomer)
    }

    fun changePassword(customerId:Long, request: ChangePasswordRequest) {
        val customer = customerRepository.getCustomer(customerId)
        userRepository.changePassword(customer.email, request.password)
    }

    fun changeEmail(customerId:Long, request: ChangeEmailRequest) {
        val customer = customerRepository.getCustomer(customerId)

    }
}
