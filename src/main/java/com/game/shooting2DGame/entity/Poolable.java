package com.game.shooting2DGame.entity;

public interface Poolable {
    void onRelease();
    void setActive(boolean active);
    boolean isActive();
}
