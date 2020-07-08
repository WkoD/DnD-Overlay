package com.github.wkod.dnd.overlay.configuration;

import java.util.Locale;

import com.github.wkod.dnd.overlay.localization.LocalizationUtils;
import com.github.wkod.dnd.overlay.util.LogUtils.Level;

public class ServerConfiguration extends Configuration {

    public static final ConfigurationParameter<Level> LOGGER_LEVEL = new ConfigurationParameter<>("logger.level",
            Level.class);
    public static final ConfigurationParameter<Locale> LOGGER_LOCALE = new ConfigurationParameter<>("logger.locale",
            Locale.class, (Locale value) -> {
                return LocalizationUtils.isSupported(value);
            });

    public static final ConfigurationParameter<Integer> SERVER_PORT = new ConfigurationParameter<>("server.port",
            Integer.class, INTEGER_POSITIVE_ZERO);
    public static final ConfigurationParameter<String> SERVER_SERVLET_CONTEXT_PATH = new ConfigurationParameter<>(
            "server.servlet.context-path", String.class);

    public static final ScreenConfigurationParameter<Integer> IMAGE_SIZE_MIN_VISIBLE = new ScreenConfigurationParameter<>(
            "image.size.min.visible", Integer.class, INTEGER_POSITIVE);
    public static final ScreenConfigurationParameter<Double> IMAGE_SIZE_SCALE_ONLOAD = new ScreenConfigurationParameter<>(
            "image.size.scale.onload", Double.class, DOUBLE_POSITIVE);
    public static final ScreenConfigurationParameter<javafx.geometry.Pos> IMAGE_TEXT_POSITION = new ScreenConfigurationParameter<>(
            "image.text.position", javafx.geometry.Pos.class);
    public static final ScreenConfigurationParameter<Integer> IMAGE_TEXT_SIZE = new ScreenConfigurationParameter<>(
            "image.text.size", Integer.class, INTEGER_POSITIVE);
    public static final ScreenConfigurationParameter<Boolean> IMAGE_TRANSPARENCY = new ScreenConfigurationParameter<>(
            "image.transparency", Boolean.class);

    public static final ScreenConfigurationParameter<javafx.geometry.Pos> BACKGROUND_TEXT_POSITION = new ScreenConfigurationParameter<>(
            "background.text.position", javafx.geometry.Pos.class);
    public static final ScreenConfigurationParameter<Integer> BACKGROUND_TEXT_SIZE = new ScreenConfigurationParameter<>(
            "background.text.size", Integer.class, INTEGER_POSITIVE);
    public static final ScreenConfigurationParameter<Boolean> BACKGROUND_TRANSPARENCY = new ScreenConfigurationParameter<>(
            "background.transparency", Boolean.class);
}
