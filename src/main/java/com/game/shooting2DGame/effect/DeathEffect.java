package com.game.shooting2DGame.effect;

public class DeathEffect extends Effect {
    private static final int WIDTH = 85;
    private static final int HEIGHT = 85;
    private static final int ANI_SPEED = 11;
    public DeathEffect() {
        super(0, 0, WIDTH, HEIGHT, ANI_SPEED);
        this.totalFrame = 16;
        this.effectType = EffectType.DEATH;
    }
}
