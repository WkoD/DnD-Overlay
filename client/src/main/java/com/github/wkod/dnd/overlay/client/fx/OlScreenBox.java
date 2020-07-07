package com.github.wkod.dnd.overlay.client.fx;

import static com.github.wkod.dnd.overlay.client.fx.localization.Label.*;
import static com.github.wkod.dnd.overlay.localization.Messages.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.cal10n.LocLogger;

import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.client.rest.RestClient;
import com.github.wkod.dnd.overlay.util.LogUtils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class OlScreenBox extends VBox {

    private static final LocLogger LOGGER = LogUtils.getLogger(OlScreenBox.class);

    /**
     * Associated screen.
     */
    private final OlScreen screen;

    private final CheckBox background;
    private final CheckBox displayname;

    /**
     * Constructor.
     * 
     * @param screen OlScreen
     */
    public OlScreenBox(OlScreen screen) {
        this.screen = screen;
        setSelected(false);

        setId(String.valueOf(this.screen.getId())); // set id to screen id

        setMinWidth(150);
        setMinHeight(100);
        setAlignment(Pos.BASELINE_CENTER);
        setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(4.0), new Insets(2.0))));

        // set screen information as labels
        Label id = new Label(String.valueOf(this.screen.getId()));
        id.setFont(new Font(id.getFont().getName(), 32));
        id.setAlignment(Pos.CENTER);
        id.maxWidth(Double.MAX_VALUE);
        id.maxHeight(Double.MAX_VALUE);

        Label size = new Label("(" + screen.getWidth() + "x" + screen.getHeight() + ")");
        size.setAlignment(Pos.CENTER);
        size.maxWidth(Double.MAX_VALUE);
        size.maxHeight(Double.MAX_VALUE);

        // set option boxes
        background = new CheckBox();
        background.setAlignment(Pos.BOTTOM_LEFT);
        background.setText(CLIENT_CHECKBOX_BACKGROUND.localize());
        background.setIndeterminate(false);
        background.setSelected(false);

        displayname = new CheckBox();
        displayname.setAlignment(Pos.BOTTOM_LEFT);
        displayname.setText(CLIENT_CHECKBOX_DISPLAY_NAME.localize());
        displayname.setIndeterminate(false);
        displayname.setSelected(false);

        Button toggle = new Button();
        toggle.setAlignment(Pos.BOTTOM_LEFT);
        toggle.setText(CLIENT_BUTTON_TOGGLE.localize());

        Button reset = new Button();
        reset.setAlignment(Pos.BOTTOM_LEFT);
        reset.setText(CLIENT_BUTTON_RESET.localize());

        // set events
        // toggle button
        toggle.setOnAction(e -> {
            LOGGER.info(CLIENT_SCREEN_TOGGLE, this.screen.getId(),
                    (background.isSelected() ? background.getText() : ""));
            RestClient.toggleImageData(this.screen.getId(), background.isSelected());
            background.setSelected(false);
            e.consume();
        });

        // reset button
        reset.setOnAction(e -> {
            LOGGER.debug(CLIENT_SCREEN_RESET, this.screen.getId(),
                    (background.isSelected() ? background.getText() : ""));
            RestClient.clearImageData(this.screen.getId(), background.isSelected());
            background.setSelected(false);
            e.consume();
        });

        // drag and drop
        setOnDragOver(e -> {
            if (e.getGestureSource() != this && e.getDragboard().hasFiles()) {
                e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            e.consume();
        });

        // drag and drop
        setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            sendDataFromClipboard(db);
            e.setDropCompleted(db.hasFiles());
            e.consume();
        });

        // ctrl + v
        setOnKeyPressed(e -> {
            if (e.isControlDown() && KeyCode.V.equals(e.getCode())) {
                sendDataFromClipboard(Clipboard.getSystemClipboard());
            }

            e.consume();
        });

        // mouse events
        setOnMouseEntered(e -> {
            requestFocus();
            setSelected(true);
            e.consume();
        });

        setOnMouseExited(e -> {
            setSelected(false);
            e.consume();
        });

        // add children
        getChildren().add(id);
        getChildren().add(size);
        getChildren().add(background);
        getChildren().add(displayname);
        getChildren().add(toggle);
        getChildren().add(reset);
    }

    /**
     * Set or reset border depending on selection.
     * 
     * @param selected boolean
     */
    private void setSelected(boolean selected) {
        if (selected) {
            setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                    + "-fx-border-insets: 2;" + "-fx-border-radius: 4;" + "-fx-border-color: lightblue;");
        } else {
            if (screen.isMain()) {
                setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                        + "-fx-border-insets: 2;" + "-fx-border-radius: 4;" + "-fx-border-color: green;");
            } else {
                setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                        + "-fx-border-insets: 2;" + "-fx-border-radius: 4;" + "-fx-border-color: black;");
            }
        }
    }

    /**
     * Send any supported data from clipboard or dragboard to server.
     * 
     * @param clipboard Clipboard
     */
    private void sendDataFromClipboard(Clipboard clipboard) {
        try {
            if (clipboard.hasContent(DataFormat.IMAGE)) {
                sendImage(new URL(clipboard.getUrl()));
            } else if (clipboard.hasContent(DataFormat.FILES)) {
                for (final File file : clipboard.getFiles()) {
                    sendImage(file.toURI().toURL());
                }
            }
        } catch (IOException e) {
            LOGGER.error(CLIENT_DATA_TRANSFER_ERROR, e);
        }
    }

    /**
     * Send an image to the server.
     * 
     * @param url URL
     */
    private void sendImage(URL url) {
        if (url == null) {
            return;
        }

        String filename = url.getFile();
        String filetype = URLConnection.guessContentTypeFromName(filename);

        if (filetype == null || !filetype.startsWith("image/")) {
            LOGGER.error(CLIENT_DATA_TRANSFER_INVALID_TYPE, filetype, filename);
            return;
        }

        filename = urlToName(URLDecoder.decode(filename, StandardCharsets.UTF_8));
        LOGGER.info(CLIENT_DATA_TRANSFER, this.screen.getId(), filename, filetype);

        try (InputStream is = url.openStream()) {
            RestClient.setImageData(this.screen.getId(), displayname.isSelected() ? filename : null, is.readAllBytes(),
                    background.isSelected());
            background.setSelected(false);
        } catch (IOException e1) {
            LOGGER.error(CLIENT_DATA_TRANSFER_ERROR, e1);
        }
    }

    /**
     * Get name element from url without directory prefix and file type suffix.
     * 
     * @param url String
     * @return String
     */
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
