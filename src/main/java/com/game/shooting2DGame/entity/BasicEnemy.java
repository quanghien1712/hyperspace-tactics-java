package com.game.shooting2DGame.entity;


import com.game.shooting2DGame.config.EnemyConfig;
import com.game.shooting2DGame.movement.MoveToTarget;
import com.game.shooting2DGame.weapon.SmartGun;

public class BasicEnemy extends Enemy{
    private static final int DEFAULT_WIDTH = 60;
    private static final int DEFAULT_HEIGHT = 60;
    private static final double MAX_SPEED = 250;
    private static final int DEFAULT_HEALTH = 100;
    private static final int DEFAULT_DAMAGE = 50;
    private static final double MOVE_RADIUS = 700;
    private static final int SCORE = 100;

    public BasicEnemy() {
        super(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.health = DEFAULT_HEALTH;
        this.damage = DEFAULT_DAMAGE;
        this.maxHealth = DEFAULT_HEALTH;
        this.enemyType = EnemyType.BASIC_ENEMY;
        this.moveRadius = MOVE_RADIUS;
        this.maxSpeed = MAX_SPEED;
        this.weapon = new SmartGun();
        this.movement = new MoveToTarget(moveRadius, EnemyConfig.ACCELERATION, EnemyConfig.DRAG);
        this.defeatedScore = SCORE;
    }

    @Override
    public void setTarget(Damageable target) {
        ((MoveToTarget)movement).setTarget(target::getPos);
    }



}
