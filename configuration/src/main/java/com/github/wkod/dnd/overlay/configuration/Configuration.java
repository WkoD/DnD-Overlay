package com.github.wkod.dnd.overlay.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.wkod.dnd.overlay.exception.OlRuntimeException;
import com.github.wkod.dnd.overlay.localization.Messages;

public abstract class Configuration {

    /**
     * Delimiter for configurations with multiple values.
     */
    public static final String LIST_DELIMITER = ",";

    /**
     * Properties file location.
     */
    private static File propertiesfile;

    /**
     * Read configuration from file and check values.
     * 
     * @param file     File
     * @param internal InputStream
     */
    public static void load(File file, InputStream internal, Class<? extends Configuration> clazz) {

        propertiesfile = file;
        Properties properties = new Properties();

        try {
            // load default configuration
            if (internal != null) {
                properties.load(internal);
            }

            // load from file
            // NOTE if internal was found, both will be merged
            // with file entries overriding internal entries
            if (file != null && file.exists()) {
                try (InputStream is = new FileInputStream(file)) {
                    properties.load(is);
                }
            } else if (internal == null) {
                // no internal or external configuration found
                throw new OlRuntimeException(Messages.CONFIGURATION_ERROR);
            }

        } catch (IOException e) {
            throw new OlRuntimeException(Messages.CONFIGURATION_ERROR, e);
        }

        Map<String, String> map = new HashMap<>();

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            map.put((String) entry.getKey(), (String) entry.getValue());
        }

        fromMap(map, clazz);
    }

    /**
     * Read configuration from String map.
     * 
     * @param properties Map<String, String>
     * @param clazz      Class<? extends Configuration>
     */
    public static void fromMap(Map<String, String> properties, Class<? extends Configuration> clazz) {
        // set and check values
        List<ConfigurationParameter<?>> list = values(clazz);

        for (ConfigurationParameter<?> config : list) {
            if (properties.containsKey(config.name())) {
                // set only known config values
                config.load((String) properties.get(config.name()));
            } else {
                // validate current value if none is given
                // NOTE this is done so no configuration value
                // is missing when configuration is changed
                config.validate();
            }
        }
    }

    /**
     * Return all configuration parameters as String map.
     * 
     * @param clazz
     * @return Map<String, String>
     */
    public static Map<String, String> toMap(Class<? extends Configuration> clazz) {
        Map<String, String> map = new HashMap<>();
        List<ConfigurationParameter<?>> list = values(clazz);

        for (ConfigurationParameter<?> config : list) {
            map.put(config.name(), config.save());
        }

        return map;
    }

    /**
     * Save current configuration.
     * 
     * @param file  File
     * @param clazz Class<? extends Configuration>
     */
    public static void save(Class<? extends Configuration> clazz) {
        // ignore if no properties file was given
        if (propertiesfile == null) {
            return;
        }

        Properties properties = new Properties();
        properties.putAll(toMap(clazz));

        // delete old properties file
        if (propertiesfile.exists()) {
            propertiesfile.delete();
        }

        // write configuration
        try (OutputStream os = new FileOutputStream(propertiesfile)) {
            properties.store(os, null);
        } catch (IOException e) {
            throw new OlRuntimeException(Messages.CONFIGURATION_ERROR, e);
        }
    }

    /**
     * Creates a copy of all configuration parameters for SpringBoot configuration.
     * 
     * @param clazz Class<?>
     * @return Properties
     */
    public static Properties getPropertiesForSpringBoot(Class<? extends Configuration> clazz) {
        Properties properties = new Properties();

        List<ConfigurationParameter<?>> list = values(clazz);
        for (ConfigurationParameter<?> parameter : list) {
            // only set default value
            properties.setProperty(parameter.name(), parameter.toString());
        }

        return properties;
    }

    /**
     * Get all declared configuration parameters.
     * 
     * @param clazz Class<?>
     * @return List<ConfigurationBase<?>>
     */
    public static List<ConfigurationParameter<?>> values(Class<? extends Configuration> clazz) {
        List<ConfigurationParameter<?>> list = new ArrayList<>();

        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (ConfigurationParameter.class.isAssignableFrom(field.getType())
                        && Modifier.isStatic(field.getModifiers())) {
                    list.add(((ConfigurationParameter<?>) field.get(null)));
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new OlRuntimeException(Messages.CONFIGURATION_ERROR, e);
        }

        return list;
    }

    protected static ConfigurationValidator<Integer> INTEGER_POSITIVE = (Integer value) -> {
        return value > 0;
    };

    protected static ConfigurationValidator<Integer> INTEGER_POSITIVE_ZERO = (Integer value) -> {
        return value >= 0;
    };

    protected static ConfigurationValidator<Double> DOUBLE_POSITIVE = (Double value) -> {
        return value > 0.0;
    };
}
