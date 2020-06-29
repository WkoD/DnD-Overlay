package com.github.wkod.dnd.overlay.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import com.github.wkod.dnd.overlay.api.exception.OlRuntimeException;

public abstract class ConfigurationBase<T> {
    
    /**
     * Configuration cache.
     */
    private static final Properties CONFIGURATION = new Properties();
    
    private final String name;
    
    private final Class<?> clazz;
    
    private final ConfigurationValidator<T> validator;
    
    /**
     * Constructor.
     * 
     * @param name String
     * @param clazz Class<?>
     */
    protected ConfigurationBase(String name, Class<?> clazz) {
        this(name, clazz, null);
    }
    
    protected ConfigurationBase(String name, Class<?> clazz, ConfigurationValidator<T> validator) {
        this.name = name;
        this.clazz = clazz;
        
        if (validator != null) {
            this.validator = validator;
        } else {
            this.validator = (T value) -> {
                return true;
            };
        }
    }
    
    /**
     * Get the value of a property cast into its specific type.
     * 
     * @return T
     */
    @SuppressWarnings("unchecked")
    public T get() {
        String valuestring = CONFIGURATION.getProperty(name);
        T value;
        
        try {
            if (clazz.isAssignableFrom(Boolean.class)) {
                value = (T) Boolean.valueOf(valuestring);
            } else if (clazz.isAssignableFrom(Integer.class)) {
                value = (T) Integer.valueOf(valuestring);
            } else if (clazz.isAssignableFrom(Double.class)) {
                value = (T) Double.valueOf(valuestring);
            } else {
                value = (T) valuestring;
            }
        } catch (NumberFormatException e) {
            throw new OlRuntimeException("Invalid value \"" + valuestring + "\" for configuration \"" + this.name + "\"", e);
        }
        
        if (!this.validator.validate(value)) {
            throw new OlRuntimeException("Invalid value \"" + valuestring + "\" for configuration \"" + this.name + "\"");
        }
        
        return value;
    }

    /**
     * Read configuration from file.
     * 
     * @param configfile File
     * @throws IOException Exception
     */
    public static void load(final File configfile) throws IOException {
        // clear current config
        CONFIGURATION.clear();
        
        // ignore missing files
        if (configfile == null || !configfile.exists()) {
            return;
        }
        
        try (InputStream is = new FileInputStream(configfile)) {
            CONFIGURATION.load(is);
        }
    }
    
    /**
     * Creates a copy of all configuration parameters.
     * 
     * @return Properties
     */
    public static Properties getCopy() {
        Properties clone = new Properties();
        clone.putAll(CONFIGURATION);
        return clone;
    }
    
    /**
     * Check all declared configuration parameters.
     * 
     * @param clazz Class<?>
     * @throws IllegalArgumentException Exception
     * @throws IllegalAccessException Exception
     */
    protected static void check(Class<?> clazz) throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(clazz) && Modifier.isStatic(field.getModifiers())) {
                ((ConfigurationBase<?>)field.get(null)).get();
            }
        }
    }
}
