package com.github.wkod.dnd.overlay.server.config;

import com.github.wkod.dnd.overlay.api.exception.OlRuntimeException;
import com.github.wkod.dnd.overlay.util.config.ConfigurationBase;
import com.github.wkod.dnd.overlay.util.config.ConfigurationValidator;

public final class Configuration<T> extends ConfigurationBase<T> {

    public static final Configuration<String> LOGGER_LEVEL = new Configuration<>("logger.level", String.class);

    public static final Configuration<String> SERVER_PORT = new Configuration<>("server.port", String.class);
    public static final Configuration<String> SERVER_SERVLET_CONTEXT_PATH = new Configuration<>(
            "server.servlet.context-path", String.class);

    public static final Configuration<Integer> IMAGE_SIZE_MIN_VISIBLE = new Configuration<>("image.size.min.visible",
            Integer.class, (Integer value) -> {
                return value > 0;
            });
    public static final Configuration<Double> IMAGE_SIZE_SCALE_ONLOAD = new Configuration<>("image.size.scale.onload",
            Double.class, (Double value) -> {
                return value >= 0d;
            });
    public static final Configuration<String> IMAGE_TEXT_POSITION = new Configuration<>("image.text.position",
            String.class, (String value) -> {
                try {
                    javafx.geometry.Pos.valueOf(value);
                } catch (IllegalArgumentException e) {
                    return false;
                }

                return true;
            });
    public static final Configuration<Integer> IMAGE_TEXT_SIZE = new Configuration<>("image.text.size", Integer.class,
            (Integer value) -> {
                return value > 0;
            });
    public static final Configuration<Boolean> IMAGE_TRANSPARENCY = new Configuration<>("image.transparency",
            Boolean.class);

    public static final Configuration<String> BACKGROUND_TEXT_POSITION = new Configuration<>("background.text.position",
            String.class, (String value) -> {
                try {
                    javafx.geometry.Pos.valueOf(value);
                } catch (IllegalArgumentException e) {
                    return false;
                }

                return true;
            });
    public static final Configuration<Integer> BACKGROUND_TEXT_SIZE = new Configuration<>("background.text.size",
            Integer.class, (Integer value) -> {
                return value > 0;
            });
    public static final Configuration<Boolean> BACKGROUND_TRANSPARENCY = new Configuration<>("background.transparency",
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

    /**
     * Check all parameter values.
     */
    public static void check() {
        try {
            check(Configuration.class);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new OlRuntimeException("Error while validating configuration", e);
        }
    }
}
