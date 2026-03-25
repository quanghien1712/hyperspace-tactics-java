package com.game.shooting2DGame.movement;

import com.game.shooting2DGame.entity.DynamicEntity;
import com.game.shooting2DGame.utils.Vector2D;

import java.util.function.Supplier;

public class MoveToTarget implements MovementBehavior{
    private Supplier<Vector2D> target;
    private final double moveRadius, acceleration, drag;

    public MoveToTarget(double moveRadius, double acceleration, double drag) {
        this.moveRadius = moveRadius;
        this.acceleration = acceleration;
        this.drag = drag;
    }

    @Override
    public void move(DynamicEntity entity, double deltaTime) {
        if (target == null) return;
        Vector2D dir = target.get().subtract(entity.getPosition());
        double targetAngle = Math.atan2(dir.getY(), dir.getX()) + Math.PI / 2.0;
        entity.setTargetAngle(targetAngle);
        entity.updateRotation(deltaTime);
        if (dir.squareLength() > moveRadius * moveRadius) {
            entity.accelerate(acceleration);
        } else entity.dragged(drag);
        Vector2D direction = entity.getDirection().normalize();
        double x = entity.getPosition().getX() +
              direction.getX() * entity.getSpeed() * deltaTime;
        double y = entity.getPosition().getY() +
              direction.getY() * entity.getSpeed() * deltaTime;
        entity.setPosition(x, y);
    }

    public void setTarget(Supplier<Vector2D> supplier) {
        this.target = supplier;
    }
}
