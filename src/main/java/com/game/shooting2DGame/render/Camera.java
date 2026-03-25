package com.game.shooting2DGame.render;

import com.game.shooting2DGame.config.GameConfig;
import com.game.shooting2DGame.entity.Player;
import com.game.shooting2DGame.utils.Vector2D;

public class Camera {
    private Vector2D position;
    private Vector2D targetPos;

    public Camera() {
        position = new Vector2D(0, 0);
        targetPos = new Vector2D(0, 0);
    }

    public void followPlayer(Player player, double deltaTime) {
        Vector2D playerPos = player.getPosition();
        double centerX = position.getX() + GameConfig.PANEL_WIDTH / 2.0;
        double centerY = position.getY() + GameConfig.PANEL_HEIGHT / 2.0;
        double deltaX = playerPos.getX() - centerX;
        double deltaY = playerPos.getY() - centerY;
        double targetOffsetX = 0, targetOffsetY = 0;
        if (Math.abs(deltaX) > GameConfig.DEAD_ZONE_WIDTH / 2.0) {
            if (deltaX > 0) targetOffsetX = deltaX - GameConfig.DEAD_ZONE_WIDTH / 2.0;
            else targetOffsetX = deltaX + GameConfig.DEAD_ZONE_WIDTH / 2.0;
        }

        if (Math.abs(deltaY) > GameConfig.DEAD_ZONE_HEIGHT / 2.0) {
            if (deltaY > 0) targetOffsetY = deltaY - GameConfig.DEAD_ZONE_HEIGHT / 2.0;
            else targetOffsetY = deltaY + GameConfig.DEAD_ZONE_HEIGHT / 2.0;
        }

        targetPos.copy(position.getX() + targetOffsetX, position.getY() + targetOffsetY);
        double lerpFactor = Math.min(1.0, GameConfig.CAMERA_SPEED * deltaTime);
        double newX = position.getX() + (targetPos.getX() - position.getX()) * lerpFactor;
        double newY = position.getY() + (targetPos.getY() - position.getY()) * lerpFactor;
        position.copy(newX, newY);
    }

    public Vector2D getScreenPos(Vector2D worldPos) {
        return worldPos.subtract(position);
    }

    public Vector2D getCameraPos() {
        return position;
    }

}
