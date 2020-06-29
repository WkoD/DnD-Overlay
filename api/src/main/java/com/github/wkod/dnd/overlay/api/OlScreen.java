package com.github.wkod.dnd.overlay.api;

import lombok.Data;

@Data
public class OlScreen {

    private int id;

    private int x;

    private int y;

    private int width;

    private int height;

    private boolean main = false;
}
