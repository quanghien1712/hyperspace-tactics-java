package com.game.shooting2DGame.projectiles;

import com.game.shooting2DGame.config.PlayerConfig;
import com.game.shooting2DGame.utils.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.function.Supplier;

public class Laser extends Projectile{
    private enum Phase { GROWING, HOLDING, SHRINKING }

    private Supplier<Double> shootAngle;
    private Supplier<Vector2D> shootPos;
    private Phase phase = Phase.GROWING;
    private double progress = 0.0;
    private double holdTime = 0.0;

    private static final double MAX_WIDTH = 30.0;
    private static final double GROW_RATE = 1.08;
    private static final double SHRINK_RATE = 0.60;
    private static final double HOLD_TIME = 1.67;
    private static final int LENGTH = 3000;
    private static final int DAMAGE = 20;
    private Shape collisionShape = new Rectangle2D.Double();

    public Laser() {
        super(0, 0, 1, 1);
        this.projectileType = ProjectileType.LASER;
        this.damage = DAMAGE;
    }

    @Override
    public void update(double deltaTime) {
        if (shootAngle == null || shootPos == null) return;
        setCurrentAngle(shootAngle.get());
        double beamAngle = currentAngle - Math.PI / 2.0;
        Vector2D deltaPos = new Vector2D(Math.cos(beamAngle), Math.sin(beamAngle));
        deltaPos.multiplyInPlace(PlayerConfig.SPRITE_SIZE / 2.0);
        setPosition(shootPos.get().add(deltaPos));
        switch (phase) {
            case GROWING -> {
                progress += GROW_RATE * deltaTime;
                if (progress >= 1.0) {
                    progress = 1.0;
                    phase = Phase.HOLDING;
                    holdTime = 0.0;
                }
            }
            case HOLDING -> {
                holdTime += deltaTime;
                if (holdTime >= HOLD_TIME) {
                    phase = Phase.SHRINKING;
                }
            }
            case SHRINKING -> {
                progress -= SHRINK_RATE * deltaTime;
                if (progress <= 0.0) {
                    progress = 0.0;
                    setActive(false);
                }
            }
        }
    }

    public double getRenderWidth() {
        double eased = progress * progress * progress;
        return Math.max(0.2, MAX_WIDTH * eased);
    }

    public int getLength() {
        return LENGTH;
    }

    @Override
    public void onRelease() {
        super.onRelease();
        phase = Phase.GROWING;
        progress = 0;
        holdTime = 0;
        this.shootAngle = null;
        this.shootPos = null;
        this.collisionShape = new Rectangle2D.Double();
    }

    @Override
    public Shape getCollisionShape() {
        return collisionShape;
    }

    public void setShootAngle(Supplier<Double> shootAngle){
        this.shootAngle = shootAngle;
    }

    public void setShootPos(Supplier<Vector2D> shootPos) {
        this.shootPos = shootPos;
    }

    public void updateScreenBound(Vector2D screenPos) {
        if (!isActive()) {
            collisionShape = new Rectangle2D.Double();
            bound = new Rectangle();
            return;
        }
        double dw = getRenderWidth();
        double halfH = Math.max(1.0, dw * 1.4 / 2.0);
        Rectangle2D rect = new Rectangle2D.Double(0, -halfH, LENGTH, halfH * 2);
        AffineTransform at = AffineTransform.getTranslateInstance(screenPos.getX(), screenPos.getY());
        at.rotate(currentAngle - Math.PI / 2.0);
        collisionShape = at.createTransformedShape(rect);
    }

}
