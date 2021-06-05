package util.database

import groovy.util.logging.Slf4j
import org.spockframework.runtime.extension.IAnnotationDrivenExtension
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo

@Slf4j
class EmbeddedMysqlExtension implements IAnnotationDrivenExtension<NeedsEmbeddedMysql> , IGlobalExtension{

    static boolean started = false

    @Override
    void start() {
    }

    void visitSpecAnnotation(NeedsEmbeddedMysql annotation, SpecInfo spec) {
        if (!started) {
            log.info("Start ========================================> Starting Mysql")
            TestEmbeddedMysql.instance().start()
            started = true
            log.info("Start ========================================> Started Mysql")
        }
    }

    @Override
    void stop() {
        if (started) {
            log.info("Stop ========================================> Stopping Mysql")
            TestEmbeddedMysql.instance().stop()
            started = false
            log.info("Stop ========================================> Stopped Mysql")
        }
    }
}
