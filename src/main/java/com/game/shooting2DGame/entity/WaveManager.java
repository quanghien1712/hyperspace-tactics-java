package com.game.shooting2DGame.entity;

import com.game.shooting2DGame.config.EnemyConfig;
import com.game.shooting2DGame.config.GameConfig;
import com.game.shooting2DGame.render.Camera;
import com.game.shooting2DGame.utils.PoolManager;
import com.game.shooting2DGame.utils.Vector2D;

import java.util.Random;

public class WaveManager {
    private final EnemyManager enemyManager;
    private final Random random;
    private final Camera camera;
    private int spawnedEnemies, defeatedEnemies;
    private double spawnTimer, waveTimer;
    private int enemiesPerWave;
    private int currentWave;
    private boolean waveInProgress;


    public WaveManager(EnemyManager enemyManager, Camera camera) {
        this.enemyManager = enemyManager;
        this.camera = camera;
        this.random = new Random();
    }

    public void update(double deltaTime) {
        if (!waveInProgress) {
            waveTimer += deltaTime;
            if (waveTimer >= EnemyConfig.WAVE_DELAY) {
                waveTimer = 0;
                startNextWave();
            }
        }
        if (spawnedEnemies < enemiesPerWave) {
            spawnTimer += deltaTime;
            if (spawnTimer >= EnemyConfig.SPAWN_INTERVAL) {
                spawnTimer = 0;
                spawnEnemy();
            }
        }
        if (defeatedEnemies >= enemiesPerWave) {
            waveInProgress = false;
        }
    }

    private void startNextWave() {
        waveInProgress = true;
        currentWave++;
        enemiesPerWave = 5 + currentWave * 2;
        spawnedEnemies = 0;
        defeatedEnemies = 0;
        spawnTimer = EnemyConfig.SPAWN_INTERVAL;
    }

    private EnemyType getEnemyType() {
        if (currentWave % 3 == 0) {
            currentWave++;
            return EnemyType.BOSS;
        }
        return switch (random.nextInt(3)) {
            case 0 -> EnemyType.BASIC_ENEMY;
            case 1 -> EnemyType.ORBITING_ENEMY;
            default -> EnemyType.DRONE;
        };
    }

    private void spawnEnemy() {
        Enemy enemy = PoolManager.acquire(getEnemyType());
        double x, y;
        int side = random.nextInt(4);
        int margin = 500;
        Vector2D cameraPos = camera.getCameraPos();
        switch (side) {
            case 0 -> { //left
                x = cameraPos.getX() - margin;
                y = cameraPos.getY() + random.nextInt(GameConfig.PANEL_HEIGHT);
            }
            case 1 -> { //right
                x = cameraPos.getX() + GameConfig.PANEL_WIDTH + margin;
                y = cameraPos.getY() + random.nextInt(GameConfig.PANEL_HEIGHT);
            }
            case 2 -> { //top
                x = cameraPos.getX() + random.nextInt(GameConfig.PANEL_WIDTH);
                y = cameraPos.getY() - margin;
            }
            default -> { //bottom
                x = cameraPos.getX() + random.nextInt(GameConfig.PANEL_WIDTH);
                y = cameraPos.getY() + GameConfig.PANEL_HEIGHT + margin;
            }
        }
        enemy.setPosition(x, y);
        spawnedEnemies++;
        enemyManager.addActiveEnemy(enemy);
    }

    public void enemyOnDefeated() {
        defeatedEnemies++;
    }
}
