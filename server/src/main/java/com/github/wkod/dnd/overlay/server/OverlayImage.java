package com.github.wkod.dnd.overlay.server;

import java.util.Objects;

import com.github.wkod.dnd.overlay.server.appconfig.Configuration;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * OverlayImage.
 * 
 * @author cpawek
 * @version 1.0
 */
@Accessors(prefix = "m")
public class OverlayImage extends ImageView {
   
   private static int mCount = 0;

   private final OverlayPane mOverlayPane;

   private double mDragX;

   private double mDragY;
   
   @Getter
   @Setter
   private boolean mSlotted = false;
   
   @Getter
   private final String mName;

   private int mTouch1PointId;
   private boolean mTouch1Active;
   private Point2D mTouch1Position;

   private int mTouch2PointId;
   private boolean mTouch2Active;
   private Point2D mTouch2Position;

   public OverlayImage(final OverlayPane pOverlayPane, final String pName, final Image pImage) {
      super(removeAlpha(pImage));
      setId(String.valueOf(mCount++));
      mOverlayPane = pOverlayPane;

      mName = pName;
      
      // set properties
      setPreserveRatio(true);

      // set sizes to 1/5 of screen (height)
      defaultScale();

      // register Events
      setOnMousePressed(e -> {
         toFront();

         mDragX = getLayoutX() - e.getSceneX();
         mDragY = getLayoutY() - e.getSceneY();
      });

      setOnMouseDragged(e -> {
         setLayoutX(e.getSceneX() + mDragX);
         setLayoutY(e.getSceneY() + mDragY);
      });

      // touch events
      setOnTouchPressed(e -> {
         if (!mTouch1Active) {
            // first finger
            mTouch1Active = true;
            mTouch1PointId = e.getTouchPoint().getId();
            mTouch1Position = new Point2D(e.getTouchPoint().getSceneX(), e.getTouchPoint().getSceneY());
            
            toFront();
         } else if (!mTouch2Active) {
            // second finger
            mTouch2Active = true;
            mTouch2PointId = e.getTouchPoint().getId();
            mTouch2Position = new Point2D(e.getTouchPoint().getSceneX(), e.getTouchPoint().getSceneY());
         }

         e.consume();
      });

      setOnTouchMoved(e -> {
         if (mTouch1Active) {
            if (Objects.equals(mTouch1PointId, e.getTouchPoint().getId())) {
               // move first finger
               Point2D position = new Point2D(e.getTouchPoint().getSceneX(), e.getTouchPoint().getSceneY());

               setLayoutX(getLayoutX() + position.getX() - mTouch1Position.getX());
               setLayoutY(getLayoutY() + position.getY() - mTouch1Position.getY());

               // update finger position
               mTouch1Position = position;
               
               if (mSlotted) {
                  mOverlayPane.removeSlot(this);
               }

            } else if (Objects.equals(mTouch2PointId, e.getTouchPoint().getId())) {
               // move second finger
               Point2D position = new Point2D(e.getTouchPoint().getSceneX(), e.getTouchPoint().getSceneY());
               
               // update finger position
               mTouch2Position = position;
            }
         }

         e.consume();
      });

      setOnTouchReleased(e -> {
         if (Objects.equals(mTouch1PointId, e.getTouchPoint().getId())) {
            mTouch1Active = false;

            // keep part of picture on screen
            if (getBoundsInParent().getMinX() + Configuration.getMinImageSize() > mOverlayPane.getVisibleWidth()) {
               setLayoutX(mOverlayPane.getVisibleWidth() - Configuration.getMinImageSize() - (getBoundsInParent().getMinX() - getLayoutX()));
            } else if (getBoundsInParent().getMaxX() < Configuration.getMinImageSize()) {
               setLayoutX(Configuration.getMinImageSize() - getBoundsInParent().getWidth() - (getBoundsInParent().getMinX() - getLayoutX()));
            }

            if (getBoundsInParent().getMinY() + Configuration.getMinImageSize() > mOverlayPane.getVisibleHeight()) {
               setLayoutY(mOverlayPane.getVisibleHeight() - Configuration.getMinImageSize() - (getBoundsInParent().getMinY() - getLayoutY()));
            } else if (getBoundsInParent().getMaxY() < Configuration.getMinImageSize()) {
               setLayoutY(Configuration.getMinImageSize() - getBoundsInParent().getHeight() - (getBoundsInParent().getMinY() - getLayoutY()));
            }
         } else if (Objects.equals(mTouch2PointId, e.getTouchPoint().getId())) {
            mTouch2Active = false;
         }

         e.consume();
      });

      setOnZoom(e -> {
         setScale(getScale() * e.getZoomFactor());
         e.consume();
      });

      setOnRotate(e -> {
         setRotate(getRotate() + e.getAngle());
         e.consume();
      });
      
      setOnSwipeRight(e -> {
         defaultScale();
         setRotate(0);
         mOverlayPane.toSlot(this);
         e.consume();
      });
   }

   public void setScale(final double pScale) {
      setScaleX(pScale);
      setScaleY(pScale);
   }

   public double getScale() {
      return getScaleX();
   }
   
   private void defaultScale() {
      double defaultHeight = (mOverlayPane.getHeight() * 0.5);
      double relheightpercent = getImage().getHeight() / defaultHeight;
      double scalefactor = 1 / relheightpercent;

      setScale(scalefactor);
   }

   public static final Image removeAlpha(final Image pImage) {
      final int width = (int) pImage.getWidth();
      final int height = (int) pImage.getHeight();
      WritableImage result = new WritableImage(width, height);

      PixelReader reader = pImage.getPixelReader();
      PixelWriter writer = result.getPixelWriter();

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            // Retrieving the color of the pixel of the loaded image
            Color color = reader.getColor(x, y);

            if (color.getOpacity() > 0) {
               writer.setColor(x, y, new Color(color.getRed(), color.getGreen(), color.getBlue(), 1.0));
            } else {
               writer.setColor(x, y, Color.WHITE);
            }
         }
      }

      return result;
   }
}
