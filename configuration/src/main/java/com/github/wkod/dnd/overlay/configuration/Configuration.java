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
import java.util.List;
import java.util.Properties;

import com.github.wkod.dnd.overlay.exception.OlRuntimeException;
import com.github.wkod.dnd.overlay.localization.Messages;

public abstract class Configuration {
    
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

        // set and check values
        List<ConfigurationParameter<?>> list = values(clazz);

        for (ConfigurationParameter<?> config : list) {
            config.fromString((String) properties.get(config.name()));
            config.get();
        }
    }
    
    /**
     * Save current configuration.
     * 
     * @param file File
     * @param clazz Class<? extends Configuration>
     */
    public static void save(Class<? extends Configuration> clazz) {
        // ignore if no properties file was given
        if (propertiesfile == null) {
            return;
        }
        
        List<ConfigurationParameter<?>> list = values(clazz);
        Properties properties = new Properties();

        for (ConfigurationParameter<?> config : list) {
            properties.setProperty(config.name(), config.toString());
        }
        
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
                if (field.getType().equals(ConfigurationParameter.class) && Modifier.isStatic(field.getModifiers())) {
                    list.add(((ConfigurationParameter<?>) field.get(null)));
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new OlRuntimeException(Messages.CONFIGURATION_ERROR, e);
        }

        return list;
    }
}
