package com.game.shooting2DGame.projectiles;

import com.game.shooting2DGame.entity.Damageable;
import com.game.shooting2DGame.entity.DynamicEntity;
import com.game.shooting2DGame.entity.Poolable;

import java.awt.*;

public abstract class Projectile extends DynamicEntity implements Poolable {
    public enum Owner {
        PLAYER,
        ENEMY,
        NONE
    }

    protected int damage;
    protected ProjectileType projectileType;
    protected boolean isActive;
    protected Owner owner;

    public Projectile(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

    public void onHit(Damageable target) {
        target.takeDamage(damage);
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }


    @Override
    public void onRelease() {
        setPosition(0, 0);
        setDirection(0, 0);
        isActive = false;
        currentAngle = 0;
        targetAngle = 0;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Owner getOwner() {
        return owner;
    }

    public Shape getCollisionShape() {
        return getBound();
    }


}
