package util.database

import com.wix.mysql.EmbeddedMysql
import com.wix.mysql.config.MysqldConfig
import groovy.util.logging.Slf4j
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.distribution.Version.v8_0_17;
import static com.wix.mysql.config.Charset.UTF8;
import java.util.concurrent.TimeUnit;

@Slf4j
class TestEmbeddedMysql {

    private static instance = null

    private EmbeddedMysql embeddedMysql

    private boolean started = false

    static synchronized TestEmbeddedMysql instance() {
        if (instance == null) {
            instance = new TestEmbeddedMysql()
        }
        return instance
    }

    void start() {
        synchronized (this) {
            if (!started) {
                MysqldConfig config = aMysqldConfig(v8_0_17)
                        .withCharset(UTF8)
                        .withPort(2215)
                        .withUser("test", "test")
                        .withTimeZone("Europe/Berlin")
                        .withTimeout(2, TimeUnit.MINUTES)
                        .withServerVariable("max_connect_errors", 666)
                        .build()

                embeddedMysql =  anEmbeddedMysql(config)
                        .addSchema("sicuro", classPathScript("db/1_create_schema.sql"))
                        .start()

                log.info("jdbc:mysql://localhost:${config.port}/sicuro?useSSL=false")
                System.setProperty('spring.datasource.url', "jdbc:mysql://localhost:${config.port}/sicuro?useSSL=false")
                System.setProperty('spring.datasource.password', config.getPassword())
                System.setProperty('spring.datasource.username', config.getUsername())
                started = true
            }
        }
    }

    void stop() {
        synchronized (this) {
            embeddedMysql.close()
            started = false
        }
    }
}
