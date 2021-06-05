package util.mail

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
import groovy.util.logging.Slf4j

@Slf4j
class GreenMailServer {

    private static instance = null
    private GreenMail greenMail
    private boolean started = false

    static synchronized  GreenMailServer instance() {
        if (instance == null) {
            instance = new GreenMailServer()
        }
        return instance
    }

    void start() {
        synchronized (this) {
            if (!started) {
                log.debug("Start Greenmail========================================> Starting Greenmail")
                ServerSetup serverSetup = new ServerSetup(2530, null,"smtp")
                serverSetup.setServerStartupTimeout(20000)
                serverSetup.setReadTimeout(5000)
                serverSetup.setWriteTimeout(5000)
                serverSetup.setVerbose(true)
                greenMail = new GreenMail(serverSetup)
                greenMail.setUser("noreply@sicuro.com", "S2ic013uro")
                greenMail.start()
                started = true
                log.debug("Start Greenmail========================================> Started Greenmail")
            }
        }
    }

    void stop() {
        synchronized (this) {
            log.debug("Stop Greenmail========================================> Stopping Greenmail")
            greenMail.stop()
            started = false
            log.debug("Stop Greenmail========================================> Stopped Greenmail ")
        }
    }

    void reset() {
        synchronized (this) {
            stop()
            start()
        }
    }

    public GreenMail getGreenMail() {
        return greenMail
    }

}
