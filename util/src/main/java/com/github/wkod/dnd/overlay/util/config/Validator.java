package com.github.wkod.dnd.overlay.util.config;

public interface Validator<T> {

    boolean validate(T value);
}
