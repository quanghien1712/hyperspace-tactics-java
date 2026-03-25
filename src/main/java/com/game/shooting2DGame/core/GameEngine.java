package com.game.shooting2DGame.core;

import com.game.shooting2DGame.config.GameConfig;
import com.game.shooting2DGame.state.DefaultStateFactory;
import com.game.shooting2DGame.state.StateManager;
import com.game.shooting2DGame.ui.GamePanel;

public class GameEngine implements Runnable{
    private GamePanel gamePanel;
    private Thread gameThread;
    private StateManager stateManager;


    public GameEngine() {
        initClasses();
        startGameThread();
    }

    private void initClasses() {
        gamePanel = new GamePanel(this);
        stateManager = new StateManager(new DefaultStateFactory(this));
        gamePanel.initWindow();
    }

    private void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / GameConfig.FPS;
        double timePerUpdate = 1000000000.0 / GameConfig.UPS;
        long lastTime = System.nanoTime();
        long currentTime;
        double deltaF = 0;
        double deltaU = 0;
        int frame = 0;
        long timer = System.nanoTime();
        while (gameThread != null) {
            currentTime = System.nanoTime();
            long d = currentTime - lastTime;
            deltaF += d / timePerFrame;
            deltaU += d / timePerUpdate;
            lastTime = currentTime;
            if (deltaU >= 1) {
                update(timePerUpdate / 1000000000.0);
                deltaU--;
            }
            if (deltaF >= 1) {
                frame++;
                render();
                deltaF--;
            }
            if(currentTime - timer >= 1000000000) {
                frame = 0;
                timer = currentTime;
            }
            if (deltaF < 1 && deltaU < 1) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void render() {
        gamePanel.repaint();
    }

    private void update(double deltaTime) {
        stateManager.update(deltaTime);
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public GamePanel getGamePanel() {
        return this.gamePanel;
    }


}
