package main.java.utils;

import java.awt.*;

public enum ColorsTemplate {
    BACKGROUND_COLOR(new Color(245, 245, 250)),
    BORDER_COLOR(new Color(180, 180, 200)),
    TITLE_TEXT_COLOR(new Color(1, 2, 17)),
    DESCRIPTION_BACKGROUND_COLOR(new Color(255, 255, 255)),
    BUTTON_BACKGROUND_COLOR(new Color(70, 130, 180)),
    BUTTON_TEXT_COLOR(Color.WHITE),
    BUTTON_HOVER_COLOR(new Color(125, 156, 181)),
    DARK_BACKGROUND_COLOR(new Color(40, 50, 60));

    private final Color color;

    ColorsTemplate(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}

