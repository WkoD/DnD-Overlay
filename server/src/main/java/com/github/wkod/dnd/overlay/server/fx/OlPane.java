package com.github.wkod.dnd.overlay.server.fx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.wkod.dnd.overlay.server.config.Configuration;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class OlPane extends Pane {

    private double posx;
    private double posy;

    private final List<OlImageStack> slotlist = new ArrayList<>();
    private final int slotmin;
    private final int slotmax;

    /**
     * Constructor.
     * 
     * @param x      int
     * @param y      int
     * @param width  int
     * @param height int
     */
    public OlPane(int x, int y, int width, int height) {
        setLayoutX(x);
        setLayoutY(y);
        setWidth(width);
        setHeight(height);
        
        posx = 0;
        posy = 0;

        slotmin = (int) (getHeight() * 0.1);
        slotmax = (int) getHeight() - (int) (getHeight() * 0.1);

        setBackground(null);
    }

    public void addImage(String name, Image image) {
        OlImageStack stack = new OlImageStack(this, name, image);

        // check width boundaries
        if (posx > 1 && (posx + stack.getBoundsInParent().getWidth()) > getWidth()) {
            // set position to next column
            posx = 0;
            posy += stack.getBoundsInParent().getHeight();
            
            // check height boundaries
            if (posy > 1 && (posy + stack.getBoundsInParent().getHeight()) > getHeight()) {
                posy = 0;
            }
        }
        
        stack.setLayoutX(posx);
        stack.setLayoutY(posy);
        
        posx += stack.getBoundsInParent().getWidth();

        // add stack to pane
        getChildren().add(stack);
    }

    public void toSlot(final OlImageStack stack) {
        // add image to slots
        slotlist.add(stack);
        stack.setSlotted(true);

        // update slots
        double slotsize = (slotmax - slotmin) / slotlist.size();

        for (int i = 0; i < slotlist.size(); ++i) {
            OlImageStack ol = slotlist.get(i);

            ol.setLayoutX(getWidth() - Configuration.IMAGE_SIZE_MIN_VISIBLE.get());
            ol.setLayoutY(slotmin + (i * slotsize));
            ol.toFront();
        }
    }

    public void removeSlot(final OlImageStack stack) {
        for (int i = slotlist.size() - 1; i >= 0; --i) {
            if (Objects.equals(slotlist.get(i).getId(), stack.getId())) {
                slotlist.remove(i);
            }
        }

        stack.setSlotted(false);
    }

    public void clear() {
        getChildren().clear();
        posx = 0;
        posy = 0;
    }
}
