package com.github.wkod.dnd.overlay.server.config;

import com.github.wkod.dnd.overlay.util.config.ConfigurationBase;
import com.github.wkod.dnd.overlay.util.config.ConfigurationValidator;

public final class Configuration<T> extends ConfigurationBase<T> {

    public static final Configuration<Double> IMAGE_SCALE_ONLOAD = new Configuration<>("image.scale.onload",
            Double.class, (Double value) -> {
                return value >= 0d;
            });
    public static final Configuration<Integer> IMAGE_SIZE_MIN_VISIBLE = new Configuration<>("image.size.min.visible",
            Integer.class, (Integer value) -> {
                return value > 0;
            });

    public static final Configuration<Integer> BACKGROUND_TEXT_SIZE = new Configuration<>("background.text.size",
            Integer.class, (Integer value) -> {
                return value > 0;
            });

    private Configuration(String name, Class<?> clazz) {
        super(name, clazz);
    }

    private Configuration(String name, Class<?> clazz, ConfigurationValidator<T> validator) {
        super(name, clazz, validator);
    }
}
