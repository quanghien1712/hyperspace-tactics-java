package com.game.shooting2DGame.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageLoader {
    private static final Logger logger = Logger.getLogger(ImageLoader.class.getName());
    private static final Map<String, BufferedImage> imageCache = new ConcurrentHashMap<>();

    private ImageLoader() {}

    public static BufferedImage loadImage(String path) {
        return imageCache.computeIfAbsent(path, ImageLoader::loadImageInternal);
    }

    private static BufferedImage loadImageInternal(String path) {
        try (InputStream is = ImageLoader.class.getResourceAsStream(path)) {
            if (is == null) {
                logger.warning("Resource not found: " + path);
                return null;
            }
            BufferedImage img = ImageIO.read(is);
            if (img == null) {
                logger.warning("Could not decode image: " + path);
            }
            return img;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load image" + path, e);
            return null;
        }
    }
}
