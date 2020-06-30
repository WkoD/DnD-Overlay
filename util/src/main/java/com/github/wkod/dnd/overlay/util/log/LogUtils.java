package com.github.wkod.dnd.overlay.util.log;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

public final class LogUtils {

    /**
     * Private constructor.
     */
    private LogUtils() {
    }

    /**
     * Set log level of root logger.
     * 
     * @param level String
     */
    public static void setLogLevel(String level) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.toLevel(level, logger.getLevel()));
    }
}
