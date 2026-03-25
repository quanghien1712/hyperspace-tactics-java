package com.game.shooting2DGame.weapon;

import com.game.shooting2DGame.projectiles.Projectile;
import com.game.shooting2DGame.projectiles.ProjectileType;
import com.game.shooting2DGame.utils.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class WeaponTest {

    @Test
    void fire_shouldRespectCooldown() {
        DummyWeapon weapon = new DummyWeapon(2.0);
        Vector2D pos = new Vector2D(0, 0);
        Vector2D dir = new Vector2D(1, 0);

        assertNull(weapon.fire(pos, dir));

        weapon.update(0.5);
        Projectile first = weapon.fire(pos, dir);
        assertNotNull(first);

        Projectile second = weapon.fire(pos, dir);
        assertNull(second);

        weapon.update(0.25);
        assertNull(weapon.fire(pos, dir));

        weapon.update(0.25);
        assertNotNull(weapon.fire(pos, dir));
    }

    @Test
    void getFireInterval_shouldMatchFireRateFormula() {
        DummyWeapon weapon = new DummyWeapon(4.0);
        assertEquals(0.25, weapon.getFireInterval(), 1e-9);
    }

    private static final class DummyWeapon extends Weapon {
        private DummyWeapon(double fireRate) {
            super(fireRate);
        }

        @Override
        public Projectile createProjectile(Vector2D playerPos, Vector2D aimDirection) {
            DummyProjectile projectile = new DummyProjectile();
            projectile.setPosition(playerPos);
            projectile.setDirection(aimDirection);
            projectile.setProjectileType(ProjectileType.NORMAL_BULLET);
            projectile.setActive(true);
            return projectile;
        }
    }

    private static final class DummyProjectile extends Projectile {
        private DummyProjectile() {
            super(0, 0, 10, 10);
        }

        @Override
        public void update(double deltaTime) {

        }

        private void setProjectileType(ProjectileType type) {
            this.projectileType = type;
        }
    }
}

