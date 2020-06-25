package com.github.wkod.dnd.overlay.client.config;

import com.github.wkod.dnd.overlay.util.config.ConfigurationBase;

public class Configuration<T> extends ConfigurationBase<T> {

    public static final Configuration<String> CONNECTION_HOST = new Configuration<>("connection.host", String.class);
    public static final Configuration<String> CONNECTION_PORT = new Configuration<>("connection.port", String.class);
    public static final Configuration<Boolean> STARTUP_UPDATE_SCREEN = new Configuration<>("startup.update.screen", Boolean.class);
    
    protected Configuration(String name, Class<?> clazz) {
        super(name, clazz);
    }
}
