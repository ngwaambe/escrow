package util.database

import groovy.util.logging.Slf4j
import org.testcontainers.containers.MySQLContainer
import org.apache.commons.compress.*

@Slf4j
class TestMysqlContainer {

    private static instance = null

    private MySQLContainer container

    private boolean started = false

    static synchronized TestMysqlContainer instance() {
        if (instance == null) {
            instance = new TestMysqlContainer()
        }
        return instance
    }

    void start() {
        synchronized (this) {
            if (!started) {
                container = new MySQLContainer("mysql:8.0.22")
                    .withDatabaseName("sicuro")
                    .withUsername("test")
                    .withPassword("test")
                    .withInitScript("db/1_create_schema.sql")

                container.start()

                System.setProperty('spring.datasource.url', container.getJdbcUrl())
                System.setProperty('spring.datasource.password', container.getPassword())
                System.setProperty('spring.datasource.username', container.getUsername())
                started = true
            }
        }
    }

    void stop() {
        synchronized (this) {
            container.close()
            started = false
        }
    }
}
