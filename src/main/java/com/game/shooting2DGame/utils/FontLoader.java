package com.game.shooting2DGame.utils;

import java.awt.Font;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FontLoader {
    private static final Logger logger = Logger.getLogger(FontLoader.class.getName());

    private FontLoader() {
    }

    public static Font loadEthnocentricOrFallback(float size, String fallbackName, int fallbackStyle) {
        try (InputStream is = FontLoader.class.getResourceAsStream("/font/Ethnocentric.otf")) {
            if (is != null) {
                return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
            }
            logger.warning("Font resource not found: /font/Ethnocentric.otf");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to load Ethnocentric font", e);
        }
        return new Font(fallbackName, fallbackStyle, Math.round(size));
    }
}
