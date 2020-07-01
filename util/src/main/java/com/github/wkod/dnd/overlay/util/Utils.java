package com.github.wkod.dnd.overlay.util;

import java.util.Locale;

import org.slf4j.LoggerFactory;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import ch.qos.logback.classic.Level;

public final class Utils {

    /**
     * Private constructor.
     */
    private Utils() {
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
    public static void setRootLogger(Locale locale, String level) {
        Locale.setDefault(locale);
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.toLevel(level, logger.getLevel()));
    }
}
