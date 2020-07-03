package com.github.wkod.dnd.overlay.configuration;

public interface ConfigurationValidator<T> {

    boolean validate(T value);
}
