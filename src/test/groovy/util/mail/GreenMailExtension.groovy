package util.mail

import com.icegreen.greenmail.util.GreenMail
import groovy.util.logging.Slf4j
import org.spockframework.runtime.extension.IAnnotationDrivenExtension
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo

@Slf4j
class GreenMailExtension implements IAnnotationDrivenExtension<NeedGreenMail>, IGlobalExtension{
    static boolean started = false
    private GreenMail greenMail

    @Override
    void start() {

    }

    void visitSpecAnnotation(NeedGreenMail annotation, SpecInfo spec) {
        synchronized (this) {
            if (!started) {
                GreenMailServer.instance().start()
                started = true
            }
        }
    }

    @Override
    void stop() {
        synchronized (this) {
            if(started) {
                GreenMailServer.instance().stop()
            }
        }
    }
}
