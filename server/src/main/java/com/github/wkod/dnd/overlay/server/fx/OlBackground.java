package com.github.wkod.dnd.overlay.server.fx;

import com.github.wkod.dnd.overlay.server.config.Configuration;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class OlBackground extends StackPane {

    private final Text lblname;

    /**
     * Constructor.
     * 
     * @param x      int
     * @param y      int
     * @param width  int
     * @param height int
     */
    public OlBackground(int x, int y, int width, int height) {
        // basic configuration
        setLayoutX(x);
        setLayoutY(y);
        setWidth(width);
        setHeight(height);

        // set pane
        setStyle("-fx-background-size: cover;");

        // add name label
        lblname = new Text("");
        lblname.setFont(new Font(Configuration.BACKGROUND_TEXT_SIZE.get()));
        lblname.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 1px;");
        lblname.maxWidth(Double.MAX_VALUE);
        lblname.maxHeight(Double.MAX_VALUE);

        StackPane.setAlignment(lblname, Pos.valueOf(Configuration.BACKGROUND_TEXT_POSITION.get()));
        getChildren().add(lblname);

        setOpacity(1);
    }

    /**
     * Set the background image resized to fill the screen with correct aspect
     * ratio.
     * 
     * @param name  String
     * @param image Image
     */
    public void set(String name, Image image) {
        // set name as label
        if (name != null) {
            lblname.setText(name);
        } else {
            lblname.setText("");
        }

        // set image
        if (Configuration.BACKGROUND_TRANSPARENCY.get()) {
            setBackground(
                    new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, true))));
        } else {
            BackgroundImage[] imageary = {
                    new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, true)) };
            BackgroundFill[] fillary = { new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY) };

            setBackground(new Background(fillary, imageary));
        }
    }

    public void clear() {
        setBackground(null);
    }
}
