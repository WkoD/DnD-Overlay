package com.github.wkod.dnd.overlay.server;

import java.io.File;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.wkod.dnd.overlay.server.config.Configuration;
import com.github.wkod.dnd.overlay.server.fx.ServerFx;

import ch.qos.logback.classic.Level;
import javafx.application.Application;
import javafx.application.Platform;

@SpringBootApplication
public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws IOException {

        // load default configuration
        Configuration.load(new File(Server.class.getClassLoader().getResource("configuration.properties").getFile()));

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

        Configuration.load(configuration);
        Application.launch(ServerFx.class, args);
    }

    private static void setLogLevel(String level) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.toLevel(level, logger.getLevel()));
    }

    @PreDestroy
    public void onExit() {
        // close javafx
        Platform.exit();
    }
}
