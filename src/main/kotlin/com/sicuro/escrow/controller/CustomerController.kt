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
    fun get(@RequestBody @Valid request: CustomerFilter) = customerService.getCustomers(request)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid request: CreateCustomer) = customerService.createCustomer(request)

    @PutMapping("/{customerId}")
    fun update(@PathVariable("customerId")customerId: Long, @RequestBody @Valid request: CustomerDetail) = customerService.updateCustomer(customerId, request)

    @PutMapping("/{customerId}/address")
    fun updateAddress(@PathVariable("customerId")customerId: Long, @RequestBody @Valid request: Address) = customerService.updateCustomerAddress(customerId, request)

    @GetMapping("/{customerId}/payment_methods")
    fun getPaymentMethod(@PathVariable("customerId")customerId: Long) = null

    @PutMapping("/{customerId}/change_password")
    fun changePassword(@PathVariable("customerId")customerId: Long, @RequestBody @Valid request: ChangePassword) = customerService.changePassword(customerId, request)

    @PutMapping("/{customerId}/change_email")
    fun changeEmail(@PathVariable("customerId")customerId: Long, @RequestBody @Valid request: ChangeEmail) = customerService.changeEmail(customerId, request)

    @PostMapping("/{customerId}/payment_account")
    fun addPaymentAccount(@PathVariable("customerId")customerId: Long, @RequestBody @Valid request: PaymentAccount) = customerService.addPaymentAccount(customerId, request)

    @PutMapping("/{customerId}/payment_account/{payment_account_id}")
    fun setDefaultPaymentAccount(@PathVariable("customerId")customerId: Long, @PathVariable("payment_account_id") paymentAccountId: Long) = customerService.setDefaultAccount(customerId, paymentAccountId)
}
