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
            log.info("========================================> Starting Mysql")
            TestEmbeddedMysql.instance().start()
            started = true
        }
    }

    @Override
    void stop() {
        if (started) {
            TestEmbeddedMysql.instance().stop()
        }
    }
}
