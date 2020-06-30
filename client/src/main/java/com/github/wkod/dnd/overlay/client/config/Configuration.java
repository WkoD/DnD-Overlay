package com.github.wkod.dnd.overlay.client.config;

import com.github.wkod.dnd.overlay.api.exception.OlRuntimeException;
import com.github.wkod.dnd.overlay.util.config.ConfigurationBase;
import com.github.wkod.dnd.overlay.util.config.ConfigurationValidator;

public class Configuration<T> extends ConfigurationBase<T> {

    public static final Configuration<String> SERVER_HOST = new Configuration<>("server.host", String.class);
    public static final Configuration<String> SERVER_PORT = new Configuration<>("server.port", String.class);
    public static final Configuration<String> SERVER_SERVLET_CONTEXT_PATH = new Configuration<>(
            "server.servlet.context-path", String.class);

    public static final Configuration<String> LOGGER_LEVEL = new Configuration<>("logger.level", String.class);

    public static final Configuration<Boolean> UPDATE_SCREEN_STARTUP = new Configuration<>("update.screen.startup",
            Boolean.class);

    /**
     * Constructor.
     * 
     * @param name  String
     * @param clazz Class<?>
     */
    private Configuration(String name, Class<?> clazz) {
        super(name, clazz);
    }

    /**
     * Constructor.
     * 
     * @param name      String
     * @param clazz     Class<?>
     * @param validator ConfigurationValidator<T>
     */
    private Configuration(String name, Class<?> clazz, ConfigurationValidator<T> validator) {
        super(name, clazz, validator);
    }

    /**
     * Check all parameter values.
     */
    public static void check() {
        try {
            check(Configuration.class);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new OlRuntimeException("Error while validating configuration", e);
        }
    }
}
