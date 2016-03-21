package com.clearcapital.oss.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;

import com.clearcapital.oss.java.CastHelpers;

public class Slf4jHelpers {

    protected static final Logger log = LoggerFactory.getLogger(Slf4jHelpers.class);

    /**
     * Stops slf4j logger during shutdown. Useful if you want logging to show up in the face of {@link System#exit(int)}
     */
    protected static Thread shutdownThread = new Thread() {

        @Override
        public void run() {
            ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
            if (loggerFactory instanceof LoggerContext) {
                log.debug("Shutting down log manager.");
                LoggerContext context = (LoggerContext) loggerFactory;
                context.stop();
            } else {
                log.debug("Not sure how to shut down log manager when the manager isn't logback. Sorry!");
            }
        }
    };

    /**
     * Useful for setting logger level after it has been configured.
     * 
     * @param Name
     * @return
     */
    public static ch.qos.logback.classic.Logger getLogbackLogger(String Name) {
        org.slf4j.Logger slf4jRoot = LoggerFactory.getLogger(Name);
        return CastHelpers.requireCast(slf4jRoot, ch.qos.logback.classic.Logger.class);
    }

    /**
     * Tell Java Runtime to call {@link Slf4jHelpers#shutdownThread} during shutdown.
     */
    public static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

}
