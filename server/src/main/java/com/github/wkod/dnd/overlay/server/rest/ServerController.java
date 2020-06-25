package com.github.wkod.dnd.overlay.server.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.wkod.dnd.overlay.api.OlData;
import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.api.exception.OlException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServerController {

    private final ServerService service;

    @GetMapping
    public List<OlScreen> getScreens() throws OlException {
        return service.getScreens();
    }

    @PostMapping("/background")
    public void setBackground(@RequestBody(required = true) OlData data) throws OlException {
        service.setBackground(data.getScreenid(), data.getName(), data.getData());
    }

    @PostMapping("/background/toggle")
    public void toggleBackground(@RequestBody(required = true) Integer screenid) throws OlException {
        service.toggleBackground(screenid);
    }

    @PostMapping("/background/clear")
    public void clearBackground(@RequestBody(required = true) Integer screenid) throws OlException {
        service.clearBackground(screenid);
    }

    @PostMapping("/image")
    public void setImage(@RequestBody(required = true) OlData data) throws OlException {
        service.setImage(data.getScreenid(), data.getName(), data.getData());
    }

    @PostMapping("/image/toggle")
    public void toggleImage(@RequestBody(required = true) Integer screenid) throws OlException {
        service.toggleImage(screenid);
    }

    @PostMapping("/image/clear")
    public void clearImage(@RequestBody(required = true) Integer screenid) throws OlException {
        service.clearImage(screenid);
    }
}
