package com.bensiebert.codeup.cws.constants;

import java.awt.*;

public class Fonts {

    public static final Integer FONT_SIZE = 18;
    public static final Integer FONT_SIZE_TITLE = 26;
    public static final String DEFAULT_FONT = "Arial";

    public static final Font TEXT_FONT = new Font(DEFAULT_FONT, Font.PLAIN, FONT_SIZE);
    public static final Font TITLE_FONT = new Font(DEFAULT_FONT, Font.BOLD, FONT_SIZE_TITLE);
    public static final Font SUBTITLE_FONT = new Font(DEFAULT_FONT, Font.BOLD, 16);

    public static Font getFont(Integer size) {
        return new Font(DEFAULT_FONT, Font.PLAIN, size);
    }

    public static Font getTitleFont(Integer size) {
        return new Font(DEFAULT_FONT, Font.BOLD, size);
    }

    public static Font getFont(String font, Integer size) {
        return new Font(font, Font.PLAIN, size);
    }

}
