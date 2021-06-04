package com.sicuro.escrow.controller

import com.sicuro.escrow.model.*
import com.sicuro.escrow.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/customers")
class CustomerController @Autowired constructor(val customerService: CustomerService) {


    @GetMapping("/{customerId}")
    fun getCustomer(@PathVariable("customerId")customerId: Long) = customerService.getCustomer(customerId)

    @GetMapping
    fun getCustomers(@RequestBody @Valid filter: CustomerFilterRequest) = customerService.getCustomers(filter)

    @PostMapping
    fun createCustomwer(@RequestBody @Valid createRequest: CustomerCreateRequest) = customerService.createCustomer(createRequest)

    @PutMapping("/{customerId}")
    fun updateCustomer(@PathVariable("customerId")customerId: Long, @RequestBody @Valid   request: CustomerDetailRequest) = customerService.updateCustomer(customerId, request)

    @PutMapping("/{customerId}/changepassword")
    fun changePassword(@PathVariable("customerId")customerId: Long, @RequestBody @Valid  request: ChangePasswordRequest) = customerService.changePassword(customerId, request)

    @PutMapping("/{customerId}/changeemail")
    fun changeEmail(@PathVariable("customerId")customerId:Long, @RequestBody @Valid  request: ChangeEmailRequest) = customerService.changeEmail(customerId, request)

}
