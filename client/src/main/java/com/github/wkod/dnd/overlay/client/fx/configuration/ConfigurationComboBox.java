package com.github.wkod.dnd.overlay.client.fx.configuration;

import com.github.wkod.dnd.overlay.configuration.ConfigurationParameter;
import com.github.wkod.dnd.overlay.configuration.ScreenConfigurationParameter;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

public class ConfigurationComboBox<T> extends ComboBox<T> implements ConfigurationField {

    private final ConfigurationParameter<T> configuration;
    private final int screenid;

    public ConfigurationComboBox(ConfigurationParameter<T> configuration) {
        super(FXCollections.observableArrayList(configuration.getValues()));
        this.configuration = configuration;
        this.screenid = -1;

        setValue(this.configuration.get());
    }

    public ConfigurationComboBox(ScreenConfigurationParameter<T> configuration, int screenid) {
        super(FXCollections.observableArrayList(configuration.getValues()));
        this.configuration = configuration;
        this.screenid = screenid;

        setValue(configuration.get(screenid));
    }

    @Override
    public void updateConfiguration() {
        if (screenid < 0) {
            configuration.set(getValue());
        } else {
            ((ScreenConfigurationParameter<T>) configuration).set(getValue(), screenid);
        }
    }
}
