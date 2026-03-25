package com.game.shooting2DGame.entity;

import com.game.shooting2DGame.config.EnemyConfig;
import com.game.shooting2DGame.movement.OrbitMovement;
import com.game.shooting2DGame.weapon.RifleAssault;

public class OrbitEnemy extends Enemy {
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_HEIGHT = 50;
    private static final double MAX_SPEED = 80;
    private static final int DEFAULT_HEALTH = 150;
    private static final int DEFAULT_DAMAGE = 60;
    private static final double MOVE_RADIUS = 100;
    private static final int SCORE = 150;

    public OrbitEnemy() {
        super(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.health = DEFAULT_HEALTH;
        this.damage = DEFAULT_DAMAGE;
        this.maxHealth = DEFAULT_HEALTH;
        this.enemyType = EnemyType.ORBITING_ENEMY;
        this.moveRadius = MOVE_RADIUS;
        this.maxSpeed = MAX_SPEED;
        this.weapon = new RifleAssault();
        this.movement = new OrbitMovement(moveRadius, EnemyConfig.ACCELERATION, EnemyConfig.DRAG);
        this.defeatedScore = SCORE;
    }

    @Override
    public void setTarget(Damageable target) {
        ((OrbitMovement)movement).setTarget(target::getPos);
    }

}
