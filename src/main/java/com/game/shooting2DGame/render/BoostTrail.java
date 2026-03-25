package com.game.shooting2DGame.render;

import com.game.shooting2DGame.config.PlayerConfig;
import com.game.shooting2DGame.utils.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class BoostTrail {
    public class TrailFrame {
        Vector2D position;
        double angle;
        long createdTime;
        TrailFrame(Vector2D position, double angle) {
            this.position = position;
            this.angle = angle;
            createdTime = System.currentTimeMillis();
        }
    }

    private static final long TRAIL_INTERVAL = 70; //ms
    private static final int MAX_TRAIL_LENGTH = 6;
    private static final int TRAIL_LIFETIME = 500;

    private LinkedList<TrailFrame> boostTrails = new LinkedList<>();
    private long lastTime = 0;

    public void addTrailFrame(Vector2D position, double angle) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= TRAIL_INTERVAL) {
            boostTrails.addFirst(new TrailFrame(position.copy(), angle));
            if (boostTrails.size() > MAX_TRAIL_LENGTH) {
                boostTrails.removeLast();
            }
            lastTime = currentTime;
        }
    }

    public void render(BufferedImage img, Camera camera, Graphics2D g2D, boolean haveOverlay) {
        if (boostTrails.isEmpty()) return;
        long currentTime = System.currentTimeMillis();
        if (!haveOverlay) {
            boostTrails.removeIf(frame -> currentTime - frame.createdTime > TRAIL_LIFETIME);
        }
        if (boostTrails.isEmpty()) return;
        AlphaComposite originalComposite = (AlphaComposite) g2D.getComposite();
        AffineTransform originalTransform = g2D.getTransform();
        int size = boostTrails.size();
        int spriteSize = PlayerConfig.SPRITE_SIZE;
        for (int i = 0; i < size; ++i) {
            TrailFrame frame = boostTrails.get(i);
            Vector2D screenPos = camera.getScreenPos(frame.position);
            float alpha = 0.4f * (1.0f - (float) i / size);
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2D.rotate(frame.angle, screenPos.getX(), screenPos.getY());
            g2D.drawImage(img, (int) screenPos.getX() - spriteSize / 2,
                  (int) screenPos.getY() - spriteSize / 2, spriteSize, spriteSize, null);
            g2D.setTransform(originalTransform);
        }
        g2D.setComposite(originalComposite);
    }
}
