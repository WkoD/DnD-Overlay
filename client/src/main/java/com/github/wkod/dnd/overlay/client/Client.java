package com.github.wkod.dnd.overlay.client;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wkod.dnd.overlay.client.config.Configuration;
import com.github.wkod.dnd.overlay.client.fx.ClientFx;

import ch.qos.logback.classic.Level;

public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IOException {

        // load default configuration
        Configuration.load(new File(Client.class.getClassLoader().getResource("configuration.properties").getFile()));

        // load user configuration
        File configuration = new File("configuration.properties");

        if (configuration.exists()) {
            Configuration.load(configuration);
        } else {
            LOGGER.warn("No configuration file was found, internal fallback will be used");
        }

        // check configuration values
        try {
            Configuration.check();
        } catch (IllegalArgumentException | IllegalAccessException e) {
        }

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
