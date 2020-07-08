package com.github.wkod.dnd.overlay.server.fx;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OlStage extends Stage {
    
    private final int screenid;

    private final OlPane pane;

    private final OlBackground background;

    /**
     * Constructor.
     * 
     * @param screenid int
     * @param x        int
     * @param y        int
     * @param width    int
     * @param height   int
     */
    public OlStage(int screenid, int x, int y, int width, int height) {
        this.screenid = screenid;
        
        // basic configuration
        setAlwaysOnTop(true);
        initStyle(StageStyle.TRANSPARENT);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);

        StackPane screenPane = new StackPane();

        // background pane
        background = new OlBackground(this.screenid, x, y, width, height);
        background.setVisible(false);

        // data pane
        pane = new OlPane(this.screenid, x, y, width, height);

        screenPane.getChildren().add(background);
        screenPane.getChildren().add(pane);

        // set scene
        Scene scene = new Scene(screenPane);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
    }

    public void setBackground(String name, Image image) {
        if (image == null) {
            return;
        }

        if (!background.isVisible()) {
            background.setVisible(true);
        }

        background.set(name, image);
    }

    public void toggleBackground() {
        background.setVisible(!background.isVisible());
    }

    public void clearBackground() {
        background.clear();
        background.setVisible(false);
    }

    public void setImage(String name, Image image) {
        if (image == null) {
            return;
        }

        if (!pane.isVisible()) {
            pane.setVisible(true);
        }

        pane.addImage(name, image);
    }

    public void toogleImage() {
        pane.setVisible(!pane.isVisible());
    }

    public void clearImage() {
        pane.clear();
        pane.setVisible(false);
    }

}
