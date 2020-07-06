package com.github.wkod.dnd.overlay.client.rest;

import static com.github.wkod.dnd.overlay.localization.Messages.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.cal10n.LocLogger;

import com.github.wkod.dnd.overlay.api.OlData;
import com.github.wkod.dnd.overlay.api.OlDataType;
import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.configuration.ClientConfiguration;
import com.github.wkod.dnd.overlay.util.LogUtils;

public class Sender {

    private static final LocLogger LOGGER = LogUtils.getLogger(Sender.class);

    public static List<OlScreen> getScreens() {
        try {
            Client client = getClient();
            WebTarget target = client.target(getTarget());

            return target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<OlScreen>>() {
                    });
        } catch (Exception e) {
            LOGGER.error(CLIENT_DATA_TRANSFER_ERROR, e);
        }

        return new ArrayList<>();
    }

    public static void setImageData(int screenid, String name, byte[] data, boolean background) {
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
            LOGGER.error(CLIENT_DATA_TRANSFER_ERROR, e);
        }
    }

    public static void toggleImageData(int screenid, boolean background) {
        try {
            Client client = getClient();
            WebTarget target = client.target(getTarget() + (background ? "/background" : "/image") + "/toggle");

            target.request(MediaType.APPLICATION_JSON).post(Entity.json(screenid), Integer.class);
        } catch (Exception e) {
            LOGGER.error(CLIENT_DATA_TRANSFER_ERROR, e);
        }
    }

    public static void clearImageData(int screenid, boolean background) {
        try {
            Client client = getClient();
            WebTarget target = client.target(getTarget() + (background ? "/background" : "/image") + "/clear");

            target.request(MediaType.APPLICATION_JSON).post(Entity.json(screenid), Integer.class);
        } catch (Exception e) {
            LOGGER.error(CLIENT_DATA_TRANSFER_ERROR, e);
        }
    }

    private static Client getClient() {
        ClientConfig config = new ClientConfig();
        return ClientBuilder.newClient(config);
    }

    private static String getTarget() {
        return "http://" + ClientConfiguration.SERVER_HOST.get() + ":" + ClientConfiguration.SERVER_PORT.get()
                + ClientConfiguration.SERVER_SERVLET_CONTEXT_PATH.get();
    }
}
