package com.game.shooting2DGame.entity;

import com.game.shooting2DGame.config.GameConfig;
import com.game.shooting2DGame.utils.Vector2D;
import com.game.shooting2DGame.movement.MovementBehavior;

import java.awt.*;

public abstract class DynamicEntity {
    protected Vector2D position;
    protected int width;
    protected int height;
    protected Rectangle bound;
    protected MovementBehavior movement;
    protected Vector2D direction;
    protected double speed, rotateSpeed;
    protected double maxSpeed;
    protected double currentAngle; // move angle, not view angle
    protected double targetAngle;

    public abstract void update(double deltaTime);

    public DynamicEntity(double x, double y, int width, int height) {
        this.position = new Vector2D(x, y);
        this.width = width;
        this.height = height;
        this.bound = new Rectangle((int)x - width / 2, (int)y - height / 2, width, height);
        this.direction = new Vector2D(0, 0);
    }

    public void updateRotation(double deltaTime) {
        double angleDiff = targetAngle - currentAngle;
        while (angleDiff > Math.PI) angleDiff -= 2 * Math.PI;
        while (angleDiff < -Math.PI) angleDiff += 2 * Math.PI;
        if (Math.abs(angleDiff) < GameConfig.ROTATION_THRESHOLD) {
            currentAngle = targetAngle;
        } else {
            currentAngle += angleDiff * rotateSpeed * deltaTime;
        }
        double moveAngle = currentAngle - Math.PI / 2.0;
        double dx = Math.cos(moveAngle);
        double dy = Math.sin(moveAngle);
        setDirection(dx, dy);
    }

    public Rectangle getBound() {
        return this.bound;
    }

    public void setBoundPos(Vector2D screenPos) {
        bound.setLocation((int)screenPos.getX() - width / 2, (int)screenPos.getY() - height / 2);
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public void setPosition(Vector2D newPos) {
        position.copy(newPos);
    }

    public void setPosition(double x, double y) {
        position.copy(x, y);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void move(double deltaTime) {
        this.movement.move(this, deltaTime);
    }

    public void setCurrentAngle(double angle) {
        currentAngle = angle;
    }

    public void setTargetAngle(double targetAngle) {
        this.targetAngle = targetAngle;
    }

    public double getCurrentAngle() {
        return currentAngle;
    }

    public double getSpeed() {
        return this.speed;
    }

    public Vector2D getDirection() {
        return direction;
    }

    public void accelerate(double deltaSpeed) {
        speed += deltaSpeed;
        if (speed > maxSpeed) speed = maxSpeed;
    }

    public void dragged(double deltaSpeed) {
        speed -= deltaSpeed;
        if (speed <= 0) speed = 0;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDirection(Vector2D direction) {
        this.direction.copy(direction);
    }

    public void setDirection(double x, double y) {
        direction.copy(x, y);
    }
}
