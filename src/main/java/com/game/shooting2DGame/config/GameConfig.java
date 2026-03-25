package com.game.shooting2DGame.config;

public class GameConfig {
    private GameConfig(){}

    public static final int TILE_SIZE = 48;
    public static final int TILES_IN_WIDTH = 31;
    public static final int TILES_IN_HEIGHT = 16;
    public static final int PANEL_WIDTH = TILE_SIZE * TILES_IN_WIDTH;
    public static final int PANEL_HEIGHT = TILE_SIZE * TILES_IN_HEIGHT;
    public static final int FPS = 120;
    public static final int UPS = 120;

    public static final double ROTATION_THRESHOLD = 0.01;
    public static final int DEAD_ZONE_WIDTH = 300;
    public static final int DEAD_ZONE_HEIGHT = 100;
    public static final double CAMERA_SPEED = 3.0;
}
