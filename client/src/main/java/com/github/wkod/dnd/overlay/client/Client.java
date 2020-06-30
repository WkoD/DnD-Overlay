package com.github.wkod.dnd.overlay.client;

import java.io.File;

import org.slf4j.LoggerFactory;

import com.github.wkod.dnd.overlay.client.config.Configuration;
import com.github.wkod.dnd.overlay.client.fx.ClientFx;

import ch.qos.logback.classic.Level;

public class Client {

    public static void main(String[] args) {

        File configuration = args.length > 0 ? new File(args[0]) : null;

        Configuration.load(configuration, Client.class.getClassLoader().getResourceAsStream("configuration.properties"),
                Configuration.class);

        // set log level
        setLogLevel(Configuration.LOGGER_LEVEL.get());

        ClientFx.runthis(args);
    }

    private static void setLogLevel(String level) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.toLevel(level, logger.getLevel()));
    }
}
