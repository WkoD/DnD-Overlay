package com.github.wkod.dnd.overlay.client;

import java.io.File;

import com.github.wkod.dnd.overlay.client.fx.ClientFx;
import com.github.wkod.dnd.overlay.configuration.ClientConfiguration;
import com.github.wkod.dnd.overlay.util.Utils;

public class Client {

    public static void main(String[] args) {

        File configuration = args.length > 0 ? new File(args[0]) : new File("configuration.properties");

        ClientConfiguration.load(configuration,
                Client.class.getClassLoader().getResourceAsStream("configuration.properties"),
                ClientConfiguration.class);

        // set log level
        Utils.setRootLogger(ClientConfiguration.LOGGER_LOCALE.get(), ClientConfiguration.LOGGER_LEVEL.get());

        ClientFx.runthis(args);
    }
}
