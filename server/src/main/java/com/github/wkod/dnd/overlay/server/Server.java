package com.github.wkod.dnd.overlay.server;

import java.io.File;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.wkod.dnd.overlay.server.config.Configuration;
import com.github.wkod.dnd.overlay.server.fx.ServerFx;

import ch.qos.logback.classic.Level;
import javafx.application.Application;
import javafx.application.Platform;

@SpringBootApplication
public class Server {

    public static void main(String[] args) throws IOException {

        File configuration = args.length > 0 ? new File(args[0]) : null;

        Configuration.load(configuration, Server.class.getClassLoader().getResourceAsStream("configuration.properties"),
                Configuration.class);

        // set log level
        setLogLevel(Configuration.LOGGER_LEVEL.get());

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
