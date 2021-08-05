package com.sicuro.escrow.persistence

import com.sicuro.escrow.model.PaymentAccount
import com.sicuro.escrow.persistence.dao.CustomerDao
import com.sicuro.escrow.persistence.dao.CustomerPaymentAccountDao
import com.sicuro.escrow.exception.ObjectNotFoundException
import com.sicuro.escrow.model.CustomerPaymentAccount
import com.sicuro.escrow.persistence.dao.PaymentAccountDao
import com.sicuro.escrow.persistence.entity.CustomerPaymentAccountEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class CustomerPaymentAccountRepository(
    val customerPaymentAccountDao: CustomerPaymentAccountDao,
    val paymentAccountDao: PaymentAccountDao,
    val customerDao: CustomerDao) {

    @Transactional
    fun add(customerId: Long, paymentAccount: PaymentAccount) : CustomerPaymentAccount{
        customerDao.findById(customerId).orElseThrow{ throw ObjectNotFoundException("Customer does not exist") }
        val paymentAccountEntity = paymentAccountDao.saveAndFlush(paymentAccount.convert())

        return CustomerPaymentAccount.convert(
            customerPaymentAccountDao.saveAndFlush(
                CustomerPaymentAccountEntity(
                    null,
                    customerId,
                    customerPaymentAccountDao.countByCustomerId(customerId) == 0L,
                    paymentAccountEntity)
            ))
    }

    fun getCustomerPaymentAccounts(customerId: Long) =
        customerPaymentAccountDao.findAllByCustomerId(customerId).map { CustomerPaymentAccount.convert(it) }.toList()

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
        accounts.find { it.paymentAccount.id == paymentAcountId}?.let {
            val defaultAccount = it.copy(defaultAccount = true)
            customerPaymentAccountDao.saveAndFlush(defaultAccount)
        }
    }

    @Transactional
    fun delete(id:Long) {
        if (customerPaymentAccountDao.existsById(id))
            customerPaymentAccountDao.deleteById(id)
    }

}
