package com.github.wkod.dnd.overlay.configuration;

import java.util.Locale;

import com.github.wkod.dnd.overlay.localization.LocalizationUtils;

public class ServerConfiguration extends Configuration {

    public static final ConfigurationParameter<String> LOGGER_LEVEL = new ConfigurationParameter<>("logger.level",
            String.class);
    public static final ConfigurationParameter<Locale> LOGGER_LOCALE = new ConfigurationParameter<>("logger.locale",
            Locale.class, (Locale value) -> {
                return LocalizationUtils.isSupported(value);
            });

    public static final ConfigurationParameter<Integer> SERVER_PORT = new ConfigurationParameter<>("server.port",
            Integer.class, (Integer value) -> {
                return value >= 0;
            });
    public static final ConfigurationParameter<String> SERVER_SERVLET_CONTEXT_PATH = new ConfigurationParameter<>(
            "server.servlet.context-path", String.class);

    public static final ConfigurationParameter<Integer> IMAGE_SIZE_MIN_VISIBLE = new ConfigurationParameter<>(
            "image.size.min.visible", Integer.class, (Integer value) -> {
                return value > 0;
            });
    public static final ConfigurationParameter<Double> IMAGE_SIZE_SCALE_ONLOAD = new ConfigurationParameter<>(
            "image.size.scale.onload", Double.class, (Double value) -> {
                return value >= 0d;
            });
    public static final ConfigurationParameter<javafx.geometry.Pos> IMAGE_TEXT_POSITION = new ConfigurationParameter<>(
            "image.text.position", javafx.geometry.Pos.class);
    public static final ConfigurationParameter<Integer> IMAGE_TEXT_SIZE = new ConfigurationParameter<>(
            "image.text.size", Integer.class, (Integer value) -> {
                return value > 0;
            });
    public static final ConfigurationParameter<Boolean> IMAGE_TRANSPARENCY = new ConfigurationParameter<>(
            "image.transparency", Boolean.class);

    public static final ConfigurationParameter<javafx.geometry.Pos> BACKGROUND_TEXT_POSITION = new ConfigurationParameter<>(
            "background.text.position", javafx.geometry.Pos.class);
    public static final ConfigurationParameter<Integer> BACKGROUND_TEXT_SIZE = new ConfigurationParameter<>(
            "background.text.size", Integer.class, (Integer value) -> {
                return value > 0;
            });
    public static final ConfigurationParameter<Boolean> BACKGROUND_TRANSPARENCY = new ConfigurationParameter<>(
            "background.transparency", Boolean.class);
}
