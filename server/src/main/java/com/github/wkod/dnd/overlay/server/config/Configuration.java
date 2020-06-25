package com.github.wkod.dnd.overlay.server.config;

import com.github.wkod.dnd.overlay.util.ConfigurationBase;

public class Configuration<T> extends ConfigurationBase<T> {

    public static final Configuration<Integer> MIN_IMAGE_SIZE = new Configuration<>("MIN_IMAGE_SIZE", Integer.class);

    protected Configuration(String name, Class<?> clazz) {
        super(name, clazz);
    }
}
