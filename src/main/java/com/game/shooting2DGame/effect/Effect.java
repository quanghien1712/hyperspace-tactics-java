package com.game.shooting2DGame.effect;

import com.game.shooting2DGame.entity.Poolable;
import com.game.shooting2DGame.utils.Vector2D;

public abstract class Effect implements Poolable {
    Vector2D position;
    protected int width, height;
    protected int aniIndex, aniTick, aniSpeed;
    protected int totalFrame;
    protected boolean isActive;
    protected EffectType effectType;

    public Effect(double x, double y, int width, int height, int aniSpeed) {
        this.position = new Vector2D(x, y);
        this.width = width;
        this.height = height;
        this.aniSpeed = aniSpeed;
    }

    public void updateAnimation() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= totalFrame) {
                aniIndex = 0;
                isActive = false;
            }
        }
    }

    @Override
    public void onRelease() {
        aniTick = aniIndex = 0;
        isActive = false;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPosition(Vector2D pos) {
        position.copy(pos);
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public EffectType getEffectType() {
        return this.effectType;
    }

    public int getAniIndex() {
        return this.aniIndex;
    }

    public void setEffectType(EffectType effectType) {
        this.effectType = effectType;
    }
}
