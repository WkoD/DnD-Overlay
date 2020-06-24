package com.github.wkod.dnd.overlay.client.rest;

import java.util.Base64;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;

import com.github.wkod.dnd.overlay.api.OlData;
import com.github.wkod.dnd.overlay.api.OlDataType;
import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.api.exception.OlException;
import com.github.wkod.dnd.overlay.client.config.Configuration;

public class Sender {

    public static List<OlScreen> getScreens() throws OlException {
        try {
            Client client = getClient();
            WebTarget target = client.target(getTarget());
            
            return target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get(new GenericType<List<OlScreen>>() {});
        } catch (Exception e) {
            throw new OlException(e);
        }
    }
    
    public static void setImageData(int screenid, String name, byte[] data, boolean background) throws OlException {
        try {
            OlData ol = new OlData();
            ol.setName(name);
            ol.setType(OlDataType.IMAGE);
            ol.setData(data != null ? Base64.getEncoder().encodeToString(data) : null);
            ol.setScreenid(screenid);
            
            Client client = getClient();
            WebTarget target = client.target(getTarget() + (background ? "/background" : "/image"));
            
            target.request(MediaType.APPLICATION_JSON).post(Entity.json(ol), OlData.class);
        } catch (Exception e) {
            throw new OlException(e);
        }
    }
    
    public static void toggleImageData(int screenid, boolean background) throws OlException {
        try {
            Client client = getClient();
            WebTarget target = client.target(getTarget() + (background ? "/background" : "/image") + "/toggle");
            
            target.request(MediaType.APPLICATION_JSON).post(Entity.json(screenid), Integer.class);
        } catch (Exception e) {
            throw new OlException(e);
        }
    }
    
    public static void clearImageData(int screenid, boolean background) throws OlException {
        try {
            Client client = getClient();
            WebTarget target = client.target(getTarget() + (background ? "/background" : "/image") + "/clear");
            
            target.request(MediaType.APPLICATION_JSON).post(Entity.json(screenid), Integer.class);
        } catch (Exception e) {
            throw new OlException(e);
        }
    }

    private static Client getClient() {
        ClientConfig config = new ClientConfig();
        return ClientBuilder.newClient(config);
    }
    
    private static String getTarget() {
        return "http://" + Configuration.HOST.get() + ":" + Configuration.PORT.get() + "/overlay";
    }
}
