package com.sicuro.service_tests

import com.sicuro.escrow.EscrowAppApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import util.applicationcontext.TestContextConfiguration
import util.database.NeedsMysql

@NeedsMysql
@ContextConfiguration
@SpringBootTest(classes = [EscrowAppApplication.class, TestContextConfiguration.class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationInitSpec extends Specification{

    def "Overall app context init integrity is OK"() {
        expect:
        true == true
    }
}
