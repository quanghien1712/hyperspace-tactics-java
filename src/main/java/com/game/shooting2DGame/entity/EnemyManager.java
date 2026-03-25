package com.game.shooting2DGame.entity;

import com.game.shooting2DGame.effect.Effect;
import com.game.shooting2DGame.effect.EffectManager;
import com.game.shooting2DGame.effect.EffectType;
import com.game.shooting2DGame.projectiles.HomingBullet;
import com.game.shooting2DGame.projectiles.Projectile;
import com.game.shooting2DGame.projectiles.ProjectileManager;
import com.game.shooting2DGame.projectiles.ProjectileType;
import com.game.shooting2DGame.render.Camera;
import com.game.shooting2DGame.render.EnemyRenderer;
import com.game.shooting2DGame.sound.SoundConstants;
import com.game.shooting2DGame.sound.SoundManager;
import com.game.shooting2DGame.utils.PoolManager;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

public class EnemyManager {
    private final Player player;
    private final List<Enemy> activeEnemy;
    private final EnemyRenderer enemyRenderer;
    private final Camera camera;
    private final WaveManager waveManager;
    private final Object lock = new Object();

    public EnemyManager(Player player, Camera camera) {
        this.player = player;
        this.camera = camera;
        this.activeEnemy = new ArrayList<>();
        enemyRenderer = new EnemyRenderer();
        this.waveManager = new WaveManager(this, camera);
    }

    public void update(double deltaTime, ProjectileManager projectileManager,
                       EffectManager effectManager) {
        synchronized (lock) {
            waveManager.update(deltaTime);
            for (int i = 0; i < activeEnemy.size(); ++i) {
                Enemy current = activeEnemy.get(i);
                current.update(deltaTime);
                current.setBoundPos(camera.getScreenPos(current.getPosition()));
                if (current
                      .getBound()
                      .intersects(player.getBound())) {
                    current.takeDamage(current.getHealth());
                    player.takeDamage(current.getMaxHealth());
                }
                if (current.isDead()) {
                    Effect death = PoolManager.acquire(EffectType.DEATH);
                    death.setPosition(current.getPosition());
                    effectManager.addActiveEffect(death);
                    SoundManager.getInstance().playSfx(SoundConstants.SFX_EXPLOSION);
                    current.setActive(false);
                    player.addScore(current.getDefeatedScore());
                    waveManager.enemyOnDefeated();
                }
                if (!current.isActive()) {
                    int last = activeEnemy.size() - 1;
                    activeEnemy.set(i, activeEnemy.get(last));
                    activeEnemy.set(last, current);
                    activeEnemy.remove(last);
                    PoolManager.release(current);
                    i--;
                } else {
                    if (current.getEnemyType() == EnemyType.DRONE) continue;
                    Projectile projectile = current.shoot();
                    if (projectile == null) continue;
                    if (projectile.getOwner() != Projectile.Owner.PLAYER &&
                          projectile.getProjectileType() == ProjectileType.HOMING_BULLET) {
                        ((HomingBullet) projectile).setTarget(player);
                    }
                    projectile.setBoundPos(camera.getScreenPos(projectile.getPosition()));
                    projectileManager.addActiveProjectile(projectile);
                }
            }
        }
    }

    public void render(Graphics2D g2D) {
        List<Enemy> snapshot;
        synchronized (lock) {
            snapshot = new ArrayList<>(activeEnemy);
        }
        snapshot.forEach(enemy -> enemyRenderer.render(enemy, camera, g2D));
    }

    public void addActiveEnemy(Enemy enemy) {
        enemy.setTarget(player);
        activeEnemy.add(enemy);
    }

    public Enemy checkCollided(Shape obj) {
        return activeEnemy.stream().filter(enemy -> intersects(obj, enemy.getBound()))
                          .findFirst()
                          .orElse(null);
    }

    private boolean intersects(Shape a, Shape b) {
        Rectangle ra = a.getBounds();
        Rectangle rb = b.getBounds();
        if (!ra.intersects(rb)) return false;
        Area overlap = new Area(a);
        overlap.intersect(new Area(b));
        return !overlap.isEmpty();
    }


}
