package com.github.wkod.dnd.overlay.util;

import java.util.Locale;

import org.slf4j.LoggerFactory;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import lombok.Getter;

public final class LogUtils {
    
    /**
     * Supported log level by Logback.
     */
    public enum Level {
        ALL(ch.qos.logback.classic.Level.ALL),
        DEBUG(ch.qos.logback.classic.Level.DEBUG),
        ERROR(ch.qos.logback.classic.Level.ERROR),
        INFO(ch.qos.logback.classic.Level.INFO),
        OFF(ch.qos.logback.classic.Level.OFF),
        TRACE(ch.qos.logback.classic.Level.TRACE),
        WARN(ch.qos.logback.classic.Level.WARN);
        
        @Getter
        private final ch.qos.logback.classic.Level level;
        
        private Level(ch.qos.logback.classic.Level level) {
            this.level = level;
        }
    }

    /**
     * Private constructor.
     */
    private LogUtils() {
    }
    
    /**
     * Creates a new localizable logger for the given class.
     * 
     * @param clazz Class<?>
     * @return LocLogger
     */
    public static LocLogger getLogger(Class<?> clazz) {
        IMessageConveyor conveyor = new MessageConveyor(Locale.getDefault());
        LocLoggerFactory factory = new LocLoggerFactory(conveyor);
        return factory.getLocLogger(clazz);
    }

    /**
     * Set log level of root logger.
     * 
     * @param level String
     */
    public static void setRootLogger(Locale locale, Level level) {
        Locale.setDefault(locale);
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(level.level);
    }
}
