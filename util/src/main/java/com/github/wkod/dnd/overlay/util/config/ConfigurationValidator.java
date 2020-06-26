package com.github.wkod.dnd.overlay.util.config;

public interface ConfigurationValidator<T> {

    boolean validate(T value);
}
