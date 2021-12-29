package com.sicuro.escrow.model

import com.sicuro.escrow.config.JacksonConfiguration
import groovy.json.JsonOutput
import spock.lang.Specification

class JsonDeserialzer extends Specification{


    def "PaybackAccount"() {
        given:
        def json = JsonOutput.toJson([
            'id':null,
            'owner':'elvis',
            'paypalAccount': 'email@email.com',
            'paymentType': 'PAYPAL',
            '@type': 'PaypalAccount']
        )
        def wrapper =  new JacksonConfiguration().objectMapper()

        when:
        def account = wrapper.writeValueAsString(new PaypalAccount(null, "Elvis", "email@email"))

        then:
        account != null

        and:
        println(account)
        println(json)

        when:
        def v = wrapper.readerFor(PaymentAccount.class).readValue(account)

        then:
         v != null

    }
}
