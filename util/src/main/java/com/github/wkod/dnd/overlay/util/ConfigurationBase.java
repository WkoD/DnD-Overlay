package com.github.wkod.dnd.overlay.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class ConfigurationBase<T> {
    
    private final String name;
    
    private final Class<?> clazz;
    
    protected ConfigurationBase(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }
    
    @SuppressWarnings("unchecked")
    public T get() {
        String value = getConfig(name);
        if (clazz.isAssignableFrom(Boolean.class)) {
            return (T) Boolean.valueOf(value);
        } else if (clazz.isAssignableFrom(Integer.class)) {
            return (T) Integer.valueOf(value);
        } else {
            return (T) value;
        }
    }
    
    protected abstract String getConfig(final String name);
    
    /**
     * Read configuration from file.
     * 
     * @param configfile File
     * @throws IOException Exception
     */
    protected static void load(final Properties configuration, final File configfile) throws IOException {
        // clear current config
        configuration.clear();
        
        // ignore missing files
        if (configfile == null || !configfile.exists()) {
            return;
        }
        
        try (InputStream is = new FileInputStream(configfile)) {
            configuration.load(is);
        }
    }
}