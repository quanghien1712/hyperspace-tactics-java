package com.game.shooting2DGame.entity;

import com.game.shooting2DGame.config.EnemyConfig;
import com.game.shooting2DGame.movement.MoveToTarget;

public class Drone extends Enemy{
    private static final int DEFAULT_WIDTH = 30;
    private static final int DEFAULT_HEIGHT = 30;
    private static final double MAX_SPEED = 350;
    private static final int DEFAULT_HEALTH = 200;
    private static final int DEFAULT_DAMAGE = 0;
    private static final double MOVE_RADIUS = 0;
    private static final int SCORE = 200;

    public Drone() {
        super(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.health = DEFAULT_HEALTH;
        this.damage = DEFAULT_DAMAGE;
        this.maxHealth = DEFAULT_HEALTH;
        this.enemyType = EnemyType.DRONE;
        this.moveRadius = MOVE_RADIUS;
        this.maxSpeed = MAX_SPEED;
        this.movement = new MoveToTarget(moveRadius, EnemyConfig.ACCELERATION, EnemyConfig.DRAG);
        this.defeatedScore = SCORE;
    }

    @Override
    public void setTarget(Damageable target) {
        ((MoveToTarget)movement).setTarget(target::getPos);
    }
}
