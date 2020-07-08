package com.github.wkod.dnd.overlay.client.fx.configuration;

import static com.github.wkod.dnd.overlay.client.fx.localization.Label.*;
import static com.github.wkod.dnd.overlay.localization.Messages.*;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.cal10n.LocLogger;

import com.github.wkod.dnd.overlay.client.rest.RestClient;
import com.github.wkod.dnd.overlay.configuration.Configuration;
import com.github.wkod.dnd.overlay.configuration.ConfigurationParameter;
import com.github.wkod.dnd.overlay.configuration.ServerConfiguration;
import com.github.wkod.dnd.overlay.exception.OlRuntimeException;
import com.github.wkod.dnd.overlay.util.LogUtils;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConfigurationWindow extends Stage {

    private static final LocLogger LOGGER = LogUtils.getLogger(ConfigurationWindow.class);

    private final Class<? extends Configuration> clazz;
    private final boolean remote;

    /**
     * List of updatable value fields.
     */
    private final List<ConfigurationField> valuelist = new ArrayList<>();

    public ConfigurationWindow(Class<? extends Configuration> clazz) {
        // flag if server or client configuration
        remote = clazz.equals(ServerConfiguration.class);
        this.clazz = clazz;

        if (remote) {
            RestClient.getConfiguration();
        }

        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setVgap(5);
        root.setHgap(5);

        // 4 columns each 25% from the whole grid
        ColumnConstraints constraint = new ColumnConstraints();
        constraint.setPercentWidth(25);
        root.getColumnConstraints().addAll(constraint, constraint, constraint, constraint);

        List<ConfigurationParameter<?>> list = Configuration.values(this.clazz);
        int row = 0;

        for (ConfigurationParameter<?> element : list) {
            // configuration name
            Label name = new Label(element.name());
            root.add(name, 0, row);
            GridPane.setColumnSpan(name, 2);

            // configuration value
            Control value = getNode(element);
            root.add(value, 2, row);
            value.setMaxWidth(Double.MAX_VALUE);
            GridPane.setColumnSpan(value, 2);

            ++row;
        }

        Button confirm = new Button(CONFIRM.localize());
        Button save = new Button(SAVE.localize());
        Button cancel = new Button(CANCEL.localize());

        confirm.setMaxWidth(Double.MAX_VALUE);
        save.setMaxWidth(Double.MAX_VALUE);
        cancel.setMaxWidth(Double.MAX_VALUE);

        root.add(confirm, 0, row);
        root.add(save, 1, row);
        root.add(cancel, 3, row);

        // set scene
        Scene scene = new Scene(root);

        setTitle((remote ? CLIENT_CONFIGURATION_SERVER.localize() : CLIENT_CONFIGURATION_CLIENT.localize()) + " "
                + CLIENT_CONFIGURATION.localize());
        setScene(scene);

        // set events
        confirm.setOnAction(e -> {
            if (update(false)) {
                close();
            }
            e.consume();
        });

        cancel.setOnAction(e -> {
            close();
            e.consume();
        });

        save.setOnAction(e -> {
            if (update(true)) {
                LOGGER.info(CLIENT_CONFIGURATION_SAVE.localize(
                        remote ? CLIENT_CONFIGURATION_SERVER.localize() : CLIENT_CONFIGURATION_CLIENT.localize()));
                close();
            }
            e.consume();
        });
    }

    /**
     * Creates a new node element depending on configuration type and adds it to the
     * value list.
     * 
     * @param element ConfigurationParameter<?>
     * @return Control
     */
    private Control getNode(ConfigurationParameter<?> element) {
        if (element.hasValues()) {
            ConfigurationComboBox<?> value = new ConfigurationComboBox<>(element);
            valuelist.add(value);
            return value;
        } else {
            ConfigurationTextField<?> value = new ConfigurationTextField<>(element);
            valuelist.add(value);
            return value;
        }
    }

    /**
     * Update configuration. Returns true, if update was successful.
     * 
     * @param save boolean
     * @return boolean
     */
    private boolean update(boolean save) {
        try {
            // update values
            for (ConfigurationField value : valuelist) {
                value.updateConfiguration();
            }

            if (remote) {
                // send to server
                RestClient.setConfiguration(save);
            } else if (save) {
                // save locally
                Configuration.save(clazz);
            }
        } catch (OlRuntimeException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
            
            LOGGER.error(e.getMessage(), e);
            return false;
        }

        return true;
    }
}
