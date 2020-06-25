package com.github.wkod.dnd.overlay.server;

import java.io.File;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.wkod.dnd.overlay.server.config.Configuration;
import com.github.wkod.dnd.overlay.server.fx.ServerFx;

import javafx.application.Application;
import javafx.application.Platform;

@SpringBootApplication
public class Server {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws IOException {
        
        File configuration = new File("configuration.properties");
        
        if (!configuration.exists()) {
            LOGGER.warn("No configuration file was found, internal fallback will be used");
            
            configuration = new File(Server.class.getClassLoader().getResource("configuration.properties").getFile());
        }
        
        Configuration.load(configuration);
        Application.launch(ServerFx.class, args);
    }

    @PreDestroy
    public void onExit() {
        // close javafx
        Platform.exit();
    }
}
