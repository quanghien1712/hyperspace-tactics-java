package com.game.shooting2DGame.projectiles;

import com.game.shooting2DGame.config.GameConfig;
import com.game.shooting2DGame.effect.Effect;
import com.game.shooting2DGame.effect.EffectManager;
import com.game.shooting2DGame.effect.EffectType;
import com.game.shooting2DGame.entity.Enemy;
import com.game.shooting2DGame.entity.EnemyManager;
import com.game.shooting2DGame.entity.Player;
import com.game.shooting2DGame.render.Camera;
import com.game.shooting2DGame.render.ProjectileRenderer;
import com.game.shooting2DGame.utils.PoolManager;
import com.game.shooting2DGame.utils.Vector2D;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

public class ProjectileManager {
    private final List<Projectile> activeProjectile;
    private final ProjectileRenderer projectileRenderer;
    private final EnemyManager enemyManager;
    private final EffectManager effectManager;
    private final Camera camera;
    private final Player player;
    private final Object lock = new Object();

    public ProjectileManager(Camera camera, EnemyManager enemyManager,
                             Player player, EffectManager effectManager) {
        this.activeProjectile = new ArrayList<>();
        this.projectileRenderer = new ProjectileRenderer();
        this.camera = camera;
        this.enemyManager = enemyManager;
        this.player = player;
        this.effectManager = effectManager;
    }

    public void update(double deltaTime) {
        synchronized (lock) {
            for (int i = 0; i < activeProjectile.size(); ++i) {
                Projectile current = activeProjectile.get(i);
                current.update(deltaTime);
                if (current.getProjectileType() == ProjectileType.LASER) {
                    ((Laser) current).updateScreenBound(camera.getScreenPos(current.getPosition()));
                } else {
                    current.setBoundPos(camera.getScreenPos(current.getPosition()));
                    outOfScreen(current);
                }
            }
            projectileVsOther();
            projectileVsProjectile();

            for (int i = 0; i < activeProjectile.size(); ++i) {
                Projectile current = activeProjectile.get(i);
                if (!current.isActive()) {
                    int last = activeProjectile.size() - 1;
                    activeProjectile.set(i, activeProjectile.get(last));
                    activeProjectile.set(last, current);
                    activeProjectile.remove(last);
                    current.setOwner(Projectile.Owner.NONE);
                    PoolManager.release(current);
                    i--;
                }
            }
        }
    }

    private void outOfScreen(Projectile current) {
        if (current.getProjectileType() == ProjectileType.LASER) return;
        Vector2D cameraPos = camera.getCameraPos();
        double margin = 1000;
        double left   = cameraPos.getX() - margin;
        double right  = cameraPos.getX() + GameConfig.PANEL_WIDTH + margin;
        double top    = cameraPos.getY() - margin;
        double bottom = cameraPos.getY() + GameConfig.PANEL_HEIGHT + margin;
        Vector2D position = current.getPosition();
        if (position.getX() < left ||
              position.getX() > right ||
              position.getY() < top ||
              position.getY() > bottom) {
            current.setActive(false);
        }
    }

    private void projectileVsOther() {
        for (int i = 0; i < activeProjectile.size(); ++i) {
            Projectile current = activeProjectile.get(i);
            if (!current.isActive()) continue;
            if (current.getOwner() == Projectile.Owner.PLAYER) {
                Enemy enemy = enemyManager.checkCollided(current.getCollisionShape());
                if (enemy == null) continue;
                if (current.getProjectileType() != ProjectileType.LASER) {
                    current.setActive(false);
                    collided(current);
                }
                current.onHit(enemy);
            }
            else {
                if (intersects(current.getCollisionShape(), player.getBound())) {
                    if (current.getProjectileType() != ProjectileType.LASER) {
                        current.setActive(false);
                        collided(current);
                    }
                    current.onHit(player);
                }
            }
        }
    }


    private void projectileVsProjectile() {
        for (int i = 0; i < activeProjectile.size(); ++i) {
            Projectile current = activeProjectile.get(i);
            if (!current.isActive) continue;
            for (int j = i + 1; j < activeProjectile.size(); ++j) {
                Projectile other = activeProjectile.get(j);
                if (!other.isActive()) continue;
                if (current.getOwner() != other.getOwner()
                      && intersects(current.getCollisionShape(), other.getCollisionShape())) {
                    if (current.getProjectileType() != ProjectileType.LASER) {
                        current.setActive(false);
                        collided(current);
                    }

                    if (other.getProjectileType() != ProjectileType.LASER) {
                        other.setActive(false);
                        collided(other);
                    }
                    break;
                }
            }
        }
    }

    private boolean intersects(Shape a, Shape b) {
        Rectangle ra = a.getBounds();
        Rectangle rb = b.getBounds();
        if (!ra.intersects(rb)) return false;
        Area overlap = new Area(a);
        overlap.intersect(new Area(b));
        return !overlap.isEmpty();
    }

    private void collided(Projectile projectile) {
        EffectType effectType = switch (projectile.getProjectileType()) {
            case HOMING_BULLET, NORMAL_BULLET -> EffectType.BULLET_EXPLOSION;
            default -> EffectType.PLASMA_EXPLOSION;
        };
        Effect explosion = PoolManager.acquire(effectType);
        explosion.setEffectType(effectType);
        Vector2D dir = projectile.getDirection().normalize();
        dir.multiplyInPlace(projectile.getHeight() / 2.0);
        explosion.setPosition(projectile.getPosition().add(dir));
        effectManager.addActiveEffect(explosion);
    }

    public void render(Graphics2D g2D) {
        List<Projectile> snapshot;
        synchronized (lock) {
            snapshot = new ArrayList<>(activeProjectile);
        }
        snapshot.forEach(projectile -> projectileRenderer.render(projectile, camera, g2D));
    }

    public void addActiveProjectile(Projectile projectile) {
        activeProjectile.add(projectile);
    }

}
