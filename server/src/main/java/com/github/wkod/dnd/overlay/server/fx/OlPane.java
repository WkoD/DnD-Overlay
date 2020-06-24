package com.github.wkod.dnd.overlay.server.fx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.wkod.dnd.overlay.server.config.Configuration;

import javafx.scene.layout.Pane;

public class OlPane extends Pane {

    private final int width;
    private final int height;
    private final double mPosInterval;
    private final double mMaxPos = 10;
    private int mNextPos = 1;

    private final List<OlImage> mSlots = new ArrayList<>();
    private final int mSlotsMin;
    private final int mSlotsMax;

    public OlPane(int width, int height) {
        this.width = width;
        this.height = height;
        mPosInterval = this.width / mMaxPos;

        mSlotsMin = (int) (this.height * 0.1);
        mSlotsMax = this.height - (int) (this.height * 0.1);

        setBackground(null);
    }

    public double getVisibleWidth() {
        return width;
    }

    public double getVisibleHeight() {
        return height;
    }

    public void addImage(final OlImage pOverlayImage) {
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

    public void toSlot(final OlImage pOverlayImage) {
        // add image to slots
        mSlots.add(pOverlayImage);
        pOverlayImage.setSlotted(true);

        // update slots
        double slotsize = (mSlotsMax - mSlotsMin) / mSlots.size();

        for (int i = 0; i < mSlots.size(); ++i) {
            OlImage image = mSlots.get(i);

            image.setLayoutX(getVisibleWidth() - Configuration.MIN_IMAGE_SIZE.get()
                    - (image.getBoundsInParent().getMinX() - image.getLayoutX()));
            image.setLayoutY(mSlotsMin + (i * slotsize) - (image.getBoundsInParent().getMinY() - image.getLayoutY()));
            image.toFront();
        }
    }

    public void removeSlot(final OlImage pOverlayImage) {
        for (int i = mSlots.size() - 1; i >= 0; --i) {
            if (Objects.equals(mSlots.get(i).getId(), pOverlayImage.getId())) {
                mSlots.remove(i);
                System.out.println(pOverlayImage.getId());
            }
        }

        pOverlayImage.setSlotted(false);
    }
}
