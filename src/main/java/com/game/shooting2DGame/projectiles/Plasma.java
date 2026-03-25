package com.game.shooting2DGame.projectiles;

import com.game.shooting2DGame.movement.LinearMovement;

public class Plasma extends Projectile {
    private static final int DEFAULT_WIDTH = 30;
    private static final int DEFAULT_HEIGHT = 30;
    private static final int DEFAULT_DAMAGE = 20;
    private static final double DEFAULT_SPEED = 1000;


    public Plasma() {
        super(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.damage = DEFAULT_DAMAGE;
        movement = new LinearMovement();
        this.speed = DEFAULT_SPEED;
        this.projectileType = ProjectileType.PLASMA;
    }

    @Override
    public void update(double deltaTime) {
        move(deltaTime);
    }



}
