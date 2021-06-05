package com.sicuro.service_tests.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.icegreen.greenmail.util.GreenMail
import com.sicuro.escrow.EscrowAppApplication
import com.sicuro.escrow.model.LinkType
import groovy.json.JsonOutput
import spock.lang.Unroll
import util.applicationcontext.TestContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Specification
import util.RequestHelper
import util.database.DatabaseHelper
import util.database.NeedsEmbeddedMysql
import util.mail.GreenMailServer
import util.mail.NeedGreenMail

@NeedsEmbeddedMysql
@NeedGreenMail
@SpringBootTest(classes = [EscrowAppApplication.class, TestContextConfiguration.class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AutControllerSpec extends Specification{

    @LocalServerPort
    private int port

    @Autowired
    private RequestHelper requestHelper

    @Autowired
    private DatabaseHelper databaseHelper

    @Autowired
    private ObjectMapper objectMapper

    private GreenMail greenMail

    def setup() {
        databaseHelper.cleanDatabase()
        GreenMailServer.instance().reset()
        greenMail = GreenMailServer.instance().getGreenMail()
    }

    def cleanup() {
        databaseHelper.cleanDatabase()
        GreenMailServer.instance().reset()
    }

    def "Signup - activation - login - complete signup - happy path" () {
        given:
        def username = "ngwaambe@hotmail.com"
        def password = "12elviscoNGWA"
        def payload = signupPayload(username, password)

        and: "make sure a country for address exist"
        def countryEntity = databaseHelper.createCountry("de", "Germany")

        expect:
        countryEntity != null

        when: "register account"
        def response =  requestHelper.post(
                 port: port,
                 path: "/api/auth/signup",
                 body: payload
         )

        then: "Signup was successful"
        response.status == 200

        and: "Activation email was sent"
        def mimeMessages = greenMail.getReceivedMessages()
        mimeMessages.size() == 1
        mimeMessages[0].subject == 'Please check your information and confirm your registration'
        greenMail.purgeEmailFromAllMailboxes()

        then: "get account activationId"
        def activationId = databaseHelper.findLinkId(username, LinkType.ACCOUNT_ACTIVATION)
        activationId != null

        when: "activate account"
        def activationResponse = requestHelper.get(port: port, path: "/api/auth/activate_account/${activationId}")

        then:"account is activated"
        activationResponse.status == 200

        when: "Signin"
        def signinResponse =  requestHelper.post(
                port: port,
                path: "/api/auth/token",
                body: JsonOutput.toJson([username:"${username}", password:"${password}"])
        )

        then: "Signin was successfull"
        signinResponse.status == 200
        signinResponse.data.access_token != null
        signinResponse.data.type == 'JWT'
        signinResponse.data.refresh_token != null

        when: "complete account signup"
        def completeResponse = requestHelper.put(
                port: port,
                path: "/api/auth/complete_signup/",
                token: signinResponse.data.access_token,
                body: completeSignupPayload()
        )

        then:
        completeResponse.status == 200

        when: "initiate reset password"
        def initiateResetPasswordResponse = requestHelper.put(
                port: port,
                path: "/api/auth/init_reset_password",
                body: JsonOutput.toJson([email:"ngwaambe@hotmail.com"])
        )

        then:
        initiateResetPasswordResponse.status == 200
        def resetpasswordId = databaseHelper.findLinkId(username, LinkType.RESET_PASSWORD)
        and:
        resetpasswordId != null

        when: "reset password"
        def resetPasswordResponse = requestHelper.get(
                port: port,
                path:"/api/auth/reset_password/${resetpasswordId}"
        )

        then:
        resetPasswordResponse.status == 200

    }

    @Unroll
    def "Signup with invalid payload fails #property" () {
        given:
        def payload = signupPayload(username, password, title, prefLang, firstname, lastname, organisation)

        when:
        def response =  requestHelper.post(
                port: port,
                path: "/api/auth/signup",
                body: payload
        )

        then: "Signup was successful"
        response.status == 400
        response.data.validationErrors[0].name == property

        where:
        property                 | username       | password   | title | prefLang | firstname | lastname | organisation
        'contact.email'          | null           | 'password' | 'Mr'  | 'en'     | 'Peter'   | 'Pan'    | [name: 'sicuro', taxNumber: '213415645']
        'contact.email'          | ''             | 'password' | 'Mr'  | 'en'     | 'Peter'   | 'Pan'    | [name: 'sicuro', taxNumber: '213415645']
        'contact.email'          | 'email'        | 'password' | 'Mr'  | 'en'     | 'Peter'   | 'Pan'    | [name: 'sicuro', taxNumber: '213415645']
        'contact.password'       | 'email@tx.com' | ''         | 'Mr'  | 'en'     | 'Peter'   | 'Pan'    | [name: 'sicuro', taxNumber: '213415645']
        'contact.password'       | 'email@tx.com' | null       | 'Mr'  | 'en'     | 'Peter'   | 'Pan'    | [name: 'sicuro', taxNumber: '213415645']
        'contact.password'       | 'email@tx.com' | 'ddd'       | 'Mr'  | 'en'     | 'Peter'   | 'Pan'    | [name: 'sicuro', taxNumber: '213415645']
        'contact.title'          | 'email@tx.com' | 'password' | ''    | 'en'     | 'Peter'   | 'Pan'    | [name: 'sicuro', taxNumber: '213415645']
        'contact.title'          | 'email@tx.com' | 'password' | null  | 'en'     | 'Peter'   | 'Pan'    | [name: 'sicuro', taxNumber: '213415645']
        'contact.language'       | 'email@tx.com' | 'password' | 'Mr'  | ''       | 'Peter'   | 'Pan'    | [name: 'sicuro', taxNumber: '213415645']
        'contact.language'       | 'email@tx.com' | 'password' | 'Mr'  | null     | 'Peter'   | 'Pan'    | [name: 'sicuro', taxNumber: '213415645']
        'contact.firstName'      | 'email@tx.com' | 'password' | 'Mr'  | 'en'     | ''        | 'Pan'    | [name: 'sicuro', taxNumber: '213415645']
        'contact.firstName'      | 'email@tx.com' | 'password' | 'Mr'  | 'en'     | null      | 'Pan'    | [name: 'sicuro', taxNumber: '213415645']
        'contact.lastName'       | 'email@tx.com' | 'password' | 'Mr'  | 'en'     | 'Peter'   | ''       | [name: 'sicuro', taxNumber: '213415645']
        'contact.lastName'       | 'email@tx.com' | 'password' | 'Mr'  | 'en'     | 'Peter'   | null     | [name: 'sicuro', taxNumber: '213415645']
        'organisation.name'      | 'email@tx.com' | 'password' | 'Mr'  | 'en'     | 'Peter'   | 'Pan'    | [name: '', taxNumber: '213415645']
        'organisation.name'      | 'email@tx.com' | 'password' | 'Mr'  | 'en'     | 'Peter'   | 'Pan'    | [name: null, taxNumber: '213415645']
        'organisation.name'      | 'email@tx.com' | 'password' | 'Mr'  | 'en'     | 'Peter'   | 'Pan'    | [taxNumber: '213415645']
        'organisation.taxNumber' | 'email@tx.com' | 'password' | 'Mr'  | 'en'     | 'Peter'   | 'Pan'    | [name: 'sicuro', taxNumber: '']
        'organisation.taxNumber' | 'email@tx.com' | 'password' | 'Mr'  | 'en'     | 'Peter'   | 'Pan'    | [name: 'sicuro', taxNumber: null]
        'organisation.taxNumber' | 'email@tx.com' | 'password' | 'Mr'  | 'en'     | 'Peter'   | 'Pan'    | [name: 'sicuro']
    }

    def "activate account with invalid activationID" () {
        when:
        when: "activate account"
        def response = requestHelper.get(port: port, path: "/api/auth/activate_account/abc")

        then:
        response.status == 404
    }

    private String signupPayload(
            username='ngwaambe@hotmail.com',
            password='12elviscoNGWA',
            title ='Mr',
            language='en',
            firstname='Peter',
            lastname='The great',
            organisation = [name:'sicuro', taxNumber: '213415645']) {

        def contact = (title != null ? [title: title] : [:]) +
                (language != null ? [language: language] : [:]) +
                (firstname != null ? [firstName: firstname] : [:]) +
                (lastname != null ? [lastName: lastname] : [:]) +
                (username != null ? [email: username] : [:]) +
                (password != null ? [password: password] : [:] )
        return JsonOutput.toJson(
        [
            organisation:organisation,
            contact: contact
        ])
    }

    private String completeSignupPayload() {
        return JsonOutput.toJson(
        [
            address: [
                    street: "Auf der Neide 14",
                    postalCode: "530179",
                    city: "Remagen",
                    countryIso: "de",
                    phoneNumber: "012457632132312"
            ],
            securityQuestion: [
                    question: "GRAND_MOTHERS_MAIDEN_NAME",
                    answer  : "angela"
            ]
        ])
    }

}
