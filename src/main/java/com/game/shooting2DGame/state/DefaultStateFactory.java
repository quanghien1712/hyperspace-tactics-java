package com.game.shooting2DGame.state;

import com.game.shooting2DGame.core.GameEngine;

public class DefaultStateFactory implements StateFactory{
    private final GameEngine gameEngine;

    public DefaultStateFactory(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public State create(GameState gameState, StateManager stateManager) {
        return switch (gameState) {
            case PLAYING -> new Playing(gameEngine, stateManager);
            case MENU -> new Menu(gameEngine, stateManager);
            case PAUSED -> new PausedOverlay(gameEngine, stateManager);
            case GAME_OVER -> new GameOver(gameEngine, stateManager);
        };
    }
}
