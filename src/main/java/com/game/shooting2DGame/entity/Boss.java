package com.game.shooting2DGame.entity;

import com.game.shooting2DGame.config.EnemyConfig;
import com.game.shooting2DGame.movement.MoveToTarget;
import com.game.shooting2DGame.weapon.RifleAssault;

public class Boss extends Enemy{
    private static final int DEFAULT_WIDTH = 300 ;
    private static final int DEFAULT_HEIGHT = 240;
    private static final double MAX_SPEED = 50;
    private static final int DEFAULT_HEALTH = 5000;
    private static final int DEFAULT_DAMAGE = 200;
    private static final double MOVE_RADIUS = 800;
    private static final int SCORE = 5000;

    public Boss() {
        super(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.health = DEFAULT_HEALTH;
        this.damage = DEFAULT_DAMAGE;
        this.maxHealth = DEFAULT_HEALTH;
        this.enemyType = EnemyType.BOSS;
        this.moveRadius = MOVE_RADIUS;
        this.maxSpeed = MAX_SPEED;
        this.weapon = new RifleAssault();
        this.movement = new MoveToTarget(moveRadius, EnemyConfig.ACCELERATION, EnemyConfig.DRAG);
        this.defeatedScore = SCORE;
    }

    @Override
    public void setTarget(Damageable target) {
        ((MoveToTarget)movement).setTarget(target::getPos);
    }
}
