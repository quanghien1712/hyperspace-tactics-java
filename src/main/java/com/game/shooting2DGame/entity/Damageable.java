package com.game.shooting2DGame.entity;

import com.game.shooting2DGame.utils.Vector2D;

public interface Damageable {
    boolean isDead();
    void takeDamage(int damage);
    Vector2D getPos();
}
