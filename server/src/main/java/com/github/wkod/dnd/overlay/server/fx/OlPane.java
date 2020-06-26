package com.github.wkod.dnd.overlay.server.fx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.wkod.dnd.overlay.server.config.Configuration;

import javafx.scene.layout.Pane;

public class OlPane extends Pane {

    private final int width;
    private final int height;

    private final double posintervall;
    private final double posmax = 10;
    private int posnext = 1;

    private final List<OlImage> slotlist = new ArrayList<>();
    private final int slotmin;
    private final int slotmax;

    public OlPane(int width, int height) {
        this.width = width;
        this.height = height;
        posintervall = this.width / posmax;

        slotmin = (int) (this.height * 0.1);
        slotmax = this.height - (int) (this.height * 0.1);

        setBackground(null);
    }

    public double getVisibleWidth() {
        return width;
    }

    public double getVisibleHeight() {
        return height;
    }

    public void addImage(final OlImage image) {
        // set position
        image.setLayoutX(posnext++ * posintervall - (image.getBoundsInParent().getMinX() - image.getLayoutX()));
        image.setLayoutY(-image.getBoundsInParent().getMinY());

        if (posnext > posmax - 2) {
            posnext = 1;
        }

        // add image to pane
        getChildren().add(image);
    }

    public void toSlot(final OlImage image) {
        // add image to slots
        slotlist.add(image);
        image.setSlotted(true);

        // update slots
        double slotsize = (slotmax - slotmin) / slotlist.size();

        for (int i = 0; i < slotlist.size(); ++i) {
            OlImage ol = slotlist.get(i);

            ol.setLayoutX(getVisibleWidth() - Configuration.IMAGE_SIZE_MIN_VISIBLE.get()
                    - (ol.getBoundsInParent().getMinX() - ol.getLayoutX()));
            ol.setLayoutY(slotmin + (i * slotsize) - (ol.getBoundsInParent().getMinY() - ol.getLayoutY()));
            ol.toFront();
        }
    }

    public void removeSlot(final OlImage image) {
        for (int i = slotlist.size() - 1; i >= 0; --i) {
            if (Objects.equals(slotlist.get(i).getId(), image.getId())) {
                slotlist.remove(i);
                System.out.println(image.getId());
            }
        }

        image.setSlotted(false);
    }
}
