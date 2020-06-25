package com.github.wkod.dnd.overlay.client;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wkod.dnd.overlay.client.config.Configuration;
import com.github.wkod.dnd.overlay.client.fx.ClientFx;

public class Client {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IOException {
        
        File configuration = new File("configuration.properties");
        
        if (!configuration.exists()) {
            LOGGER.warn("No configuration file was found, internal fallback will be used");
            
            configuration = new File(Client.class.getClassLoader().getResource("configuration.properties").getFile());
        }

        Configuration.load(configuration);
        ClientFx.runthis(args);
    }

}
