package com.game.shooting2DGame.state;

public interface StateFactory {
    State create(GameState gameState, StateManager stateManager);
}
