package com.github.wkod.dnd.overlay.client.config;

import com.github.wkod.dnd.overlay.util.config.ConfigurationBase;
import com.github.wkod.dnd.overlay.util.config.ConfigurationValidator;

public class Configuration<T> extends ConfigurationBase<T> {

    public static final Configuration<String> SERVER_HOST = new Configuration<>("server.host", String.class);
    public static final Configuration<String> SERVER_PORT = new Configuration<>("server.port", String.class);
    public static final Configuration<String> SERVER_SERVLET_CONTEXT_PATH = new Configuration<>(
            "server.servlet.context-path", String.class);
    
    public static final Configuration<String> LOGGER_LEVEL = new Configuration<>("logger.level", String.class);
    
    public static final Configuration<Boolean> STARTUP_UPDATE_SCREEN = new Configuration<>("startup.update.screen",
            Boolean.class);

    private Configuration(String name, Class<?> clazz) {
        super(name, clazz);
    }

    private Configuration(String name, Class<?> clazz, ConfigurationValidator<T> validator) {
        super(name, clazz, validator);
    }
    
    public static void check() throws IllegalArgumentException, IllegalAccessException {
        check(Configuration.class);
    }
}
