package com.sicuro.escrow.controller

import com.sicuro.escrow.model.ChangeEmailRequest
import com.sicuro.escrow.model.ChangePasswordRequest
import com.sicuro.escrow.model.Customer
import com.sicuro.escrow.model.CustomerDetailRequest
import com.sicuro.escrow.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerController @Autowired constructor(val customerService: CustomerService) {

    @GetMapping("/{customerId}")
    fun getCustomer(@PathVariable("customerId")customerId: Long):Customer {
        return customerService.getCustomer(customerId);
    }

    @PutMapping("/{customerId}")
    fun updateCustomer(@PathVariable("customerId")customerId: Long,
                       @RequestBody request: CustomerDetailRequest) = customerService.updateCustomer(customerId, request)

    @PutMapping("/{customerId}")
    fun changePassword(@PathVariable("customerId")customerId: Long,
                       @RequestBody request: ChangePasswordRequest) = customerService.changePassword(customerId, request)

    @PutMapping("/{customerId}")
    fun changeEmail(@PathVariable("customerId")customerId:Long,
                    @RequestBody request: ChangeEmailRequest) = customerService.changeEmail(customerId, request)
}
