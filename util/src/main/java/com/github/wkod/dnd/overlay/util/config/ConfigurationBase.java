package com.github.wkod.dnd.overlay.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Properties;

import com.github.wkod.dnd.overlay.api.exception.OlRuntimeException;
import com.github.wkod.dnd.overlay.api.localization.Messages;

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
     * @param name  String
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
     * Get the value of the property cast into its specific type.
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
            } else if (clazz.isAssignableFrom(Locale.class)) {
                value = (T) Locale.forLanguageTag(valuestring);
            } else {
                value = (T) valuestring;
            }
        } catch (NumberFormatException e) {
            throw new OlRuntimeException(Messages.CONFIGURATION_INVALID, valuestring, name, e);
        }

        if (value == null || !this.validator.validate(value)) {
            throw new OlRuntimeException(Messages.CONFIGURATION_INVALID, valuestring);
        }

        return value;
    }

    /**
     * Set value of the property
     * 
     * @param value T
     */
    public void set(T value) {
        if (value == null || !this.validator.validate(value)) {
            throw new OlRuntimeException(Messages.CONFIGURATION_INVALID, String.valueOf(value));
        }

        CONFIGURATION.setProperty(name, String.valueOf(value));
    }

    /**
     * Read configuration from file.
     * 
     * @param file     File
     * @param internal InputStream
     */
    public static void load(File file, InputStream internal, Class<?> clazz) {

        try {
            // load default configuration
            if (internal != null) {
                CONFIGURATION.load(internal);
            }

            // load from file
            if (file == null || !file.exists()) {
                if (internal == null) {
                    // no internal or external configuration found
                    throw new OlRuntimeException(Messages.CONFIGURATION_ERROR);
                }
                
                return;
            }

            try (InputStream is = new FileInputStream(file)) {
                CONFIGURATION.load(is);
            }
        } catch (IOException e) {
            throw new OlRuntimeException(Messages.CONFIGURATION_ERROR, e);
        }

        try {
            check(clazz);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new OlRuntimeException(Messages.CONFIGURATION_ERROR, e);
        }
    }

    /**
     * Read configuration from inputstream.
     * 
     * @param configfile File
     * @throws IOException Exception
     */
    public static void load(final InputStream stream) throws IOException {

        // ignore missing files
        if (stream == null) {
            return;
        }

        CONFIGURATION.load(stream);
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
     * @throws IllegalAccessException   Exception
     */
    protected static void check(Class<?> clazz) throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(clazz) && Modifier.isStatic(field.getModifiers())) {
                ((ConfigurationBase<?>) field.get(null)).get();
            }
        }
    }
}
