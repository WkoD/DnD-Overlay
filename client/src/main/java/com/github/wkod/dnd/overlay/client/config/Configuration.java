package com.github.wkod.dnd.overlay.client.config;

import com.github.wkod.dnd.overlay.util.ConfigurationBase;

public class Configuration<T> extends ConfigurationBase<T> {

    public static final Configuration<String> HOST = new Configuration<>("HOST", String.class);
    public static final Configuration<String> PORT = new Configuration<>("PORT", String.class);
    public static final Configuration<Boolean> UPDATE_SCREENS_AT_STARTUP = new Configuration<>("UPDATE_SCREENS_AT_STARTUP", Boolean.class);
    
    protected Configuration(String name, Class<?> clazz) {
        super(name, clazz);
    }
}
