package com.game.shooting2DGame.state;

import com.game.shooting2DGame.config.GameConfig;
import com.game.shooting2DGame.core.GameEngine;
import com.game.shooting2DGame.effect.EffectManager;
import com.game.shooting2DGame.entity.EnemyManager;
import com.game.shooting2DGame.entity.Player;
import com.game.shooting2DGame.input.KeyBoardInputs;
import com.game.shooting2DGame.input.MouseInputs;
import com.game.shooting2DGame.entity.PlayerController;
import com.game.shooting2DGame.projectiles.ProjectileManager;
import com.game.shooting2DGame.render.*;
import com.game.shooting2DGame.sound.SoundManager;
import com.game.shooting2DGame.ui.HUD;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Playing implements State{
    private final StateManager stateManager;
    private Player player;
    private EnemyManager enemyManager;
    private ProjectileManager projectileManager;
    private EffectManager effectManager;

    private PlayerRenderer playerRenderer;
    private PlayingBackground backgroundRenderer;
    private SpaceWarpTransition warpTransition;
    private HUD hud;

    private PlayerController playerController;
    private Camera camera;
    private final KeyBoardInputs keyBoardInputs;
    private final MouseInputs mouseInputs;

    public Playing(GameEngine gameEngine, StateManager stateManager) {
        this.stateManager = stateManager;
        mouseInputs = gameEngine.getGamePanel().getMouseInputs();
        keyBoardInputs = gameEngine.getGamePanel().getKeyBoardInputs();
        initClasses();
    }

    private void initClasses() {
        playerRenderer = new PlayerRenderer();
        backgroundRenderer = new PlayingBackground();
        warpTransition = new SpaceWarpTransition();
        camera = new Camera();
        player = new Player(GameConfig.PANEL_WIDTH / 2.0, GameConfig.PANEL_HEIGHT / 2.0);
        effectManager = new EffectManager();
        enemyManager = new EnemyManager(player, camera);
        projectileManager = new ProjectileManager(camera, enemyManager, player, effectManager);
        playerController = new PlayerController(keyBoardInputs, mouseInputs, camera, player);
        hud = new HUD();
        warpTransition.start();
    }

    @Override
    public void update(double deltaTime) {
        if (warpTransition.isActive()) {
            warpTransition.update(deltaTime);
            return;
        }
        playerController.update(projectileManager);
        player.update(deltaTime);
        camera.followPlayer(player, deltaTime);
        enemyManager.update(deltaTime, projectileManager, effectManager);
        projectileManager.update(deltaTime);
        effectManager.update();
        updateState();
    }

    @Override
    public void render(Graphics2D g2D) {
        if (warpTransition.isActive()) {
            if (warpTransition.shouldRevealGameplay()) {
                renderGameplayScene(g2D);
            } else {
                g2D.setColor(Color.BLACK);
                g2D.fillRect(0, 0, GameConfig.PANEL_WIDTH, GameConfig.PANEL_HEIGHT);
            }

            warpTransition.render(g2D);
            hud.renderBorder(g2D);

            float hudOverlayAlpha = warpTransition.getHudFlashOverlayAlpha();
            if (hudOverlayAlpha > 0f) {
                Composite oldComposite = g2D.getComposite();
                g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, hudOverlayAlpha));
                g2D.setColor(Color.WHITE);
                g2D.fillRect(0, 0, GameConfig.PANEL_WIDTH, GameConfig.PANEL_HEIGHT);
                g2D.setComposite(oldComposite);
            }
            return;
        }

        renderGameplayScene(g2D);
    }

    private void renderGameplayScene(Graphics2D g2D) {
        backgroundRenderer.render(g2D, camera);
        hud.render(g2D, player.getHealth(), player.getDisplayScore(),
              player.getUltiCooldown(), player.getUltiInterval());
        playerRenderer.render(player, camera, g2D, stateManager.haveOverlay());
        enemyManager.render(g2D);
        projectileManager.render(g2D);
        effectManager.render(g2D, camera);
    }


    private void updateState() {
        if (keyBoardInputs.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            stateManager.changeState(GameState.PAUSED);
            SoundManager.getInstance().stopAllSFX();
        }
        if (player.isDead()) {
            stateManager.changeState(GameState.GAME_OVER);
            stateManager.setGameOverData(player.getDisplayScore());
        }
    }

}
