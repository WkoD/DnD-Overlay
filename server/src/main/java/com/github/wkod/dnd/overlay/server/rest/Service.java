package com.github.wkod.dnd.overlay.server.rest;

import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;

import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.configuration.ServerConfiguration;
import com.github.wkod.dnd.overlay.server.fx.ServerFx;

@org.springframework.stereotype.Service
public class Service {

    public List<OlScreen> getScreens() {
        return ServerFx.getScreens();
    }

    public Map<String, String> getConfiguration() {
        return ServerConfiguration.toMap(ServerConfiguration.class);
    }

    public void setConfiguration(Map<String, String> configuration) {
        ServerConfiguration.fromMap(configuration, ServerConfiguration.class);
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
