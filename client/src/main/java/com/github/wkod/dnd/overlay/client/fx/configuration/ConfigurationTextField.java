package com.github.wkod.dnd.overlay.client.fx.configuration;

import com.github.wkod.dnd.overlay.configuration.ConfigurationParameter;

import javafx.scene.control.TextField;

public class ConfigurationTextField<T> extends TextField implements ConfigurationField {

    private final ConfigurationParameter<T> configuration;

    public ConfigurationTextField(ConfigurationParameter<T> element) {
        super(element.toString());
        configuration = element;
    }

    @Override
    public void updateConfiguration() {
        configuration.fromString(getText());
    }
}
