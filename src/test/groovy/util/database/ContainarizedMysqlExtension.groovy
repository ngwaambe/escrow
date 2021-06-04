package util.database

import groovy.util.logging.Slf4j
import org.spockframework.runtime.extension.IAnnotationDrivenExtension
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo

@Slf4j
class ContainarizedMysqlExtension implements IAnnotationDrivenExtension<NeedsContainarizedMysql> , IGlobalExtension{

    static boolean started = false

    @Override
    void start() {
    }

    void visitSpecAnnotation(NeedsContainarizedMysql annotation, SpecInfo spec) {
        if (!started) {
            log.info("========================================> Starting Mysql")
            TestMysqlContainer.instance().start()
            started = true
        }
    }

    @Override
    void stop() {
        if (started) {
            TestMysqlContainer.instance().stop()
        }
    }
}
