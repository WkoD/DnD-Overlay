package com.github.wkod.dnd.overlay.server.fx;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OlBackground extends Stage {

    private final Pane pane;
    
    private Image image = null;

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
        initStyle(StageStyle.UNDECORATED);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);

        // set pane
        pane = new Pane();
        pane.setStyle("-fx-background-size: cover;");
        Scene bgscene = new Scene(pane);
        setScene(bgscene);

        // translucent background that catches all mouse and touch events
//        setOpacity(0.01);
//        getScene().setFill(Color.TRANSPARENT);
    }

    /**
     * Set the background image resized to fill the screen with correct aspect
     * ratio.
     * 
     * @param image Image
     */
    public void setImage(final Image image) {
        this.image = image;
        
        setOpacity(1);
        pane.setBackground(
                new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, true))));
    }
    
    public void toggle() {
        if (isShowing()) {
            hide();
        } else if(image != null) {
            show();
            setImage(image);
        }
    }
    
    public void resetImage() {
        image = null;
        pane.setBackground(null);
    }
}
