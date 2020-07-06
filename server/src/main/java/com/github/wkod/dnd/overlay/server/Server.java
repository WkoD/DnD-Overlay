package com.github.wkod.dnd.overlay.server;

import java.io.File;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.wkod.dnd.overlay.configuration.ServerConfiguration;
import com.github.wkod.dnd.overlay.server.fx.ServerFx;
import com.github.wkod.dnd.overlay.util.LogUtils;

import javafx.application.Application;
import javafx.application.Platform;

@SpringBootApplication
public class Server {

    public static void main(String[] args) throws IOException {

        File configuration = args.length > 0 ? new File(args[0]) : new File("configuration.properties");

        ServerConfiguration.load(configuration,
                Server.class.getClassLoader().getResourceAsStream("configuration.properties"),
                ServerConfiguration.class);

        // set log level
        LogUtils.setRootLogger(ServerConfiguration.LOGGER_LOCALE.get(), ServerConfiguration.LOGGER_LEVEL.get());

        Application.launch(ServerFx.class, args);
    }

    @PreDestroy
    public void onExit() {
        // close javafx
        Platform.exit();
    }
}
