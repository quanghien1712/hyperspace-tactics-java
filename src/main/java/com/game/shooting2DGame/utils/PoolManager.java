package com.game.shooting2DGame.utils;

import com.game.shooting2DGame.effect.DeathEffect;
import com.game.shooting2DGame.effect.Effect;
import com.game.shooting2DGame.effect.EffectType;
import com.game.shooting2DGame.effect.ExplosionEffect;
import com.game.shooting2DGame.entity.*;
import com.game.shooting2DGame.projectiles.*;

import java.util.Map;

public class PoolManager {
    private static final Map<EnemyType, ObjectPool<Enemy>> enemyPools = Map.of(
          EnemyType.BASIC_ENEMY, new ObjectPool<>(BasicEnemy::new, 20),
          EnemyType.ORBITING_ENEMY, new ObjectPool<>(OrbitEnemy::new, 20),
          EnemyType.DRONE, new ObjectPool<>(Drone::new, 20),
          EnemyType.BOSS, new ObjectPool<>(Boss::new, 1)
    );

    private static final Map<ProjectileType, ObjectPool<Projectile>> projectilePools = Map.of(
          ProjectileType.PLASMA, new ObjectPool<>(Plasma::new, 30),
          ProjectileType.HOMING_BULLET, new ObjectPool<>(HomingBullet::new, 30),
          ProjectileType.NORMAL_BULLET, new ObjectPool<>(NormalBullet::new, 80),
          ProjectileType.LASER, new ObjectPool<>(Laser::new, 1)
    );

    private static final Map<EffectType, ObjectPool<Effect>> effectPools = Map.of(
          EffectType.PLASMA_EXPLOSION, new ObjectPool<>(ExplosionEffect::new, 15),
          EffectType.BULLET_EXPLOSION, new ObjectPool<>(ExplosionEffect::new, 15),
          EffectType.DEATH, new ObjectPool<>(DeathEffect::new, 20)
    );

    private PoolManager() {}

    public static Projectile acquire(ProjectileType type) {
        ObjectPool<Projectile> pool = projectilePools.get(type);
        if (pool == null) throw new IllegalArgumentException("No pool for type: " + type);
        return pool.acquire();
    }

    public static Effect acquire(EffectType effectType) {
        ObjectPool<Effect> pool = effectPools.get(effectType);
        if (pool == null) throw new IllegalArgumentException("No pool for type: " + effectType);
        return pool.acquire();
    }

    public static Enemy acquire(EnemyType type) {
        ObjectPool<Enemy> pool = enemyPools.get(type);
        if (pool == null) throw new IllegalArgumentException("No pool for type: " + type);
        return pool.acquire();
    }

    public static void release(Effect effect) {
        ObjectPool<Effect> pool = effectPools.get(effect.getEffectType());
        if (pool == null) throw new IllegalArgumentException("No pool for type: " + effect.getEffectType());
        pool.release(effect);
    }

    public static void release(Projectile projectile) {
        ObjectPool<Projectile> pool = projectilePools.get(projectile.getProjectileType());
        if (pool == null) throw new IllegalArgumentException("No pool for type: " + projectile.getProjectileType());
        pool.release(projectile);
    }

    public static void release(Enemy enemy) {
        ObjectPool<Enemy> pool = enemyPools.get(enemy.getEnemyType());
        if (pool == null) throw new IllegalArgumentException("No pool for type: " + enemy.getEnemyType());
        pool.release(enemy);
    }
}
