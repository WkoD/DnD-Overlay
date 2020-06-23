package com.github.wkod.dnd.overlay.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.wkod.dnd.overlay.server.appconfig.Configuration;
import com.sun.glass.ui.Screen;

import javafx.scene.layout.Pane;

public class OverlayPane extends Pane {

   private final Screen mScreen;
   private final double mPosInterval;
   private final double mMaxPos = 10;
   private int mNextPos = 1;

   private final List<OverlayImage> mSlots = new ArrayList<>();
   private final int mSlotsMin;
   private final int mSlotsMax;

   public OverlayPane(final Screen pScreen) {
      mScreen = pScreen;
      mPosInterval = mScreen.getWidth() / mMaxPos;

      mSlotsMin = (int) (mScreen.getHeight() * 0.1);
      mSlotsMax = mScreen.getHeight() - (int) (mScreen.getHeight() * 0.1);
   }

   public double getVisibleWidth() {
      return mScreen.getWidth();
   }

   public double getVisibleHeight() {
      return mScreen.getHeight();
   }

   public void addImage(final OverlayImage pOverlayImage) {
      // set position
      pOverlayImage.setLayoutX(
            mNextPos++ * mPosInterval - (pOverlayImage.getBoundsInParent().getMinX() - pOverlayImage.getLayoutX()));
      pOverlayImage.setLayoutY(-pOverlayImage.getBoundsInParent().getMinY());

      if (mNextPos > mMaxPos - 2) {
         mNextPos = 1;
      }

      // add image to pane
      getChildren().add(pOverlayImage);
   }

   public void toSlot(final OverlayImage pOverlayImage) {
      // add image to slots
      mSlots.add(pOverlayImage);
      pOverlayImage.setSlotted(true);

      // update slots
      double slotsize = (mSlotsMax - mSlotsMin) / mSlots.size();

      for (int i = 0; i < mSlots.size(); ++i) {
         OverlayImage image = mSlots.get(i);

         image.setLayoutX(
               getVisibleWidth() - Configuration.getMinImageSize() - (image.getBoundsInParent().getMinX() - image.getLayoutX()));
         image.setLayoutY(mSlotsMin + (i * slotsize) - (image.getBoundsInParent().getMinY() - image.getLayoutY()));
         image.toFront();
      }
   }

   public void removeSlot(final OverlayImage pOverlayImage) {
      for (int i = mSlots.size() - 1; i >= 0; --i) {
         if (Objects.equals(mSlots.get(i).getId(), pOverlayImage.getId())) {
            mSlots.remove(i);
            System.out.println(pOverlayImage.getId());
         }
      }

      pOverlayImage.setSlotted(false);
   }
}
