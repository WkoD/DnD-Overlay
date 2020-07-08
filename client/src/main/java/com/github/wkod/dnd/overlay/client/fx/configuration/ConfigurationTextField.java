package com.github.wkod.dnd.overlay.client.fx.configuration;

import com.github.wkod.dnd.overlay.configuration.ConfigurationParameter;
import com.github.wkod.dnd.overlay.configuration.ScreenConfigurationParameter;

import javafx.scene.control.TextField;

public class ConfigurationTextField<T> extends TextField implements ConfigurationField {

    private final ConfigurationParameter<T> configuration;
    private final int screenid;

    public ConfigurationTextField(ConfigurationParameter<T> configuration) {
        super(configuration.toString());
        this.configuration = configuration;
        this.screenid = -1;
    }
    
    public ConfigurationTextField(ScreenConfigurationParameter<T> configuration, int screenid) {
        super(configuration.toString(screenid));
        this.configuration = configuration;
        this.screenid = screenid;
    }

    @Override
    public void updateConfiguration() {
        if (screenid < 0) {
            configuration.fromString(getText());
        } else {
            ((ScreenConfigurationParameter<T>) configuration).fromString(getText(), screenid);
        }
    }
}
