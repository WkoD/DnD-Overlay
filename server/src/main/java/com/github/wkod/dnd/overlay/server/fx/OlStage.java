package com.github.wkod.dnd.overlay.server.fx;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OlStage extends Stage {

    private final OlPane pane;
    private final OlBackground background;

    /**
     * Constructor.
     * 
     * @param x      int
     * @param y      int
     * @param width  int
     * @param height int
     */
    public OlStage(int x, int y, int width, int height) {
        // basic configuration
        setAlwaysOnTop(true);
        initStyle(StageStyle.TRANSPARENT);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);

        // image pane
        pane = new OlPane(width, height);
        Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

        // background pane
        background = new OlBackground(x, y, width, height);
        background.hide();
    }

    /**
     * Sets the background image.
     * 
     * @param name  String
     * @param image Image
     */
    public void setBackground(String name, Image image) {
        if (image == null) {
            return;
        }

        if (!background.isShowing()) {
            background.show();
            pane.toFront();
        }

        background.setImage(image);
    }

    public void toggleBackground() {
        background.toggle();
    }

    public void clearBackground() {
        if (background.isShowing()) {
            background.resetImage();
            background.hide();
        }
    }

    public void setImage(String name, Image image) {
        pane.addImage(new OlImage(pane, name, image));
    }

    public void clearImage() {
        pane.getChildren().clear();
    }

}
