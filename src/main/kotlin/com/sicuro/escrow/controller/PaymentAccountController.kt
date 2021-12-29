package com.sicuro.escrow.controller

import com.sicuro.escrow.model.PaymentAccount
import com.sicuro.escrow.persistence.PaymentAccountRepository
import com.sicuro.escrow.util.security.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/customers")
class PaymentAccountController @Autowired constructor(
    val paymentAccountRepository: PaymentAccountRepository,
    val jwtUtils: JwtUtils) {

    @GetMapping("/{customerId}/payment_accounts")
    fun getPaymentAccounts(@NotNull @RequestHeader("Authorization") headerAuth: String, @PathVariable("customerId")customerId: Long): List<PaymentAccount> {
        jwtUtils.userIsAuthenticatedUser(customerId, headerAuth)
        return paymentAccountRepository.getPaymentAccounts(customerId);
    }

    @PostMapping("/{customerId}/payment_accounts")
    fun createPaymentAccount(@NotNull @RequestHeader("Authorization") headerAuth: String, @PathVariable("customerId")customerId: Long, @RequestBody @Valid request: PaymentAccount): PaymentAccount {
        jwtUtils.userIsAuthenticatedUser(customerId, headerAuth)
        return paymentAccountRepository.add(customerId, request)
    }

    @PutMapping("/{customerId}/payment_accounts")
    fun updatePaymentAccount(@NotNull @RequestHeader("Authorization") headerAuth: String, @PathVariable("customerId")customerId: Long, @RequestBody @Valid request: PaymentAccount): PaymentAccount {
        jwtUtils.userIsAuthenticatedUser(customerId, headerAuth)
        return paymentAccountRepository.update(customerId, request)
    }
    @GetMapping("/{customerId}/payment_accounts/{paymentAccountId}")
    fun getPaymentAccount(@NotNull @RequestHeader("Authorization") headerAuth: String, @PathVariable("customerId")customerId: Long, @PathVariable("paymentAccountId") paymentAccountId: Long) : PaymentAccount {
        jwtUtils.userIsAuthenticatedUser(customerId, headerAuth)
        return paymentAccountRepository.getPaymentAccount(customerId, paymentAccountId)
    }

    @DeleteMapping("/{customerId}/payment_accounts/{paymentAccountId}")
    fun deletePaymentAccount(@NotNull @RequestHeader("Authorization") headerAuth: String, @PathVariable("customerId")customerId: Long, @PathVariable("paymentAccountId") paymentAccountId: Long) {
        jwtUtils.userIsAuthenticatedUser(customerId, headerAuth)
        paymentAccountRepository.delete(customerId, paymentAccountId)
    }
}
