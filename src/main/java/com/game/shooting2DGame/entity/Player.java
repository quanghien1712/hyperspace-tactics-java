package com.game.shooting2DGame.entity;

import com.game.shooting2DGame.projectiles.Projectile;
import com.game.shooting2DGame.sound.SoundConstants;
import com.game.shooting2DGame.sound.SoundManager;
import com.game.shooting2DGame.state.PlayerState;
import com.game.shooting2DGame.config.PlayerConfig;
import com.game.shooting2DGame.movement.LinearMovement;
import com.game.shooting2DGame.utils.Vector2D;
import com.game.shooting2DGame.weapon.LaserGun;
import com.game.shooting2DGame.weapon.PlasmaGun;
import com.game.shooting2DGame.weapon.Weapon;


public class Player extends DynamicEntity implements Damageable {
    private Weapon weapon;
    private final Weapon laserGun;
    private final Weapon plasmaGun;

    private int health;
    private PlayerState state;

    private double viewAngle = 0;
    private double trailTimer = 0;
    private double boostCooldown = 0;
    private double boostTimer = 0;
    private int score, displayScore;


    public Player(double x, double y) {
        super(x, y, PlayerConfig.DEFAULT_WIDTH, PlayerConfig.DEFAULT_HEIGHT);
        this.speed = 0;
        this.rotateSpeed = PlayerConfig.ROTATION_SPEED;
        this.maxSpeed = PlayerConfig.MAX_SPEED;
        this.health = PlayerConfig.MAX_HEALTH;
        this.movement = new LinearMovement();
        state = PlayerState.NORMAL;
        this.plasmaGun = new PlasmaGun();
        this.laserGun = new LaserGun();
        weapon = plasmaGun;
    }

    @Override
    public void update(double deltaTime) {
        laserGun.update(deltaTime);
        plasmaGun.update(deltaTime);
        updateScore();
        boostCooldown = Math.max(0, boostCooldown - deltaTime);
        trailTimer = Math.max(0, trailTimer - deltaTime);
        updateRotation(deltaTime);
        if (state == PlayerState.BOOSTING) {
            boost(deltaTime);
        }
        if (state == PlayerState.BOOST_DECAY) {
            boostDecay(deltaTime);
        }
        move(deltaTime);
    }

    public void startBoost() {
        setState(PlayerState.BOOSTING);
        trailTimer = PlayerConfig.TRAIL_DURATION;
        boostCooldown = PlayerConfig.BOOST_COOLDOWN;
    }

    private void boost(double deltaTime) {
        boostTimer += deltaTime;
        setSpeed(PlayerConfig.BOOST_SPEED);
        if (boostTimer >= PlayerConfig.BOOST_DURATION) {
            setState(PlayerState.BOOST_DECAY);
            boostTimer = 0;
        }
    }

    private void boostDecay(double deltaTime) {
        speed -= PlayerConfig.DECAY_RATE * deltaTime;
        if (speed <= PlayerConfig.MAX_SPEED) {
            setState(PlayerState.NORMAL);
            speed = PlayerConfig.MAX_SPEED;
        }
    }

    @Override
    public void updateRotation(double deltaTime) {
        if (state == PlayerState.BOOSTING || state == PlayerState.BOOST_DECAY) {
            super.updateRotation(deltaTime);
        } else currentAngle = viewAngle;
    }

    public Projectile shoot(Vector2D direction) {
        Vector2D deltaPos = direction.normalize();
        deltaPos.multiplyInPlace(PlayerConfig.SPRITE_SIZE / 2.0);
        Projectile projectile = weapon.fire(position.add(deltaPos), direction);
        if (projectile != null) {
            projectile.setCurrentAngle(viewAngle);
            projectile.setOwner(Projectile.Owner.PLAYER);
        }
        return projectile;
    }

    public double getUltiCooldown() {
        return laserGun.getCooldownRemaining();
    }

    public double getUltiInterval() {
        return laserGun.getFireInterval();
    }

    @Override
    public boolean isDead() {
        return health <= 0;
    }

    @Override
    public void takeDamage(int damage) {
        SoundManager.getInstance().playSfx(SoundConstants.SFX_ELECTRIC);
        health -= damage;
    }

    @Override
    public Vector2D getPos() {
        return getPosition();
    }

    public int getDisplayScore() {
        return displayScore;
    }

    public void addScore(int score) {
        this.score += score;
    }

    private void updateScore() {
        if (displayScore < score ) {
            displayScore += Math.max(1, (score - displayScore) / 10);
        }
    }

    public boolean shouldRenderTrail() {
        return trailTimer > 0;
    }

    public double getViewAngle() {
        return viewAngle;
    }

    public void setViewAngle(double viewAngle) {
        this.viewAngle = viewAngle;
    }

    public double getTrailProgress() {
        return trailTimer / PlayerConfig.TRAIL_DURATION;
    }

    public double getBoostProgress() {
        return boostTimer / PlayerConfig.BOOST_DURATION;
    }

    public boolean shouldBoost() {
        return boostCooldown == 0;
    }

    public int getHealth() {
        return health;
    }

    public PlayerState getPlayerState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void switchWeapon() {
        weapon = (weapon instanceof LaserGun) ? plasmaGun : laserGun ;
    }
}
