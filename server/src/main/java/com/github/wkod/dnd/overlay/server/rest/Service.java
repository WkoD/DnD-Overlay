package com.github.wkod.dnd.overlay.server.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.tomcat.util.codec.binary.Base64;

import com.github.wkod.dnd.overlay.api.OlConfiguration;
import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.configuration.ConfigurationParameter;
import com.github.wkod.dnd.overlay.configuration.ServerConfiguration;
import com.github.wkod.dnd.overlay.server.fx.ServerFx;

@org.springframework.stereotype.Service
public class Service {

    public List<OlScreen> getScreens() {
        return ServerFx.getScreens();
    }

    public List<OlConfiguration> getConfiguration() {
        List<ConfigurationParameter<?>> values = ServerConfiguration.values(ServerConfiguration.class);
        List<OlConfiguration> configuration = new ArrayList<>();

        for (ConfigurationParameter<?> parameter : values) {
            OlConfiguration olc = new OlConfiguration();
            olc.setName(parameter.name());
            olc.setValue(parameter.toString());
            configuration.add(olc);
        }

        return configuration;
    }

    public void setConfiguration(List<OlConfiguration> configuration) {
        Properties properties = new Properties();

        for (OlConfiguration parameter : configuration) {
            properties.put(parameter.getName(), parameter.getValue());
        }

        ServerConfiguration.load(properties, ServerConfiguration.class);
    }

    public void saveConfiguration() {
        ServerConfiguration.save(ServerConfiguration.class);
    }

    public void setBackground(Integer screenid, String name, String data) {
        ServerFx.setImage(screenid, name, data != null ? Base64.decodeBase64(data) : null, true);
    }

    public void toggleBackground(Integer screenid) {
        ServerFx.toggleScreen(screenid, true);
    }

    public void clearBackground(Integer screenid) {
        ServerFx.clearScreen(screenid, true);
    }

    public void setImage(Integer screenid, String name, String data) {
        ServerFx.setImage(screenid, name, data != null ? Base64.decodeBase64(data) : null, false);
    }

    public void toggleImage(Integer screenid) {
        ServerFx.toggleScreen(screenid, false);
    }

    public void clearImage(Integer screenid) {
        ServerFx.clearScreen(screenid, false);
    }
}
