package com.sicuro.escrow.persistence

import com.sicuro.escrow.model.PaymentAccount
import com.sicuro.escrow.persistence.dao.PaymentAccountDao
import org.springframework.stereotype.Repository

@Repository
class PaymentAccountRepository(val paymentAccountDao: PaymentAccountDao) {

    fun saveUpdate(paymentAccount: PaymentAccount) = paymentAccountDao.saveAndFlush(paymentAccount.convert())

}
