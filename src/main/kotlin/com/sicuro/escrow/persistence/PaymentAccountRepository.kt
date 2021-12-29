package com.sicuro.escrow.persistence

import com.sicuro.escrow.exception.ObjectNotFoundException
import com.sicuro.escrow.model.PaymentAccount
import com.sicuro.escrow.persistence.dao.CustomerDao
import com.sicuro.escrow.persistence.dao.CustomerPaymentAccountDao
import com.sicuro.escrow.persistence.dao.PaymentAccountDao
import com.sicuro.escrow.persistence.entity.CustomerPaymentAccountEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class PaymentAccountRepository(
    val customerPaymentAccountDao: CustomerPaymentAccountDao,
    val paymentAccountDao: PaymentAccountDao,
    val customerDao: CustomerDao
) {

    @Transactional
    fun add(customerId: Long, paymentAccount: PaymentAccount): PaymentAccount {
        customerDao.findById(customerId).orElseThrow { throw ObjectNotFoundException("Customer does not exist") }
        val accountEntity = customerPaymentAccountDao.saveAndFlush(
            CustomerPaymentAccountEntity(
                null,
                customerId,
                customerPaymentAccountDao.countByCustomerId(customerId) == 0L,
                paymentAccount.convert()
            )
        )
        return PaymentAccount.convert(accountEntity.paymentAccount)
    }

    @Transactional
    fun update(customerId: Long, paymentAccount: PaymentAccount): PaymentAccount {
        logger.info("updating paymentaccount: {}", paymentAccount.id)
        customerDao.findById(customerId).orElseThrow { throw ObjectNotFoundException("Customer does not exist") }
        if (paymentAccount.id == null)
            throw ObjectNotFoundException("Payment account does not exist")

        paymentAccountDao.findById(paymentAccount.id!!).orElseThrow {
            throw ObjectNotFoundException("Payment account does not exist")
        }

        val updatedAccountEntity = paymentAccountDao.saveAndFlush(paymentAccount.convert())

        return PaymentAccount.convert(updatedAccountEntity)
    }

    fun getPaymentAccounts(customerId: Long): List<PaymentAccount> =
        customerPaymentAccountDao.findAllByCustomerId(customerId).map { PaymentAccount.convert(it.paymentAccount) }.toList()

    fun getPaymentAccount(customerId: Long, paymentAccountId: Long): PaymentAccount {
        return customerPaymentAccountDao.findByCustomerIdAndPaymentAccountId(customerId,paymentAccountId)?.let {
            PaymentAccount.convert(it.paymentAccount)
        }?: throw ObjectNotFoundException("Payment account does not exist")
    }
    @Transactional
    fun setDefaultCustomerAccount(customerId: Long, paymentAcountId: Long) {
        val accounts = customerPaymentAccountDao.findAllByCustomerId(customerId)
        if (!accounts.any { it.paymentAccount.id == paymentAcountId }) {
            throw ObjectNotFoundException("Payment account does not belong to customer")
        }
        accounts.find { it.defaultAccount }?.let {
            val defaultAccount = it.copy(defaultAccount = false)
            customerPaymentAccountDao.saveAndFlush(defaultAccount)
        }
        accounts.find { it.paymentAccount.id == paymentAcountId }?.let {
            val defaultAccount = it.copy(defaultAccount = true)
            customerPaymentAccountDao.saveAndFlush(defaultAccount)
        }
    }

    @Transactional
    fun delete(customerId: Long, paymentAccountId: Long) {
        customerPaymentAccountDao.findByCustomerIdAndPaymentAccountId(customerId, paymentAccountId)?.let {
            logger.info("Delete paymentaccount: {}", paymentAccountId)
            customerPaymentAccountDao.deleteById(it.id!!)
        }
    }

    companion object {
        val logger = LoggerFactory.getLogger(PaymentAccountRepository::class.java)
    }
}
