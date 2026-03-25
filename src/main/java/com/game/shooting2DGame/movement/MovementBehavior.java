package com.game.shooting2DGame.movement;

import com.game.shooting2DGame.entity.DynamicEntity;

public interface MovementBehavior {
    void move(DynamicEntity entity, double deltaTime);
}
