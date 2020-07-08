package com.github.wkod.dnd.overlay.client.fx.configuration;

import static com.github.wkod.dnd.overlay.client.fx.localization.Label.*;
import static com.github.wkod.dnd.overlay.localization.Messages.*;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.cal10n.LocLogger;

import com.github.wkod.dnd.overlay.client.rest.RestClient;
import com.github.wkod.dnd.overlay.configuration.Configuration;
import com.github.wkod.dnd.overlay.configuration.ConfigurationParameter;
import com.github.wkod.dnd.overlay.configuration.ScreenConfigurationParameter;
import com.github.wkod.dnd.overlay.configuration.ServerConfiguration;
import com.github.wkod.dnd.overlay.exception.OlRuntimeException;
import com.github.wkod.dnd.overlay.util.LogUtils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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

    public ConfigurationWindow(Class<? extends Configuration> clazz, int screencount) {
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
        List<ScreenConfigurationParameter<?>> screenlist = new ArrayList<>();
        int row = 0;

        // add server configuration
        for (ConfigurationParameter<?> element : list) {
            if (element instanceof ScreenConfigurationParameter<?>) {
                screenlist.add((ScreenConfigurationParameter<?>) element);
                continue;
            }

            addToGrid(root, row++, element);
        }

        if (!screenlist.isEmpty()) {
            // add tabs for each screen + default
            TabPane screentab = new TabPane();
            screentab.setMaxWidth(Double.MAX_VALUE);
            List<GridPane> screentablist = new ArrayList<>();

            // default tab
            GridPane defaultpane = new GridPane();
            defaultpane.getColumnConstraints().addAll(constraint, constraint, constraint, constraint);
            screentablist.add(defaultpane);
            screentab.getTabs().add(createTab("Default", defaultpane));
            root.add(screentab, 0, row);
            GridPane.setColumnSpan(screentab, 4);
            ++row;

            // screen tabs
            for (int i = 0; i < screencount; ++i) {
                GridPane pane = new GridPane();
                pane.getColumnConstraints().addAll(constraint, constraint, constraint, constraint);
                screentablist.add(pane);
                screentab.getTabs().add(createTab("Screen " + i, pane));
            }

            // add screenspecific configuration
            int tabrow = 0;
            for (ScreenConfigurationParameter<?> element : screenlist) {
                addToGrid(screentablist, tabrow++, element);
            }
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

    private void addToGrid(GridPane pane, int row, ConfigurationParameter<?> element) {
        // configuration name
        Label name = new Label(element.name());
        pane.add(name, 0, row);
        GridPane.setColumnSpan(name, 2);

        // configuration value
        Control value = addValueNode(element);
        pane.add(value, 2, row);
        value.setMaxWidth(Double.MAX_VALUE);
        GridPane.setColumnSpan(value, 2);
    }

    private void addToGrid(List<GridPane> panelist, int row, ScreenConfigurationParameter<?> element) {
        // add to default pane
        GridPane pane = panelist.get(0);
        addToGrid(pane, row, element);

        // add to screen tabs
        for (int i = 1; i < panelist.size(); ++i) {
            pane = panelist.get(i);

            // configuration name
            Label name = new Label(element.name());
            pane.add(name, 0, row);
            GridPane.setColumnSpan(name, 2);

            // configuration value
            Control value = addValueNode(element, i - 1);
            pane.add(value, 2, row);
            value.setMaxWidth(Double.MAX_VALUE);
            GridPane.setColumnSpan(value, 2);
        }
    }

    /**
     * Creates a new node element depending on configuration type and adds it to the
     * value list.
     * 
     * @param element ConfigurationParameter<?>
     * @return Control
     */
    private Control addValueNode(ConfigurationParameter<?> element) {
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
     * Creates a new node element depending on configuration type and adds it to the
     * value list.
     * 
     * @param element  ConfigurationParameter<?>
     * @param screenid int
     * @return Control
     */
    private Control addValueNode(ScreenConfigurationParameter<?> element, int screenid) {
        if (element.hasValues()) {
            ConfigurationComboBox<?> value = new ConfigurationComboBox<>(element, screenid);
            valuelist.add(value);
            return value;
        } else {
            ConfigurationTextField<?> value = new ConfigurationTextField<>(element, screenid);
            valuelist.add(value);
            return value;
        }
    }

    private Tab createTab(String name, Node node) {
        Tab tab = new Tab(name, node);
        tab.setClosable(false);
        return tab;
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
