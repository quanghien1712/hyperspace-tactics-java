package com.game.shooting2DGame.effect;

public class ExplosionEffect extends Effect {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;
    private static final int ANI_SPEED = 3;

    public ExplosionEffect() {
        super(0, 0, WIDTH, HEIGHT, ANI_SPEED);
        this.totalFrame = 16;
    }
}
