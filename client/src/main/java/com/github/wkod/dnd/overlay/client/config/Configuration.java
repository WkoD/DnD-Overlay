package com.github.wkod.dnd.overlay.client.config;

import java.util.Locale;

import com.github.wkod.dnd.overlay.api.localization.LocalizationUtils;
import com.github.wkod.dnd.overlay.util.config.ConfigurationBase;
import com.github.wkod.dnd.overlay.util.config.ConfigurationValidator;

public class Configuration<T> extends ConfigurationBase<T> {

    public static final Configuration<String> LOGGER_LEVEL = new Configuration<>("logger.level", String.class);
    public static final Configuration<Locale> LOGGER_LOCALE = new Configuration<>("logger.locale", Locale.class,
            (Locale value) -> {
        return LocalizationUtils.isSupported(value);
    });
    
    public static final Configuration<String> SERVER_HOST = new Configuration<>("server.host", String.class);
    public static final Configuration<String> SERVER_PORT = new Configuration<>("server.port", String.class);
    public static final Configuration<String> SERVER_SERVLET_CONTEXT_PATH = new Configuration<>(
            "server.servlet.context-path", String.class);

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
}
