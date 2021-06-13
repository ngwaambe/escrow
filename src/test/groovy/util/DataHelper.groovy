package util

import com.sicuro.escrow.model.Title
import groovy.json.JsonOutput

class DataHelper {

   static Map contactParams(Map params) {
        def defaults = [
                email    : "email@email.com",
                title    : Title.Mr,
                language : "en",
                firstname: "firstname",
                lastname : "lastname",
                password : "password"
        ]
        return defaults + params
    }

    static Map addresssParams (Map params) {
        def defaults = [
                street : "Street",
                houseNumber : "10 A",
                streetExtension : "",
                postalCode : "53179",
                city : "Bonn",
                region : "NordRhein Westfallen",
                countryIso : "de",
                phoneNumber : "+4915763255325"
        ]

        return defaults + params
    }

    static Map organisationParams(Map params) {
        def defaults = [
                name: "sicuro Gmbh",
                taxNumber: "123342222"
        ]
        return defaults + params
    }

    static Map customerFilterParams(Map params) {
        def defaults = [
                offset   : 0,
                limit    : 10,
                sortField: null,
                email    : null,
                lastname : null,
                country  : null,
                status   : null
        ]

        return defaults + params
    }

    static Map customerDetailParams(Map params) {
        def defaults = [
                title    : "Mr",
                language : "en",
                firstname: "Peter",
                lastname : "The Great",
                organisation : [name: "sicuro", taxNumber: "113213213"]
        ]
        return defaults + params
    }
}
