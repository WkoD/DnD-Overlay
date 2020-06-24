package com.github.wkod.dnd.overlay.client.config;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.github.wkod.dnd.overlay.util.ConfigurationBase;

public class Configuration<T> extends ConfigurationBase<T> {

    public static final Configuration<String> HOST = new Configuration<>("HOST", String.class);
    public static final Configuration<String> PORT = new Configuration<>("PORT", String.class);
    public static final Configuration<Boolean> UPDATE_SCREENS_AT_STARTUP = new Configuration<>("UPDATE_SCREENS_AT_STARTUP", Boolean.class);
    
    
    /**
     * Configuration cache.
     */
    private static Properties CONFIGURATION = new Properties();
    
    protected Configuration(String name, Class<?> clazz) {
        super(name, clazz);
    }

    @Override
    protected String getConfig(String name) {
        return CONFIGURATION.getProperty(name);
    }
    
    public static void load(final File configfile) throws IOException {
        load(CONFIGURATION, configfile);
    }
}
