package com.github.wkod.dnd.overlay.server.rest;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.wkod.dnd.overlay.api.OlData;
import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.exception.OlException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Controller {

    private final Service service;

    @GetMapping
    public List<OlScreen> getScreens() throws OlException {
        return service.getScreens();
    }

    @GetMapping("/configuration")
    public Map<String, String> getConfiguration() throws OlException {
        return service.getConfiguration();
    }

    @PostMapping(value = { "/configuration", "/configuration/save" })
    public void setConfiguration(@RequestBody(required = true) Map<String, String> configuration,
            HttpServletRequest request) throws OlException {

        service.setConfiguration(configuration);

        if (request.getServletPath().endsWith("save")) {
            service.saveConfiguration();
        }
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
