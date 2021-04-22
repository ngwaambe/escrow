package util.database

import groovy.util.logging.Slf4j
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo

@Slf4j
class MysqlExtension extends AbstractAnnotationDrivenExtension<NeedsMysql> implements IGlobalExtension{

    static boolean started = false

    @Override
    void start() {
    }

    void visitSpecAnnotation(NeedsMysql annotation, SpecInfo spec) {
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
