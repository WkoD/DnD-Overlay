package com.github.wkod.dnd.overlay.server;

import java.io.File;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.wkod.dnd.overlay.server.config.Configuration;
import com.github.wkod.dnd.overlay.server.fx.ServerFx;
import com.github.wkod.dnd.overlay.util.Utils;

import javafx.application.Application;
import javafx.application.Platform;

@SpringBootApplication
public class Server {

    public static void main(String[] args) throws IOException {

        File configuration = args.length > 0 ? new File(args[0]) : null;

        Configuration.load(configuration, Server.class.getClassLoader().getResourceAsStream("configuration.properties"),
                Configuration.class);

        // set log level
        Utils.setRootLogger(Configuration.LOGGER_LOCALE.get(), Configuration.LOGGER_LEVEL.get());

        Application.launch(ServerFx.class, args);
    }

    @PreDestroy
    public void onExit() {
        // close javafx
        Platform.exit();
    }
}
