package com.github.wkod.dnd.overlay.client.fx;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.api.exception.OlException;
import com.github.wkod.dnd.overlay.client.config.Configuration;
import com.github.wkod.dnd.overlay.client.rest.Sender;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * ControlWindow.
 * 
 * @author cpawek
 * @version 1.0
 */
public class ClientWindow extends Stage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientWindow.class);

    private final List<OlScreen> screenList = new ArrayList<>();
    private final HBox screenListPane;

    private final Scene scene;

    public ClientWindow() {
        // root pane
        BorderPane root = new BorderPane();

        // set menu
        root.setTop(createMenu());

        // set screen pane
        screenListPane = new HBox();
        root.setCenter(screenListPane);

        // set scene
        scene = new Scene(root);

        setTitle("Control Window");
        setScene(scene);

        // update screens if enabled
        if (Configuration.STARTUP_UPDATE_SCREEN.get()) {
            updateScreenListPane();
        }
    }

    private MenuBar createMenu() {
        // create menu elements
        Menu menu = new Menu("Menu");
        MenuItem screens = new MenuItem("Read Screens");
        MenuItem exit = new MenuItem("Exit");

        menu.getItems().add(screens);
        menu.getItems().add(exit);

        // create menu bar
        MenuBar menubar = new MenuBar();
        menubar.getMenus().add(menu);

        // set events
        screens.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent pEvent) {
                updateScreenListPane();
            }
        });

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent pEvent) {
                Platform.exit();
            }
        });

        setOnCloseRequest(e -> {
            Platform.exit();
        });

        return menubar;
    }

    private void updateScreenListPane() {
        screenList.clear();
        screenListPane.getChildren().clear();

        try {
            List<OlScreen> list = Sender.getScreens();

            if (list != null) {
                screenList.addAll(list);
            }
        } catch (OlException e) {
            LOGGER.error(e.getMessage(), e);
        }

        for (OlScreen screen : screenList) {
            // set main pane
            VBox pane = new VBox();
            pane.setId(String.valueOf(screen.getId())); // set id to screen id
            pane.setMinWidth(150);
            pane.setMinHeight(100);
            pane.setAlignment(Pos.BASELINE_CENTER);

            if (screen.isMain()) {
                pane.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                        + "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: green;");
            } else {
                pane.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                        + "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: black;");
            }

            // set screen information as labels
            Label id = new Label(String.valueOf(screen.getId()));
            id.setFont(new Font(id.getFont().getName(), 32));
            id.setAlignment(Pos.CENTER);
            id.maxWidth(Double.MAX_VALUE);
            id.maxHeight(Double.MAX_VALUE);

            Label size = new Label("(" + screen.getWidth() + "x" + screen.getHeight() + ")");
            size.setAlignment(Pos.CENTER);
            size.maxWidth(Double.MAX_VALUE);
            size.maxHeight(Double.MAX_VALUE);

            // set option boxes
            CheckBox background = new CheckBox();
            background.setId("background " + screen.getId());
            background.setAlignment(Pos.BOTTOM_LEFT);
            background.setText("Background");
            background.setIndeterminate(false);
            background.setSelected(false);

            CheckBox displayname = new CheckBox();
            displayname.setAlignment(Pos.BOTTOM_LEFT);
            displayname.setText("Display name");
            displayname.setIndeterminate(false);
            displayname.setSelected(false);

            Button toggle = new Button();
            toggle.setAlignment(Pos.BOTTOM_LEFT);
            toggle.setText("Toggle");

            Button reset = new Button();
            reset.setAlignment(Pos.BOTTOM_LEFT);
            reset.setText("Reset");

            // set events
            // toggle button
            toggle.setOnAction(e -> {
                try {
                    Sender.toggleImageData(screen.getId(), background.isSelected());
                    background.setSelected(false);
                } catch (OlException e1) {
                    LOGGER.error(e1.getMessage(), e1);
                }
            });

            // reset button
            reset.setOnAction(e -> {
                try {
                    Sender.clearImageData(screen.getId(), background.isSelected());
                    background.setSelected(false);
                } catch (OlException e1) {
                    LOGGER.error(e1.getMessage(), e1);
                }
            });

            // drag and drop
            pane.setOnDragOver(e -> {
                if (e.getGestureSource() != pane && e.getDragboard().hasFiles()) {
                    e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                e.consume();
            });

            // drag and drop
            pane.setOnDragDropped(e -> {
                Dragboard db = e.getDragboard();
                e.setDropCompleted(db.hasFiles());

                for (final File file : db.getFiles()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        Sender.setImageData(screen.getId(), displayname.isSelected() ? urlToName(file.getName()) : null,
                                imageToByte(new Image(fis)), background.isSelected());
                        background.setSelected(false);
                    } catch (IOException | OlException e1) {
                        LOGGER.error(e1.getMessage(), e1);
                    }
                }

                e.consume();
            });

            // ctrl + v
            pane.setOnKeyPressed(e -> {
                if (e.isControlDown() && KeyCode.V.equals(e.getCode())) {
                    Clipboard cb = Clipboard.getSystemClipboard();

                    try {
                        if (cb.hasContent(DataFormat.IMAGE)) {
                            Sender.setImageData(screen.getId(),
                                    displayname.isSelected() ? urlToName(cb.getUrl()) : null,
                                    imageToByte(cb.getImage()), background.isSelected());
                            background.setSelected(false);
                        }
                    } catch (IOException | OlException e1) {
                        LOGGER.error(e1.getMessage(), e1);
                    }
                }
            });

            // add children
            pane.getChildren().add(id);
            pane.getChildren().add(size);
            pane.getChildren().add(background);
            pane.getChildren().add(displayname);
            pane.getChildren().add(toggle);
            pane.getChildren().add(reset);

            // add to screen pane
            HBox.setHgrow(pane, Priority.ALWAYS);
            screenListPane.getChildren().add(pane);
        }
    }

    private static byte[] imageToByte(Image image) throws IOException {
        BufferedImage bimage = SwingFXUtils.fromFXImage(image, null);

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            ImageIO.write(bimage, "png", stream);
            return stream.toByteArray();
        }
    }

    private static String urlToName(String url) {
        if (url == null || url.length() == 0) {
            return url;
        }

        String result = null;

        int index = url.lastIndexOf('/');

        if (index < 0) {
            result = url;
        } else {
            result = url.substring(index + 1);
        }

        index = result.lastIndexOf(".");

        if (index < 0) {
            return result;
        } else {
            return result.substring(0, index);
        }
    }
}
