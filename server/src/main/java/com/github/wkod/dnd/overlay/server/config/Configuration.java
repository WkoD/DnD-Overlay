package com.github.wkod.dnd.overlay.server.config;

import com.github.wkod.dnd.overlay.util.config.ConfigurationBase;

public class Configuration<T> extends ConfigurationBase<T> {

    public static final Configuration<Integer> IMAGE_SIZE_MIN = new Configuration<>("image.size.min", Integer.class);
    public static final Configuration<Integer> BACKGROUND_TEXT_SIZE = new Configuration<>("background.text.size", Integer.class);
    

    protected Configuration(String name, Class<?> clazz) {
        super(name, clazz);
    }
}
