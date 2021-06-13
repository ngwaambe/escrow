package com.sicuro.service_tests.persistence

import com.sicuro.escrow.EscrowAppApplication
import com.sicuro.escrow.model.Address
import com.sicuro.escrow.model.Contact
import com.sicuro.escrow.model.Customer
import com.sicuro.escrow.model.CustomerCreateRequest
import com.sicuro.escrow.model.Organisation
import com.sicuro.escrow.model.SignupRequest
import com.sicuro.escrow.persistence.CustomerRepository
import com.sicuro.escrow.persistence.dao.RoleDao
import com.sicuro.escrow.model.Title
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import util.applicationcontext.TestContextConfiguration
import util.database.DatabaseHelper
import util.database.NeedsEmbeddedMysql

@NeedsEmbeddedMysql
@SpringBootTest(classes = [EscrowAppApplication.class, TestContextConfiguration.class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerRepositorySpec extends Specification{

    @Autowired
    CustomerRepository customerRepository

    @Autowired
    RoleDao roleDao

    @Autowired
    private DatabaseHelper databaseHelper

    def setup() {
        databaseHelper.cleanDatabase()
    }

    def "create customer via signup-request"() {
        given:
        def request = new SignupRequest(
            new Organisation("Sicuro", "00000111"),
            new Contact(
                Title.Mr,
                "de",
                "Elvis",
                "Ngwa Ambe",
                "ngwaambe@hotmail.com",
                "blablabla"
            )
        )
        def address = new Address(
                null, "Auf der Neid",
                "14",
                null ,
                "53242",
                "Remagen",
                "Rheinland Pfalz",
                "de", "1235432132130"
        )

        when:
        def customer = customerRepository.createCustomer(request)

        then:
        customer != null
        customer.customerNumber != null
        customer.title  == request.contact.title
        customer.firstname == request.contact.firstname
        customer.lastname == request.contact.lastname
        customer.email == request.contact.email
        customer.organisation == request.organisation.name
        customer.taxNumber == request.organisation.taxNumber


        and:
        def customerDb = databaseHelper.findCustomerByEmail("ngwaambe@hotmail.com")

        customer.customerNumber == customerDb.customerNumber
        customer.title  == customerDb.title
        customer.firstname == customerDb.firstname
        customer.lastname == customerDb.lastname
        customer.email == customerDb.email
        customer.organisation == customerDb.organisation
        customer.taxNumber == customerDb.taxNumber

        when: "update customer personal details"
        def updatedCustomer = customerRepository.updateCustomerDetails(
                new Customer(
                        customer.id, customer.customerNumber, Title.Mr_Dr,
                        "NewFirstname", "NewLastName", customer.gender,
                        customer.email, "en",null,
                        "Next", "1323223", customer.applyVat,
                        customer.partnerId, customer.identityNumber))

        then:
        def updatedCustomerDb = databaseHelper.findCustomerByEmail("ngwaambe@hotmail.com")

        updatedCustomer.customerNumber == updatedCustomerDb.customerNumber
        updatedCustomer.title  == updatedCustomerDb.title
        updatedCustomer.firstname == updatedCustomerDb.firstname
        updatedCustomer.lastname == updatedCustomerDb.lastname
        updatedCustomer.email == updatedCustomerDb.email
        updatedCustomer.language == updatedCustomerDb.language
        updatedCustomer.organisation == updatedCustomerDb.organisation
        updatedCustomer.taxNumber == updatedCustomerDb.taxNumber


        when: "Add customer address"
        updatedCustomer = customerRepository.updateAddress(updatedCustomer.id, address)

        then:
        updatedCustomer.address.id != null
        updatedCustomer.address.street == address.street
        updatedCustomer.address.streetExtension == address.streetExtension
        updatedCustomer.address.city == address.city
        updatedCustomer.address.postalCode == address.postalCode
        updatedCustomer.address.houseNumber == address.houseNumber
        updatedCustomer.address.countryIso == address.countryIso
        updatedCustomer.address.phoneNumber == address.phoneNumber

        when: "address is updated"
        def addressToUpdate = new Address(
                updatedCustomer.address.id,
                "Flossweg",
                "48",
                null,
                "53179",
                 "Bonn",
                "Nordrhein Westfalen",
                updatedCustomer.address.countryIso,
                updatedCustomer.address.phoneNumber)

        updatedCustomer = customerRepository.updateAddress(updatedCustomer.id, addressToUpdate)

        then:
        updatedCustomer.address.id != null
        updatedCustomer.address.street == "Flossweg"
        updatedCustomer.address.city == "Bonn"
        updatedCustomer.address.postalCode == "53179"
        updatedCustomer.address.houseNumber == "48"
        updatedCustomer.address.region == "Nordrhein Westfalen"
    }

    def "set customer vat value" () {
        given:
        def customerEntity = databaseHelper.createCustomerEntity([applyVat: initVatValue])

        expect:
        customerEntity != null
        customerEntity.applyVat == initVatValue

        when:
        def customer = customerRepository.setCustomerVat(customerEntity.id, finalVatValue)

        then:
        customer.applyVat == finalVatValue

        where:
        initVatValue | finalVatValue
        false        | true
        true         | false
    }

    def "create customer via create-customer-request"() {
        given:
        def request = new CustomerCreateRequest(
                new Contact(
                        Title.Mr,
                        "de",
                        "Elvis",
                        "Ngwa Ambe",
                        "ngwaambe@hotmail.com",
                        "blablabla"
                ),
                new Address(null, "Auf der Neide", "14", null, "53424","Remagen", null, "de", "015732565255"),
                ["ROLE_CUSTOMER", "ROLE_AGENT"],
                new Organisation("Sicuro", "00000111"), null, null
        )
        def country = databaseHelper.createCountry("de", "Germany")

        expect:
        country != null

        when:
        def customer = customerRepository.createCustomer(request)

        then:
        customer != null
        customer.customerNumber != null
        customer.firstname == request.contact.firstname
        customer.lastname == request.contact.lastname
        customer.email == request.contact.email
        customer.organisation == request.organisation.name
        customer.taxNumber == request.organisation.taxNumber
        customer.address.street == request.address.street
        customer.address.houseNumber == request.address.houseNumber
        customer.address.postalCode == request.address.postalCode
        customer.address.city == request.address.city
        customer.address.countryIso == request.address.countryIso
        customer.address.phoneNumber == request.address.phoneNumber


        and:
        def customerDb = databaseHelper.findCustomerByEmail("ngwaambe@hotmail.com")

        customer.customerNumber == customerDb.customerNumber
        customer.firstname == customerDb.firstname
        customer.lastname == customerDb.lastname
        customer.email == customerDb.email
        customer.organisation == customerDb.organisation
        customer.taxNumber == customerDb.taxNumber
        customer.address.street == customerDb.address.street
        customer.address.houseNumber == customerDb.address.houseNumber
        customer.address.postalCode == customerDb.address.postalCode
        customer.address.city == customerDb.address.city
        customer.address.countryIso == customerDb.address.countryIso
        customer.address.phoneNumber == customerDb.address.phoneNumber
    }

    def "change customer email address"() {
        given:
        def initialEmail = "soma@email.com"
        def newEmail = "new@email.com"
        def customerEntity = databaseHelper.createCustomerEntity([email: initialEmail])

        expect:
        customerEntity != null
        customerEntity.email == initialEmail
        databaseHelper.findCustomerByEmail(newEmail) == null

        when:
        customerRepository.changeEmail(customerEntity.id, newEmail)

        then:
        databaseHelper.findCustomerByEmail(newEmail) != null
        databaseHelper.findCustomerByEmail(initialEmail)  == null
    }

    def "set customer address"() {

    }
}
