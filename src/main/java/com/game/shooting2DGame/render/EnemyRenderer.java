package com.game.shooting2DGame.render;

import com.game.shooting2DGame.entity.Enemy;
import com.game.shooting2DGame.entity.EnemyType;
import com.game.shooting2DGame.utils.ImageLoader;
import com.game.shooting2DGame.utils.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class EnemyRenderer {
    private Map<EnemyType, BufferedImage> images = new HashMap<>();

    public EnemyRenderer() {
        loadAnimation();
    }

    private void loadAnimation() {
        images.put(EnemyType.BASIC_ENEMY, ImageLoader.loadImage("/aircraft/Enemy1.png"));
        images.put(EnemyType.ORBITING_ENEMY, ImageLoader.loadImage("/aircraft/Enemy2.png"));
        images.put(EnemyType.DRONE, ImageLoader.loadImage("/aircraft/Drone.png"));
        images.put(EnemyType.BOSS, ImageLoader.loadImage("/aircraft/Boss.png"));
    }

    public void render(Enemy enemy, Camera camera, Graphics2D g2D) {
        EnemyType enemyType = enemy.getEnemyType();
        BufferedImage img = images.get(enemyType);
        Vector2D screenPos = camera.getScreenPos(enemy.getPosition());
        double angle = enemy.getCurrentAngle();
        AffineTransform oldTransform = g2D.getTransform();
        g2D.rotate(angle, (int)screenPos.getX(), (int)screenPos.getY());
        g2D.drawImage(img, (int)screenPos.getX() - enemy.getWidth() / 2,
              (int)screenPos.getY() - enemy.getHeight() / 2,
              enemy.getWidth(), enemy.getHeight(), null);
        g2D.setTransform(oldTransform);
    }
}
