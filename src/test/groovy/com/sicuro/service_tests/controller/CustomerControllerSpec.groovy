package com.sicuro.service_tests.controller

import com.sicuro.escrow.EscrowAppApplication
import com.sicuro.escrow.model.LinkType
import com.sicuro.escrow.model.Title
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Specification
import spock.lang.Unroll
import util.RequestHelper
import util.applicationcontext.TestContextConfiguration
import util.database.DatabaseHelper
import util.database.NeedsEmbeddedMysql
import util.mail.GreenMailServer
import util.mail.NeedGreenMail

@NeedsEmbeddedMysql
@NeedGreenMail
@SpringBootTest(classes = [EscrowAppApplication.class, TestContextConfiguration.class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerSpec extends Specification {

    @LocalServerPort
    private int port

    @Autowired
    private RequestHelper requestHelper

    @Autowired
    private DatabaseHelper databaseHelper
    private String username = "ngwaambe@hotmail.com"
    private String password = "12345678"

    def setup() {
        databaseHelper.cleanDatabase()
        signupAndActivateAccount(username, password)
    }

    def cleanup() {
        databaseHelper.cleanDatabase()
    }

    def "get customer,  happy path"() {
        given:
        def token = fetchToken(username, password);
        def customerId = databaseHelper.findCustomerByEmail(username).id


        expect:
        customerId != null
        token != null

        when:
        def response = requestHelper.get(port:port, token: token, path: "/api/customers/${customerId}")

        then:
        response.status == 200
        response.data.id == customerId
        response.data.customerNumber != null
        response.data.title == 'Mr'
        response.data.firstName == 'Peter'
        response.data.lastName == 'The Great'
        response.data.gender =='MALE'
        response.data.email ==  username
        response.data.language == 'en'
        response.data.organisation == 'sicuro'
        response.data.taxNumber == '213415645'
        response.data.applyVat == false
        response.data.address == null
        response.data.partnerId == null
        response.data.identityNumber == null
    }

    @Unroll
    def "get customer,  not authorized #title"() {

        when:
        def response = requestHelper.get(port:port, token: token, path: "/api/customers/1")

        then:
        response.status == 401

        where:
        title           | token
        'invalid token' | ''
        'expired token' | 'eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2MjIxODQ1NDcsInN1YiI6ImUubmd3YUB0YXJlbnQuZGUiLCJleHAiOjE2MjIxODQ3ODcsImN1c3RvbWVySWQiOjIsInRlbXBQd2QiOmZhbHNlfQ.rZTxite8J3txlgfDypRsUnF2vSkoOiijRLKkW8Ra-LjZahe6w8G1krkTml2Tyo2J1VPizBCakKPDpEyUcnU-3Q'
    }

    def "update customer personal data"() {
        given:
        def token = fetchToken(username, password);
        def customer = databaseHelper.findCustomerByEmail(username)

        expect:
        customer != null
        customer.title == Title.Mr
        customer.firstName == 'Peter'
        customer.lastName == 'The Great'
        customer.organisation == 'sicuro'
        customer.taxNumber == '213415645'
        token != null

        when:
        def response = requestHelper.put(port:port, token: token, path: "/api/customers/${customer.id}", body: customerDetailPayload("Samsung", "Toyota",  null ))

        then:
        response.status == 200
        response.data.id == customer.id
        response.data.customerNumber != null
        response.data.title == 'Mr'
        response.data.firstName == 'Samsung'
        response.data.lastName == 'Toyota'
        response.data.gender =='MALE'
        response.data.email ==  username
        response.data.language == 'en'
        response.data.organisation == 'sicuro'
        response.data.taxNumber == '213415645'
        response.data.applyVat == false
        response.data.address == null
        response.data.partnerId == null
        response.data.identityNumber == null


        when:
        response = requestHelper.put(port:port, token: token, path: "/api/customers/${customer.id}", body: customerDetailPayload("Samsung", "Toyota",  [name:"Bella", taxNumber: "124523"]))

        then:
        response.status == 200
        response.data.id == customer.id
        response.data.customerNumber != null
        response.data.title == 'Mr'
        response.data.firstName == 'Samsung'
        response.data.lastName == 'Toyota'
        response.data.gender =='MALE'
        response.data.email ==  username
        response.data.language == 'en'
        response.data.organisation == 'Bella'
        response.data.taxNumber == '124523'
        response.data.applyVat == false
        response.data.address == null
        response.data.partnerId == null
        response.data.identityNumber == null

    }

    def "change password"() {
        given:
        def token = fetchToken(username, password);
        def customerId = databaseHelper.findCustomerByEmail(username).id


        expect:
        customerId != null
        token != null

        when:
        def response = requestHelper.put(port:port, token: token, path: "/api/customers/${customerId}/changepassword", body: JsonOutput.toJson([password:"password"]))

        then:
        response.status == 200
    }

    private void signupAndActivateAccount(String username, String password) {
        def payload = signupPayload(username, password)
        assert requestHelper.post(port: port, path: "/api/auth/signup",body: payload).status == 200

        def activationId = databaseHelper.findLinkId(username, LinkType.ACCOUNT_ACTIVATION)
        assert activationId != null

        assert requestHelper.get(port: port, path: "/api/auth/activate_account/${activationId}").status == 200
    }

    private String fetchToken(String username, String password) {
        def response =  requestHelper.post(port: port, path: "/api/auth/token", body: JsonOutput.toJson([username:"${username}", password:"${password}"]))
        assert response.status ==  200
        return response.data.access_token
    }

    private String signupPayload(username='ngwaambe@hotmail.com', password='12elviscoNGWA') {
        return JsonOutput.toJson(
        [
            organisation:[
                name:"sicuro",
                taxNumber:"213415645"
            ],
            contact: [
                title:"Mr",
                language:"en",
                firstName:"Peter",
                lastName: "The Great",
                email:"${username}",
                password:"${password}"
            ]
        ])
    }

    private String customerDetailPayload(firstname = "Peter", lastName = "The Great", organisation = [name: "sicuro", taxNumber: "113213213"]) {
        return JsonOutput.toJson(
            [
                    title       : "Mr",
                    language    : "en",
                    firstName   : "${firstname}",
                    lastName    : "${lastName}",
                    organisation: organisation
            ])
    }


}
