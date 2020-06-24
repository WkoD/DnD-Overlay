package com.github.wkod.dnd.overlay.server.config;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.github.wkod.dnd.overlay.util.ConfigurationBase;

public class Configuration<T> extends ConfigurationBase<T> {

    public static final Configuration<Integer> MIN_IMAGE_SIZE = new Configuration<>("MIN_IMAGE_SIZE", Integer.class);
    
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
