package com.game.shooting2DGame.projectiles;

import com.game.shooting2DGame.movement.LinearMovement;

public class NormalBullet extends Projectile {

    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 40;
    private static final int DEFAULT_DAMAGE = 15;
    private static final double DEFAULT_SPEED = 700;


    public NormalBullet() {
        super(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.damage = DEFAULT_DAMAGE;
        movement = new LinearMovement();
        this.speed = DEFAULT_SPEED;
        this.projectileType = ProjectileType.NORMAL_BULLET;
    }


    @Override
    public void update(double deltaTime) {
        move(deltaTime);
    }
}
