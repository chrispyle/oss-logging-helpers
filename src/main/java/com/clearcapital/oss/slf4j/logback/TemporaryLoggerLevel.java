package com.clearcapital.oss.slf4j.logback;

import com.clearcapital.oss.slf4j.Slf4jHelpers;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * Temporarily change the log level for a particular logger. This class is NOT threadsafe. The reason it is not
 * threadsafe is that changing the log level will affect logging from other threads, at least for the same logger.
 * Further, TemporaryLogLevel takes no special precautions to determine if it is trampling the results of other
 * TemporaryLogLevel objects. So you could have a situation where thread 'a' changes a logger's level from null to
 * debug, then thread 'b' changes from debug to warn, then thread 'a' changes it from warn to null, and finally thread
 * 'b' changes from null to debug, leaving the logger in a different state from when the TemporaryLogLevel objects began
 * changing it.
 * 
 * A better approach, if it ever becomes necessary (I'm writing this to make a single-threaded JUnit test easier to
 * debug), would be to have TemporaryLogLevel use a shared Map<String,Stack<Level>> for determining what the "original"
 * value is. You would still have a situation where two TemporaryLogLevel objects could have an effectively
 * indeterminate state while they're "open," but at least they would be guaranteed to leave the logger the way it was
 * when they all close.
 */
public class TemporaryLoggerLevel implements AutoCloseable {

    private final Logger managedLog;
    private final Level originalLevel;

    public TemporaryLoggerLevel(Level level, String loggerName) {
        managedLog = Slf4jHelpers.getLogbackLogger(loggerName);
        originalLevel = managedLog.getLevel();
        managedLog.setLevel(level);
    }

    @Override
    public void close() throws Exception {
        managedLog.setLevel(originalLevel);
    }

}
