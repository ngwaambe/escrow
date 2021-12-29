package com.sicuro.service_tests.persistence

import com.sicuro.escrow.EscrowAppApplication
import com.sicuro.escrow.model.BankAccount
import com.sicuro.escrow.model.PaymentAccountType
import com.sicuro.escrow.model.PaypalAccount
import com.sicuro.escrow.persistence.PaymentAccountRepository
import com.sicuro.escrow.persistence.dao.PaymentAccountDao
import com.sicuro.escrow.persistence.entity.PaypalAcccountEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Unroll
import util.applicationcontext.TestContextConfiguration
import util.database.DatabaseHelper
import util.database.NeedsEmbeddedMysql

@NeedsEmbeddedMysql
@SpringBootTest(classes = [EscrowAppApplication.class, TestContextConfiguration.class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentAccountRepositorySpec extends Specification {
    @Autowired
    PaymentAccountRepository paymentAccountRepository

    @Autowired
    PaymentAccountDao paymentAccountDao

    @Autowired
    DatabaseHelper databaseHelper

    def setup() {
        databaseHelper.cleanDatabase()
    }

    @Unroll
    def "create new PaymentAccount - happy path"() {
        given:
        def customerEntity = databaseHelper.createCustomerEntity([applyVat: false])

        when:
        def createdAccount = paymentAccountRepository.add(customerEntity.id, account)

        then:
        createdAccount != null

        and:
        with(createdAccount) {
            it.id != null
            it.owner == account.owner
            if (it.paymentType == PaymentAccountType.PAYPAL) {
                it.paypalAccount == (account as PaypalAccount).paypalAccount
            } else {
                it.bankName == (account as BankAccount).bankName
                it.iban == (account as BankAccount).iban
                it.swiftBic == (account as BankAccount).swiftCode
                it.city == (account as BankAccount).city
                it.postalCode == (account as BankAccount).postalCode
                it.countryIso == (account as BankAccount).countryIso
            }
        }
        where:
        account << [
                new PaypalAccount(
                        null,
                        "Elvis Ngwa Ambe",
                        "emial@emial.com"
                ),
                new BankAccount(
                        null,
                        "Elvis Ngwa Ambe",
                        "PostBank",
                        "78698698768768768976876",
                        "Swiftbic",
                        "Bonn",
                        "765756",
                        "de"
                )
        ]
    }

    def "Fetch payment accounts"() {
        given:
        def customerEntity = databaseHelper.createCustomerEntity([applyVat: false])
        def accounts = [
            new PaypalAccount(
                    null,
                    "Elvis Ngwa Ambe",
                    "emial@emial.com"
            ),
            new BankAccount(
                    null,
                    "Elvis Ngwa Ambe",
                    "PostBank",
                    "78698698768768768976876",
                    "Swiftbic",
                    "Bonn",
                    "765756",
                    "de"
            )
        ]
        databaseHelper.createPaymentAccount(customerEntity.id, accounts)

        expect:
        paymentAccountDao.findAll().size() == 2

        when:
        def results = paymentAccountRepository.getPaymentAccounts(customerEntity.id)

        then:
        results.size() == 2

    }

    def "update payment account - happy path"() {
        given:
        def customerEntity = databaseHelper.createCustomerEntity([applyVat: false])
        def account = paymentAccountRepository.add(
                customerEntity.id,
                new PaypalAccount(null,"Elvis Ngwa Ambe","emial@emial.com"))

        expect:
        account.id != null

        when:
        def result = paymentAccountRepository.update(
                customerEntity.id,
                new PaypalAccount(account.id, account.owner, "e.ngwa@email.com"))

        then:
        result != null
        def updatedAccount = paymentAccountDao.findById(result.id).get() as PaypalAcccountEntity
        updatedAccount.owner == account.owner
        updatedAccount.paypalAccount == "e.ngwa@email.com"

        when: "delete account"
        paymentAccountRepository.delete(account.id)

        then:
        paymentAccountDao.findAll().size() == 0

    }

}
