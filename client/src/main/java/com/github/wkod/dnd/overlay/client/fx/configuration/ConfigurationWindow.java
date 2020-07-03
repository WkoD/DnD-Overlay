package com.github.wkod.dnd.overlay.client.fx.configuration;

import static com.github.wkod.dnd.overlay.client.fx.localization.Label.*;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.cal10n.LocLogger;

import com.github.wkod.dnd.overlay.configuration.Configuration;
import com.github.wkod.dnd.overlay.configuration.ConfigurationParameter;
import com.github.wkod.dnd.overlay.exception.OlRuntimeException;
import com.github.wkod.dnd.overlay.util.Utils;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConfigurationWindow extends Stage {

    private static final LocLogger LOGGER = Utils.getLogger(ConfigurationWindow.class);

    private final List<ConfigurationField> valuelist = new ArrayList<>();

    public ConfigurationWindow(Class<? extends Configuration> clazz) {
        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setVgap(5);
        root.setHgap(5);

        List<ConfigurationParameter<?>> list = Configuration.values(clazz);
        int row = 0;

        for (ConfigurationParameter<?> element : list) {
            Label name = new Label(element.name());
            root.add(name, 0, row);
            GridPane.setColumnSpan(name, 2);

            if (element.isTypeEnum()) {
                ConfigurationComboBox<?> value = new ConfigurationComboBox<>(element);
                valuelist.add(value);
                root.add(value, 2, row);
                GridPane.setColumnSpan(value, 2);
            } else {
                ConfigurationTextField<?> value = new ConfigurationTextField<>(element);
                valuelist.add(value);
                root.add(value, 2, row);
                GridPane.setColumnSpan(value, 2);
            }

            ++row;
        }

        Button confirm = new Button(CONFIRM.localize());
        Button cancel = new Button(CANCEL.localize());
        Button save = new Button(SAVE.localize());

        root.add(confirm, 0, row);
        root.add(cancel, 1, row);
        root.add(save, 4, row);

        // set scene
        Scene scene = new Scene(root);

        setTitle(CLIENT_CONFIGURATION_CLIENT.localize() + " " + CLIENT_CONFIGURATION.localize());
        setScene(scene);

        // set events
        confirm.setOnAction(e -> {
            update();
            close();
            e.consume();
        });

        cancel.setOnAction(e -> {
            close();
            e.consume();
        });
        
        save.setOnAction(e -> {
            update();
            Configuration.save(clazz);
            e.consume();
        });
    }
    
    /**
     * Update configuration.
     */
    private void update() {
        try {
            // update values
            for (ConfigurationField value : valuelist) {
                value.updateConfiguration();
            }
        } catch (OlRuntimeException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
}