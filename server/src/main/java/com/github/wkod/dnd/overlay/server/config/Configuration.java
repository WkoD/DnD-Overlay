package com.github.wkod.dnd.overlay.server.config;

import com.github.wkod.dnd.overlay.util.config.ConfigurationBase;

public class Configuration<T> extends ConfigurationBase<T> {

    public static final Configuration<Integer> IMAGE_SIZE_MIN = new Configuration<>("image.size.min", Integer.class);

    protected Configuration(String name, Class<?> clazz) {
        super(name, clazz);
    }
}
