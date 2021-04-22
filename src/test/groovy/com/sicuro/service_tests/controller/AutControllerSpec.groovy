package com.sicuro.service_tests.controller

import com.sicuro.escrow.EscrowAppApplication
import groovy.json.JsonOutput
import util.applicationcontext.TestContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Specification
import util.RequestHelper
import util.database.DatabaseHelper
import util.database.NeedsMysql

@NeedsMysql
@SpringBootTest(classes = [EscrowAppApplication.class, TestContextConfiguration.class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AutControllerSpec extends Specification{

    @LocalServerPort
    private int port

    @Autowired
    private RequestHelper requestHelper

    @Autowired
    private DatabaseHelper databaseHelper

    def setup() {
        databaseHelper.cleanDatabase()
    }

    def "Signup - successful" () {
        given:
         def payload = signupPayload()
        expect:
        payload != null

        when:
        def response =  requestHelper.post(
                 port: port,
                 path: "/api/auth/signup",
                 body: payload
         )

        then:
        response.status == 200
    }

    private String signupPayload() {
        return JsonOutput.toJson(
        [
            organisation:[
                name:"sicuro",
                taxNumber:"213415645"
            ],
            contact: [
                title:"Mr",
                preferredLanguage:"en",
                firstName:"Peter",
                lastName: "The Great",
                email:"ngwaambe@hotmail.com",
                password:"12elviscoNGWA"
            ]
        ])
    }
}
