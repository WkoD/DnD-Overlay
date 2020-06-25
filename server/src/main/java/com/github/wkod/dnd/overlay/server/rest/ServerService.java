package com.github.wkod.dnd.overlay.server.rest;

import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.server.fx.ServerFx;

@Service
public class ServerService {

    public List<OlScreen> getScreens() {
        return ServerFx.getScreens();
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
