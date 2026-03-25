package com.game.shooting2DGame.render;

import com.game.shooting2DGame.projectiles.Laser;
import com.game.shooting2DGame.projectiles.Projectile;
import com.game.shooting2DGame.projectiles.ProjectileType;
import com.game.shooting2DGame.utils.ImageLoader;
import com.game.shooting2DGame.utils.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ProjectileRenderer {
    private Map<ProjectileType, BufferedImage> images = new HashMap<>();
    private final LaserRenderer laserRenderer = new LaserRenderer();
    public ProjectileRenderer() {
        loadImage();
    }

    private void loadImage() {
        images.put(ProjectileType.PLASMA, ImageLoader.loadImage("/bullet/plasma.png"));
        images.put(ProjectileType.HOMING_BULLET,ImageLoader.loadImage("/bullet/homingbullet.png"));
        images.put(ProjectileType.NORMAL_BULLET,ImageLoader.loadImage("/bullet/normalbullet.png"));
    }


    public void render(Projectile projectile, Camera camera, Graphics2D g2D) {
        ProjectileType projectileType = projectile.getProjectileType();
        if (projectileType == ProjectileType.LASER) {
            laserRenderer.render((Laser)projectile, camera, g2D);
            return;
        }
        BufferedImage img = images.get(projectileType);
        int width = projectile.getWidth();
        int height = projectile.getHeight();
        Vector2D screenPos = camera.getScreenPos(projectile.getPosition());
        AffineTransform oldTransform = g2D.getTransform();
        double angle = projectile.getCurrentAngle();
        g2D.rotate(angle, screenPos.getX(), screenPos.getY());
        g2D.drawImage(img, (int)screenPos.getX() - width / 2, (int)screenPos.getY() - height / 2,
              width, height, null);
        g2D.setTransform(oldTransform);
    }

}
