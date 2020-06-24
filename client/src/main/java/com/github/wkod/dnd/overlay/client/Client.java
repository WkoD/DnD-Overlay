package com.github.wkod.dnd.overlay.client;

import java.io.File;
import java.io.IOException;

import com.github.wkod.dnd.overlay.client.config.Configuration;
import com.github.wkod.dnd.overlay.client.fx.ClientFx;

public class Client {

    public static void main(String[] args) throws IOException {
        Configuration.load(new File("configuration.properties"));
        ClientFx.runthis(args);
    }

}
