package util.applicationcontext

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import util.RequestHelper
import util.database.DatabaseHelper

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

@Configuration
class TestContextConfiguration {

    @Bean
    DatabaseHelper databaseHelper() {
        return new DatabaseHelper()
    }

    @Bean
    RequestHelper requestHelper() {
        return new RequestHelper()
    }



    @Bean
    Clock clock() {
        return Clock.fixed(Instant.now(), ZoneId.systemDefault())
    }
}
