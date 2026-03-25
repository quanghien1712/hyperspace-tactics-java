package com.game.shooting2DGame.config;

public class PlayerConfig {
    private PlayerConfig() {}


    public static final int MAX_HEALTH = 1000;
    public static final int DEFAULT_WIDTH = 60;
    public static final int DEFAULT_HEIGHT = 60;

    public static final double MAX_SPEED = 200;
    public static final double BOOST_SCALE = 5;
    public static final double BOOST_SPEED = MAX_SPEED * BOOST_SCALE;
    public static final double BOOST_DURATION = 0.4;
    public static final double BOOST_DECAY = 1;
    public static final double BOOST_COOLDOWN = 4;
    public static final double TRAIL_DURATION = BOOST_DURATION + BOOST_DECAY + 0.5;

    public static final double ROTATION_SPEED = 3;
    public static final double ACCELERATION = 2;
    public static final double DRAG = 1;
    public static final double DECAY_RATE =
          PlayerConfig.BOOST_SPEED / PlayerConfig.BOOST_DECAY;

    public static final double SIZE_SCALE = 1.7;
    public static final int SPRITE_SIZE = (int) (GameConfig.TILE_SIZE * SIZE_SCALE);
}
