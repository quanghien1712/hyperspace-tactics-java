package com.game.shooting2DGame.weapon;

import com.game.shooting2DGame.projectiles.Projectile;
import com.game.shooting2DGame.utils.Vector2D;


public abstract class Weapon {
    private final double fireInterval;
    private double cooldownRemaining;

    public Weapon(double fireRate) {
        this.fireInterval = (fireRate > 0) ? (1.0 / fireRate) : 0.0;
        this.cooldownRemaining = fireInterval;
    }

    public abstract Projectile createProjectile(Vector2D playerPos, Vector2D aimDirection);

    public void update(double deltaTime) {
        cooldownRemaining = Math.max(0.0, cooldownRemaining - deltaTime);
    }

    public boolean canFire() {
        return cooldownRemaining <= 0.0;
    }

    public Projectile fire(Vector2D playerPos, Vector2D aimDirection) {
        if (!canFire()) {
            return null;
        }
        cooldownRemaining = fireInterval;
        return createProjectile(playerPos, aimDirection);
    }

    public double getCooldownRemaining() {
        return cooldownRemaining;
    }

    public double getFireInterval() {
        return fireInterval;
    }
}
