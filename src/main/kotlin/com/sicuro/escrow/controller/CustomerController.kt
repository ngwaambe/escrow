package com.sicuro.escrow.controller

import com.sicuro.escrow.model.*
import com.sicuro.escrow.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/customers")
class CustomerController @Autowired constructor(val customerService: CustomerService) {


    @GetMapping("/{customerId}")
    fun get(@PathVariable("customerId")customerId: Long) = customerService.getCustomer(customerId)

    @PostMapping("/filter")
    fun get(@RequestBody @Valid filterRequest: CustomerFilterRequest) = customerService.getCustomers(filterRequest)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid createRequest: CustomerCreateRequest) = customerService.createCustomer(createRequest)

    @PutMapping("/{customerId}")
    fun update(@PathVariable("customerId")customerId: Long, @RequestBody @Valid   request: CustomerDetailRequest) = customerService.updateCustomer(customerId, request)

    @PutMapping("/{customerId}/address")
    fun updateAddress(@PathVariable("customerId")customerId: Long, @RequestBody @Valid   request: Address) = customerService.updateCustomerAddress(customerId, request)

    @GetMapping("/{customerId}/payment_methods")
    fun getPaymentMethod(@PathVariable("customerId")customerId: Long) = null

    @PutMapping("/{customerId}/changepassword")
    fun changePassword(@PathVariable("customerId")customerId: Long, @RequestBody @Valid  request: ChangePasswordRequest) = customerService.changePassword(customerId, request)

    @PutMapping("/{customerId}/changeemail")
    fun changeEmail(@PathVariable("customerId")customerId:Long, @RequestBody @Valid  request: ChangeEmailRequest) = customerService.changeEmail(customerId, request)

}
