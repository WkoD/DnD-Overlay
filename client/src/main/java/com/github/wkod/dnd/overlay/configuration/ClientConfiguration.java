package com.github.wkod.dnd.overlay.configuration;

import java.util.Locale;

public final class ClientConfiguration extends ServerConfiguration {

    public static final ConfigurationParameter<String> LOGGER_LEVEL = ServerConfiguration.LOGGER_LEVEL;
    public static final ConfigurationParameter<Locale> LOGGER_LOCALE = ServerConfiguration.LOGGER_LOCALE;

    public static final ConfigurationParameter<String> SERVER_HOST = new ConfigurationParameter<>("server.host",
            String.class);
    public static final ConfigurationParameter<Integer> SERVER_PORT = ServerConfiguration.SERVER_PORT;
    public static final ConfigurationParameter<String> SERVER_SERVLET_CONTEXT_PATH = ServerConfiguration.SERVER_SERVLET_CONTEXT_PATH;

    public static final ConfigurationParameter<Boolean> UPDATE_SCREEN_STARTUP = new ConfigurationParameter<>(
            "update.screen.startup", Boolean.class);
}
