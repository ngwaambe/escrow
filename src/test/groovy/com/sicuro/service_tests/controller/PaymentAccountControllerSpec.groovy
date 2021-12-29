package com.sicuro.service_tests.controller

import com.sicuro.escrow.EscrowAppApplication
import com.sicuro.escrow.model.BankAccount
import com.sicuro.escrow.model.PaypalAccount
import com.sicuro.escrow.persistence.entity.BankAccountEntity
import com.sicuro.escrow.persistence.entity.PaypalAcccountEntity
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Specification
import util.RequestHelper
import util.applicationcontext.TestContextConfiguration
import util.database.DatabaseHelper
import util.database.NeedsEmbeddedMysql
import util.mail.NeedGreenMail

@NeedsEmbeddedMysql
@NeedGreenMail
@SpringBootTest(classes = [EscrowAppApplication.class, TestContextConfiguration.class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentAccountControllerSpec extends Specification{

    @LocalServerPort
    private int port

    @Autowired
    private RequestHelper requestHelper

    @Autowired
    private DatabaseHelper databaseHelper
    private String username = "ngwaambe@hotmail.com"
    private String password = "12345678"

    def setup() {
        requestHelper.signupAndActivateAccount(username, password, port, databaseHelper)
    }

    def cleanup() {
        databaseHelper.cleanDatabase()
    }

    def "create payment account - happy path"() {
        given:
        def (customerId, token) = setupCustomer()

        and: "creation payload"
        def payload = JsonOutput.toJson(account)
        println('##<'+payload+'>##')


        when:
        def response = requestHelper.post(port:port, token: token, path: "/api/customers/${customerId}/payment_account", body:payload)

        then:
        response.status ==200

        with(response.data) {
            it.id != null
            it.owner == account.owner
            if (it.paymentType == "PAYPAL") {
                it.paypalAccount == (account as PaypalAccount).paypalAccount
            } else {
                it.bankName == (account as BankAccount).bankName
                it.iban == (account as BankAccount).iban
                it.swiftCode == (account as BankAccount).swiftCode
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
                        "DE58670700100700680200",
                        "DEUTDESMXXX",
                        "Bonn",
                        "53424",
                        "DE"
                )
        ]
    }

    def "Update paypal payment account"() {
        given:
        def (customerId, token) = setupCustomer()
        databaseHelper.createPaymentAccount(customerId, List.of(account))

        expect:
        databaseHelper.countPaymentAccount() == 1

        and:
        def existingAccount = databaseHelper.getPaymentAccount().first()
        assert existingAccount != null
        assert existingAccount.owner == account.owner

        and:
        def updateAccount = new PaypalAccount(existingAccount.id, newAccount[0], newAccount[1])

        when:
        def response = requestHelper.put(port:port, token: token, path: "/api/customers/${customerId}/payment_accounts", body:JsonOutput.toJson(updateAccount))

        then:
        response.status ==200

        with(response.data) {
            it.id == existingAccount.id
            it.owner == newAccount[0]
            it.paypalAccount == newAccount[1]
        }

        and:
        def data = databaseHelper.getPaymentAccount(existingAccount.id)
        with(data) {
            it.owner == newAccount[0]
            (it as PaypalAcccountEntity).paypalAccount == newAccount[1]
        }

        where:
        account << [
                new PaypalAccount(
                        null,
                        "Elvis Ngwa Ambe",
                        "emial@emial.com"
                ),
        ]

        newAccount << [ ["Elvis Ngwa Ambe" , "emial@emial.com1"]]
    }

    def "Update bank payment account"() {
        given:"create and login user"
        def (customerId, token) = setupCustomer()

        and:"create a bank acount"
        databaseHelper.createPaymentAccount(customerId, List.of(account))

        expect:
        databaseHelper.countPaymentAccount() == 1

        and:
        def existingAccount = databaseHelper.getPaymentAccount().first()
        assert existingAccount != null
        assert existingAccount.owner == account.owner

        and:
        def updateAccount = new BankAccount(
                existingAccount.id,
                newAccount[0],
                newAccount[1],
                newAccount[2],
                newAccount[3],
                newAccount[4],
                newAccount[5],
                newAccount[6],)

        when: "Update existing account"
        def response = requestHelper.put(port:port, token: token, path: "/api/customers/${customerId}/payment_accounts", body:JsonOutput.toJson(updateAccount))

        then:
        response.status ==200

        with(response.data) {
            it.id == existingAccount.id
            it.owner == newAccount[0]
            it.bankName == newAccount[1]
            it.iban == newAccount[2]
            it.swiftCode == newAccount[3]
            it.city == newAccount[4]
            it.postalCode == newAccount[5]
            it.countryIso == newAccount[6]
        }

        and:
        def data = databaseHelper.getPaymentAccount(existingAccount.id)
        with(data) {
            it.owner == newAccount[0]
            (it as BankAccountEntity).bankName == newAccount[1]
            (it as BankAccountEntity).iban == newAccount[2]
            (it as BankAccountEntity).swiftCode == newAccount[3]
            (it as BankAccountEntity).city == newAccount[4]
            (it as BankAccountEntity).postalCode == newAccount[5]
            (it as BankAccountEntity).countryIso == newAccount[6]
        }

        where:
        account << [
            new BankAccount(
                null,
                "Elvis Ngwa Ambe",
                "PostBank",
                "DE58670700100700680200",
                "DEUTDESMXXX",
                "Bonn",
                "53424",
                "DE"
            ),
        ]

        newAccount << [ ["Elvis Ngwa Ambe",  "PostBank", "DE58670700100700680200","DEUTDESMXXX","Bonn","53424","DE"]]
    }

    def "Delete payment account"() {
        given:
        def (customerId, token) = setupCustomer()
        databaseHelper.createPaymentAccount(customerId, List.of(account))

        expect:
        databaseHelper.countPaymentAccount() == 1

        and:
        def existingAccount = databaseHelper.getPaymentAccount().first()
        assert existingAccount != null

        when:
        def response = requestHelper.delete(port:port, token: token, path: "/api/customers/${customerId}/payment_accounts/${existingAccount.id}")

        then:
        response.status ==200

        and:
        databaseHelper.getPaymentAccount(existingAccount.id) == null
        databaseHelper.getPaymentAccount().size() == 0

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
                "DE58670700100700680200",
                "DEUTDESMXXX",
                "Bonn",
                "53424",
                "DE"
            )
        ]

    }

    def "Login user accessing another user data return 409"() {
        given:
        def (_, token) = setupCustomer()

        when:
        def response = requestHelper.delete(port:port, token: token, path: "/api/customers/2/payment_accounts/0")

        then:
        response.status == 409
    }

    def "Access not existing account returns 404"() {
        given:
        def (customerId, token) = setupCustomer()

        when:
        def result = requestHelper.get(port:port, token: token, path: "/api/customers/${customerId}/payment_accounts/1")

        then:
        result.status == 404
    }

    def "Access without login fails"() {
        when:
        def result = requestHelper.get(port:port, token: "token", path: "/api/customers/1/payment_accounts/1")

        then:
        result.status == 401
    }

    private Tuple2<Long, String> setupCustomer() {
        def customer = databaseHelper.findCustomerByEmail(username)
        def token = requestHelper.fetchToken(username, password, port)

        assert token != null
        assert customer != null
        return new Tuple2<Long, String>(customer.id, token)
    }
}
