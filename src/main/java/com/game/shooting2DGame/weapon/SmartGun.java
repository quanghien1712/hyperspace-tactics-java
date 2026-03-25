package com.game.shooting2DGame.weapon;

import com.game.shooting2DGame.projectiles.Projectile;
import com.game.shooting2DGame.projectiles.ProjectileType;
import com.game.shooting2DGame.utils.PoolManager;
import com.game.shooting2DGame.utils.Vector2D;

public class SmartGun extends Weapon{
    private static final double FIRE_RATE = 0.4;

    public SmartGun() {
        super(FIRE_RATE);
    }

    @Override
    public Projectile createProjectile(Vector2D playerPos, Vector2D aimDirection) {
        Projectile projectile = PoolManager.acquire(ProjectileType.HOMING_BULLET);
        projectile.setPosition(playerPos);
        projectile.setDirection(aimDirection);
        return projectile;
    }
}
