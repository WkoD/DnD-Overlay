package com.github.wkod.dnd.overlay.configuration;

import java.util.Locale;

import com.github.wkod.dnd.overlay.util.LogUtils.Level;

public final class ClientConfiguration extends Configuration {

    public static final ConfigurationParameter<Level> LOGGER_LEVEL = ServerConfiguration.LOGGER_LEVEL.clone();
    public static final ConfigurationParameter<Locale> LOGGER_LOCALE = ServerConfiguration.LOGGER_LOCALE.clone();

    public static final ConfigurationParameter<String> SERVER_HOST = new ConfigurationParameter<>("server.host",
            String.class);
    public static final ConfigurationParameter<Integer> SERVER_PORT = ServerConfiguration.SERVER_PORT.clone();
    public static final ConfigurationParameter<String> SERVER_SERVLET_CONTEXT_PATH = ServerConfiguration.SERVER_SERVLET_CONTEXT_PATH
            .clone();

    public static final ConfigurationParameter<Boolean> UPDATE_SCREEN_STARTUP = new ConfigurationParameter<>(
            "update.screen.startup", Boolean.class);
}
