package com.github.wkod.dnd.overlay.server;

import com.sun.glass.ui.Screen;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * BackgroundStage.
 * @author cpawek
 * @version 1.0
 */
public class BackgroundStage extends Stage {
   
   private final Pane mPane;

   public BackgroundStage(final Screen pOverlayScreen) {
      initStyle(StageStyle.UNDECORATED);
      setX(pOverlayScreen.getX());
      setY(pOverlayScreen.getY());
      setWidth(pOverlayScreen.getWidth());
      setHeight(pOverlayScreen.getHeight());
      
      mPane = new Pane();
      mPane.setStyle("-fx-background-size: cover;");
      
      Scene bgscene = new Scene(mPane);

      setScene(bgscene);
      
      reset();
   }
   
   public void reset() {
      setOpacity(0.01);
      getScene().setFill(Color.TRANSPARENT);
   }
   
   public void setImage(final Image pImage) {
      setOpacity(1);
      mPane.setBackground(new Background(new BackgroundImage(pImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, true))));
   }
}
