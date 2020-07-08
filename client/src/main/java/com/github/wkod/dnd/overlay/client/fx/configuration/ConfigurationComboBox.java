package com.github.wkod.dnd.overlay.client.fx.configuration;

import com.github.wkod.dnd.overlay.configuration.ConfigurationParameter;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

public class ConfigurationComboBox<T> extends ComboBox<T> implements ConfigurationField {

    private final ConfigurationParameter<T> configuration;

    public ConfigurationComboBox(ConfigurationParameter<T> configuration) {
        super(FXCollections.observableArrayList(configuration.getValues()));
        this.configuration = configuration;

        setValue(this.configuration.get());
    }

    @Override
    public void updateConfiguration() {
        configuration.set(getValue());
    }
}
