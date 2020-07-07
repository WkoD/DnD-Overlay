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
        
        File configuration = new File(getConfigurationFileName(args));

        ServerConfiguration.load(configuration,
                Server.class.getClassLoader().getResourceAsStream("configuration.properties"),
                ServerConfiguration.class);

        // set log level
        LogUtils.setRootLogger(ServerConfiguration.LOGGER_LOCALE.get(), ServerConfiguration.LOGGER_LEVEL.get());

        Application.launch(ServerFx.class, args);
    }

    private static String getConfigurationFileName(String[] args) {
        for (String arg : args) {
            // ignore spring arguments
            if (arg.startsWith("-")) {
                continue;
            }

            return arg;
        }

        return "configuration.properties";
    }

    @PreDestroy
    public void onExit() {
        // close javafx
        Platform.exit();
    }
}
