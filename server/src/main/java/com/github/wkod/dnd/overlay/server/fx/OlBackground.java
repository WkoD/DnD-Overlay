package com.github.wkod.dnd.overlay.server.fx;

import com.github.wkod.dnd.overlay.server.config.Configuration;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OlBackground extends Stage {

    private final StackPane pane;
    private final Text lblname;
    
    private String name = null;

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
        pane = new StackPane();
        pane.setStyle("-fx-background-size: cover;");
        Scene bgscene = new Scene(pane);
        setScene(bgscene);
        
        // add name label
        lblname = new Text("");
        lblname.setFont(new Font(Configuration.BACKGROUND_TEXT_SIZE.get()));
        lblname.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 1px;");
        lblname.maxWidth(Double.MAX_VALUE);
        lblname.maxHeight(Double.MAX_VALUE);
        
        StackPane.setAlignment(lblname, Pos.BOTTOM_CENTER);
        pane.getChildren().add(lblname);

        // translucent background that catches all mouse and touch events
//        setOpacity(0.01);
//        getScene().setFill(Color.TRANSPARENT);
    }

    /**
     * Set the background image resized to fill the screen with correct aspect
     * ratio.
     * 
     * @param name  String
     * @param image Image
     */
    public void setImage(String name, Image image) {
        this.name = name;
        this.image = image;

        setOpacity(1);
        
        // set name as label
        if (this.name != null) {
            lblname.setText(this.name);
        } else {
            lblname.setText("");
        }
        
        // set image
        pane.setBackground(
                new Background(new BackgroundImage(this.image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, true))));
    }

    public void toggle() {
        if (isShowing()) {
            hide();
        } else if (image != null) {
            show();
            setImage(name, image);
        }
    }

    public void resetImage() {
        name = null;
        image = null;
        pane.setBackground(null);
        hide();
    }
}
