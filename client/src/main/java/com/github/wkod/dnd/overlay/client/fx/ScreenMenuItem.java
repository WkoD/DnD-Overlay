package com.github.wkod.dnd.overlay.client.fx;

import static com.github.wkod.dnd.overlay.client.fx.localization.Label.*;

import javafx.scene.control.CheckMenuItem;

public class ScreenMenuItem extends CheckMenuItem {

    /**
     * Associated screen box.
     */
    private final OlScreenBox screenbox;

    /**
     * Constructor.
     * 
     * @param screenbox OlScreenBox
     */
    public ScreenMenuItem(OlScreenBox screenbox) {
        super(CLIENT_SCREENS_SCREEN.localize(screenbox.getId()));
        this.screenbox = screenbox;

        setId(screenbox.getId());
        setSelected(true);

        setOnAction(e -> {
            boolean checked = isSelected();
            this.screenbox.setVisible(checked);
            this.screenbox.setManaged(checked);
            e.consume();
        });
    }
    
    /**
     * Set screen to visible.
     */
    public void enable() {
        setSelected(true);
        screenbox.setVisible(true);
        screenbox.setManaged(true);
    }
}
