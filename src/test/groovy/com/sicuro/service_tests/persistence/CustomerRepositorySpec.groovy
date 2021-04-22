package com.sicuro.service_tests.persistence

import com.sicuro.escrow.EscrowAppApplication
import com.sicuro.escrow.model.Contact
import com.sicuro.escrow.model.Organisation
import com.sicuro.escrow.model.SignupRequest
import com.sicuro.escrow.persistence.CustomerRepository
import com.sicuro.escrow.persistence.dao.RoleDao
import com.sicuro.escrow.model.Title
import com.sicuro.escrow.persistence.entity.RoleEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification
import util.applicationcontext.TestContextConfiguration
import util.database.DatabaseHelper
import util.database.NeedsMysql

@Testcontainers
@NeedsMysql
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

    def "create customer"() {
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

        and:
        def role = roleDao.save(new RoleEntity(null, 'ROLE_CUSTOMER', 'ROLE_CUSTOMER', null, null))

        expect:
        role != null

        when:
        def result = customerRepository.createCustomer(request)

        then:
        result.second != null
        result.first.customerNumber != null
        result.first.firstName == request.contact.firstName
        result.first.lastName == request.contact.lastName
        result.first.email == request.contact.email
        result.first.organisation == request.organisation.name
        result.first.taxNumber == request.organisation.taxNumber


        and:
        def customer = databaseHelper.findCustomerByEmail("ngwaambe@hotmail.com")
        result.second != null
        result.first.customerNumber == customer.customerNumber
        result.first.firstName == customer.firstName
        result.first.lastName == customer.lastName
        result.first.email == customer.email
        result.first.organisation == customer.organisation
        result.first.taxNumber == customer.taxNumber
    }
}
