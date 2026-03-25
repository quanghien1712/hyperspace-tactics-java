package com.game.shooting2DGame.movement;

import com.game.shooting2DGame.entity.DynamicEntity;
import com.game.shooting2DGame.utils.Vector2D;

public class LinearMovement implements MovementBehavior {
    public LinearMovement(){}

    @Override
    public void move(DynamicEntity entity, double deltaTime) {
        Vector2D normalizedDir = entity.getDirection().normalize();

        double x = entity.getPosition().getX() +
                   normalizedDir.getX() * entity.getSpeed() * deltaTime;
        double y = entity.getPosition().getY() +
                   normalizedDir.getY() * entity.getSpeed() * deltaTime;

        entity.setPosition(x, y);
    }

}
