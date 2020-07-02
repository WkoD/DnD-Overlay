package com.github.wkod.dnd.overlay.client.fx;

import static com.github.wkod.dnd.overlay.api.localization.Messages.*;
import static com.github.wkod.dnd.overlay.client.fx.localization.Label.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.cal10n.LocLogger;

import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.api.exception.OlException;
import com.github.wkod.dnd.overlay.client.rest.Sender;
import com.github.wkod.dnd.overlay.util.Utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class OlScreenBox extends VBox {

    private static final LocLogger LOGGER = Utils.getLogger(OlScreenBox.class);

    /**
     * Associated screen.
     */
    private final OlScreen screen;

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
        CheckBox background = new CheckBox();
        background.setAlignment(Pos.BOTTOM_LEFT);
        background.setText(CLIENT_CHECKBOX_BACKGROUND.localize());
        background.setIndeterminate(false);
        background.setSelected(false);

        CheckBox displayname = new CheckBox();
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
            Sender.toggleImageData(this.screen.getId(), background.isSelected());
            background.setSelected(false);
            e.consume();
        });

        // reset button
        reset.setOnAction(e -> {
            LOGGER.debug(CLIENT_SCREEN_RESET, this.screen.getId(),
                    (background.isSelected() ? background.getText() : ""));
            Sender.clearImageData(this.screen.getId(), background.isSelected());
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
            e.setDropCompleted(db.hasFiles());

            for (final File file : db.getFiles()) {
                String filename = urlToName(file.getName());
                LOGGER.info(CLIENT_DATA_TRANSFER, this.screen.getId(), filename,
                        (background.isSelected() ? background.getText() : ""));
                try (FileInputStream fis = new FileInputStream(file)) {
                    Sender.setImageData(this.screen.getId(), displayname.isSelected() ? filename : null,
                            imageToByte(new Image(fis)), background.isSelected());
                    background.setSelected(false);
                } catch (IOException | OlException e1) {
                    LOGGER.error(CLIENT_DATA_TRANSFER_ERROR, e1);
                }
            }

            e.consume();
        });

        // ctrl + v
        setOnKeyPressed(e -> {
            if (e.isControlDown() && KeyCode.V.equals(e.getCode())) {
                Clipboard cb = Clipboard.getSystemClipboard();

                try {
                    if (cb.hasContent(DataFormat.IMAGE)) {
                        String filename = urlToName(cb.getUrl());
                        LOGGER.info(CLIENT_DATA_TRANSFER, this.screen.getId(), filename,
                                (background.isSelected() ? background.getText() : ""));
                        Sender.setImageData(this.screen.getId(), displayname.isSelected() ? filename : null,
                                imageToByte(cb.getImage()), background.isSelected());
                        background.setSelected(false);
                    }
                } catch (IOException | OlException e1) {
                    LOGGER.error(CLIENT_DATA_TRANSFER_ERROR, e1);
                }
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
                    + "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: lightblue;");
        } else {
            if (screen.isMain()) {
                setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                        + "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: green;");
            } else {
                setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                        + "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: black;");
            }
        }
    }

    /**
     * Transform JavaFX image to byte array.
     * 
     * @param image Image
     * @return byte[]
     * @throws IOException Exception
     * @throws OlException Exception
     */
    private static byte[] imageToByte(Image image) throws IOException, OlException {
        BufferedImage bimage = SwingFXUtils.fromFXImage(image, null);

        if (bimage == null) {
            throw new OlException(CLIENT_DATA_INVALID_IMAGE);
        }

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            ImageIO.write(bimage, "gif", stream);
            return stream.toByteArray();
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
