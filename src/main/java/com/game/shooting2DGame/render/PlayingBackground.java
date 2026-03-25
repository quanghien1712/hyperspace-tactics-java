package com.game.shooting2DGame.render;

import com.game.shooting2DGame.config.GameConfig;
import com.game.shooting2DGame.utils.ImageLoader;
import com.game.shooting2DGame.utils.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayingBackground {
    private BufferedImage img;
    private static final int IMAGE_SIZE = 1024;

    public PlayingBackground(){
        img = ImageLoader.loadImage("/background/Green_Nebula_02-1024x1024.png");
    }

    public void render(Graphics2D g2D, Camera camera) {
        Vector2D cameraPos = camera.getCameraPos();
        int offsetX = -(int)cameraPos.getX() % IMAGE_SIZE;
        int offsetY = -(int)cameraPos.getY() % IMAGE_SIZE;
        if (offsetX >= 0) offsetX -= IMAGE_SIZE;
        if (offsetY >= 0) offsetY -= IMAGE_SIZE;
        int tilesX = GameConfig.PANEL_WIDTH / IMAGE_SIZE + 2;
        int tilesY = GameConfig.PANEL_HEIGHT / IMAGE_SIZE + 2;
        for (int y = 0; y < tilesY; ++y) {
            for (int x = 0; x < tilesX; ++x) {
                int drawX = offsetX + x * IMAGE_SIZE;
                int drawY = offsetY + y * IMAGE_SIZE;
                g2D.drawImage(img, drawX, drawY, null);
            }
        }
    }
}
