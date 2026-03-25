package com.game.shooting2DGame.projectiles;

import com.game.shooting2DGame.entity.Damageable;
import com.game.shooting2DGame.movement.MoveToTarget;

public class HomingBullet extends Projectile{
    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 60;
    private static final int DEFAULT_DAMAGE = 5;
    private static final double DEFAULT_SPEED = 260;
    private static final double ROTATION_SPEED = 1.4;

    private Damageable target;

    public HomingBullet() {
        super(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.damage = DEFAULT_DAMAGE;
        this.rotateSpeed = ROTATION_SPEED;
        this.speed = DEFAULT_SPEED;
        this.maxSpeed = DEFAULT_SPEED;
        this.projectileType = ProjectileType.HOMING_BULLET;
        movement = new MoveToTarget(0, 2, 0);
    }

    @Override
    public void update(double deltaTime) {
        if (target != null && !target.isDead()) {
            move(deltaTime);
        }
    }

    public void setTarget(Damageable target) {
        this.target = target;
        ((MoveToTarget)movement).setTarget(() -> target.getPos());
    }

    @Override
    public void onRelease() {
        super.onRelease();
        this.target = null;
    }
}
