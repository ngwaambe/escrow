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
import util.DataHelper
import util.RequestHelper
import util.applicationcontext.TestContextConfiguration
import util.database.DatabaseHelper
import util.database.NeedsEmbeddedMysql
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
        response.data.firstname == 'Peter'
        response.data.lastname == 'The Great'
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
        customer.firstname == 'Peter'
        customer.lastname == 'The Great'
        customer.organisation == 'sicuro'
        customer.taxNumber == '213415645'
        token != null

        and: "Payload"
        def payload = JsonOutput.toJson(DataHelper.contactParams([firstname:"Samsung", lastname:"Toyota"]))

        when:
        def response = requestHelper.put(port:port, token: token, path: "/api/customers/${customer.id}", body: payload)

        then:
        response.status == 200
        response.data.id == customer.id
        response.data.customerNumber != null
        response.data.title == 'Mr'
        response.data.firstname == 'Samsung'
        response.data.lastname == 'Toyota'
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
        payload = JsonOutput.toJson( DataHelper.contactParams([firstname:"Samsung", lastname:"Toyota", organisation:[name:"Bella", taxNumber: "124523"]]))
        response = requestHelper.put(port:port, token: token, path: "/api/customers/${customer.id}", body: payload)

        then:
        response.status == 200
        response.data.id == customer.id
        response.data.customerNumber != null
        response.data.title == 'Mr'
        response.data.firstname == 'Samsung'
        response.data.lastname == 'Toyota'
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
        def response = requestHelper.put(port:port, token: token, path: "/api/customers/${customerId}/change_password", body: JsonOutput.toJson([currentPassword: password , password:"password"]))

        then:
        response.status == 200
    }

    def "get customers, happy path"() {
        given:
        databaseHelper.addAgentRoleToUser(username)
        def token = fetchToken(username, password);
        def customerId = databaseHelper.findCustomerByEmail(username).id
        def payload = DataHelper.customerFilterParams([offset:0, limit:10])

        expect:
        customerId != null
        token != null

        when:
        def response = requestHelper.post(port:port, token: token, path: "/api/customers/filter", body:payload)

        then:
        response.status == 200
        response.data.totalResultCount == 1
        response.data.resultCount == 1
        response.data.data[0].email == username
        response.data.data[0].id == customerId
    }

    def "create new customer  happy path"() {
        given:
        def customer = databaseHelper.findCustomerByEmail(username)
        def token = fetchToken(username, password)

        and: "Add Agent role to user"
        databaseHelper.addAgentRoleToUser(username)

        and: "make sure a country for address exist"
        def country = databaseHelper.createCountry("de", "Germany")

        expect:
        token != null
        country != null
        customer != null

        and: "creation payload"
        def payload = JsonOutput.toJson([
                contact: DataHelper.contactParams([
                        title:Title.Mr,
                        firstname:"Olle",
                        lastname:"Lalla",
                        email:"me@kribisoft.com",
                        language: "en"]),
                address: DataHelper.addresssParams([
                        street:"Auf der Neide",
                        houseNumber:"14 A",
                        streetExtension:null,
                        postalCode:"53424",
                        city:"Remagen",
                        countryIso:"de",
                        phoneNumber:"123645789"]),
                organisation:DataHelper.organisationParams([
                        name:"kribisoft llc",
                        taxNumber: "1236455"]),
                roles:["ROLE_CUSTOMER"],
                partnerId:customer.customerNumber,
                identityNumber: "123456987"
        ])

        when:
        def response = requestHelper.post(port:port, token: token, path: "/api/customers", body:payload)


        then:
        response.status == 201
        response.data.id != 0
        response.data.customerNumber != null
        response.data.title == Title.Mr.toString()
        response.data.firstname == 'Olle'
        response.data.lastname == 'Lalla'
        response.data.gender ==Title.Mr.gender.toString()
        response.data.email ==  "me@kribisoft.com"
        response.data.language == 'en'
        response.data.organisation == 'kribisoft llc'
        response.data.taxNumber == '1236455'
        response.data.applyVat == true
        response.data.address != null
        response.data.address.street == "Auf der Neide"
        response.data.address.houseNumber == "14 A"
        response.data.address.streetExtension == null
        response.data.address.postalCode == "53424"
        response.data.address.city == "Remagen"
        response.data.address.countryIso == "de"
        response.data.address.phoneNumber == "123645789"
        response.data.partnerId == customer.customerNumber
        response.data.identityNumber == "123456987"
    }

    def "create new customer - not authorized"() {
        given:
        def token = fetchToken(username, password)

        expect:
        token != null

        and: "creation payload"
        def payload = JsonOutput.toJson([:])

        when:
        def response = requestHelper.post(port:port, token: token, path: "/api/customers", body:payload)

        then:
        response.status == 403
    }

    def "update customer - add address"() {
        given:
        def token = fetchToken(username, password);
        def customer = databaseHelper.findCustomerByEmail(username)

        expect:
        customer != null
        customer.address == null
        token != null
        and:
        def payload = JsonOutput.toJson(DataHelper.addresssParams([
                street: "Auf der Neide",
                streetExtension: "",
                houseNumber: "14",
                postalCode: "53424",
                city: "Remagen",
                region: "Rheinland Pfalz",
                countryIso : "de",
                phoneNumber : "+4915763255325"

        ]))

        when:"set customer address"
        def response = requestHelper.put(port:port, token: token, path: "/api/customers/${customer.id}/address", body:payload)

        then:
        response.status == 200
        response.data.id == customer.id
        response.data.address.id != null
        response.data.address.street == "Auf der Neide"
        response.data.address.streetExtension == ""
        response.data.address.houseNumber == "14"
        response.data.address.postalCode == "53424"
        response.data.address.city == "Remagen"
        response.data.address.region == "Rheinland Pfalz"
        response.data.address.countryIso == "de"
        response.data.address.phoneNumber == "+4915763255325"

        and:
        def updatedCustomer = databaseHelper.findCustomerByEmail(username)
        updatedCustomer.address.id == response.data.address.id

        when: "update customer address addressId not available"
        response = requestHelper.put(port:port, token: token, path: "/api/customers/${customer.id}/address", body:JsonOutput.toJson(DataHelper.addresssParams([
                street: "Flossweg",
                houseNumber: "48",
                postalCode: "53179",
                city: "Bonn",
                region: "Nordrhein Westfalen",
                countryIso : "de",
                phoneNumber : "+49157632553458"]))
        )
        then:
        response.status == 200
        response.data.id == customer.id
        response.data.address.id == updatedCustomer.address.id

        and:
        def updatedCustomer2 = databaseHelper.findCustomerByEmail(username)
        updatedCustomer2.address.street == "Flossweg"
        updatedCustomer2.address.streetExtension == ""
        updatedCustomer2.address.houseNumber == "48"
        updatedCustomer2.address.postalCode == "53179"
        updatedCustomer2.address.city == "Bonn"
        updatedCustomer2.address.region == "Nordrhein Westfalen"
        updatedCustomer2.address.countryIso == "de"
        updatedCustomer2.address.phoneNumber == "+49157632553458"

        when: "update customer address addressId available"
        response = requestHelper.put(port:port, token: token, path: "/api/customers/${customer.id}/address", body:JsonOutput.toJson(DataHelper.addresssParams([
                id: updatedCustomer2.address.id,
                street: "Flossweg",
                houseNumber: "45",
                postalCode: "53179",
                city: "Bonn",
                countryIso : "de",
                region: "Nordrhein-Westfalen",
                phoneNumber : "+49157632553000"]))
        )
        then:
        response.status == 200
        response.data.id == customer.id
        response.data.address.id == updatedCustomer2.address.id

        and:
        def updatedCustomer3 = databaseHelper.findCustomerByEmail(username)
        updatedCustomer3.address.street == "Flossweg"
        updatedCustomer3.address.streetExtension == ""
        updatedCustomer3.address.houseNumber == "45"
        updatedCustomer3.address.postalCode == "53179"
        updatedCustomer3.address.city == "Bonn"
        updatedCustomer3.address.region == "Nordrhein-Westfalen"
        updatedCustomer3.address.countryIso == "de"
        updatedCustomer3.address.phoneNumber == "+49157632553000"

        when: "update customer address addressId not same as in customer"
        response = requestHelper.put(port:port, token: token, path: "/api/customers/${customer.id}/address", body:JsonOutput.toJson(DataHelper.addresssParams([id: 125])))
        then:
        response.status == 404

        and:"Address was not updated"
        def updatedCustomer4 = databaseHelper.findCustomerByEmail(username)
        updatedCustomer4.address.street == "Flossweg"
        updatedCustomer4.address.streetExtension == ""
        updatedCustomer4.address.houseNumber == "45"
        updatedCustomer4.address.postalCode == "53179"
        updatedCustomer4.address.city == "Bonn"
        updatedCustomer4.address.region == "Nordrhein-Westfalen"
        updatedCustomer4.address.countryIso == "de"
        updatedCustomer4.address.phoneNumber == "+49157632553000"

    }

    @Unroll
    def "customer filter works as expected : #title"() {
        given:
        databaseHelper.addAgentRoleToUser(username)
        def customer = databaseHelper.findCustomerByEmail(username)
        def token = fetchToken(username, password)

        and: "make sure a country for address exist"
        def countryDE = databaseHelper.createCountry("de", "Germany")
        def countryFR = databaseHelper.createCountry("fr", "France")

        expect:
        token != null
        countryDE != null
        countryFR != null
        customer != null

        and: "creation payload"
        def data = [
                [city:"Bonn",country:"de",email:"email1@email.com", firstname:"firstname1", lastname:"lastname1", language: "es"],
                [city:"Bonn",country:"de",email:"email2@email.com", firstname:"firstname1", lastname:"lastname2", language: "es"],
                [city:"Bonn",country:"de",email:"email3@email.com", firstname:"firstname1", lastname:"lastname3", language: "es"],
                [city:"Remagen",country:"de",email:"email4@email.com", firstname:"firstname2", lastname:"lastname4", language: "es"],
                [city:"Remagen",country:"de",email:"email5@email.com", firstname:"firstname2", lastname:"lastname5", language: "es"],
                [city:"Remagen",country:"de",email:"email6@email.com", firstname:"firstname2", lastname:"lastname6", language: "it"],
                [city:"Remagen",country:"de",email:"email7@email.com", firstname:"firstname2", lastname:"lastname7", language: "it"],
                [city:"paris",country:"fr",email:"email8@email.com", firstname:"firstname2", lastname:"lastname8", language: "en"],
                [city:"paris",country:"fr",email:"email9@email.com", firstname:"firstname2", lastname:"lastname9", language: "en"],
                [city:"paris",country:"fr",email:"email10@email.com", firstname:"firstname2", lastname:"lastname10", language: "en"],
                [city:"paris",country:"fr",email:"email11@email.com", firstname:"firstname2", lastname:"lastname11", language: "en"],
        ]
        data.eachWithIndex{ value, key ->
                def payload = JsonOutput.toJson([
                        contact: DataHelper.contactParams([email:value["email"], firstname: value["firstname"], lastname: value["lastname"], language: value["language"]]),
                        address: DataHelper.addresssParams([city:value["city"], countryIso: value["country"]]),
                        organisation:DataHelper.organisationParams([:]),
                        roles:["ROLE_CUSTOMER"],
                        partnerId:customer.customerNumber,
                        identityNumber: "123456987"
                ])
                def response = requestHelper.post(port:port, token: token, path: "/api/customers", body:payload)
                assert response.status == 201
        }

        when: "get first 10 customers"
        def response = requestHelper.post(port:port, token: token, path: "/api/customers/filter", body:JsonOutput.toJson(DataHelper.customerFilterParams(filter)))

        then:
        response.status == 200
        response.data.totalResultCount == totalResultCount
        response.data.resultCount == resultCount

        where:
        title                                                             | filter                                                           | totalResultCount | resultCount
        "first 10 customers with no filters"                              | [:]                                                              | 12               | 10
        "customer living in city bonn"                                    | [city: "Bonn"]                                                   | 3                | 3
        "customer living in country DE"                                   | [country: "de"]                                                  | 7                | 7
        "customer with given email: email3@email.com"                     | [email: "email3@email.com"]                                      | 1                | 1
        "customer with given firstname: firstname2"                       | [firstname: "firstname2"]                                        | 8                | 8
        "customer with given lastname: lastname10"                        | [lastname: "lastname10"]                                         | 1                | 1
        "customer city:paris, firstname:firstname2"                       | [city: "paris", firstname: "firstname2"]                         | 4                | 4
        "customer city:paris, firstname:firstname2, lastname: lastname11" | [firstname: "firstname2", city: "paris", lastname: "lastname11"] | 1                | 1
        "customer city:Bonn, firstname:firstname2, lastname: lastname11"  | [firstname: "firstname2", city: "Bonn", lastname: "lastname11"]  | 0                | 0
        "customer firstname like first"                                   | [firstname: "first"]                                             | 11               | 10
        "customer language: es"                                           | [language: "es"]                                                 | 5                | 5
        "customer language: ES"                                           | [language: "ES"]                                                 | 5                | 5
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
                firstname:"Peter",
                lastname: "The Great",
                email:"${username}",
                password:"${password}"
            ]
        ])
    }

}
