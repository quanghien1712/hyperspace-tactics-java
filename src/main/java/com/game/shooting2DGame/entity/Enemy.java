package com.game.shooting2DGame.entity;

import com.game.shooting2DGame.config.EnemyConfig;
import com.game.shooting2DGame.projectiles.Projectile;
import com.game.shooting2DGame.utils.Vector2D;
import com.game.shooting2DGame.weapon.Weapon;


public abstract class Enemy extends DynamicEntity implements Damageable, Poolable {
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected EnemyType enemyType;
    protected boolean isActive;
    protected double moveRadius;
    protected Weapon weapon;
    protected int defeatedScore;

    public Enemy(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.rotateSpeed = EnemyConfig.ROTATION_SPEED;
    }

    public EnemyType getEnemyType() {
        return this.enemyType;
    }

    @Override
    public void update(double deltaTime) {
        if (weapon != null) {
            weapon.update(deltaTime);
        }
        move(deltaTime);
    }

    public Projectile shoot() {
        Vector2D deltaPos = direction.normalize();
        deltaPos.multiplyInPlace(height / 2.0);
        Projectile projectile = weapon.fire(position.add(deltaPos), direction);
        if (projectile != null) {
            projectile.setOwner(Projectile.Owner.ENEMY);
            projectile.setCurrentAngle(currentAngle);
        }
        return projectile;
    }
    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDefeatedScore() {
        return defeatedScore;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
    }

    @Override
    public boolean isDead() {
        return health <= 0;
    }

    @Override
    public Vector2D getPos() {
        return getPosition();
    }

    @Override
    public void onRelease() {
        setPosition(0, 0);
        setDirection(0, 0);
        currentAngle = targetAngle = 0;
        isActive = false;
        health = maxHealth;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }

    public abstract void setTarget(Damageable target);

}
