package com.game.shooting2DGame.state;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StateManager {
    private final StateFactory stateFactory;
    private volatile State currentState;
    private final Map<GameState, State> states = new HashMap<>();
    private int score, record;

    public StateManager(StateFactory stateFactory) {
        this.stateFactory = stateFactory;
        initState();
        currentState = states.get(GameState.MENU);
    }

    private void initState() {
        for (GameState gameState : GameState.values()) {
            states.put(gameState, stateFactory.create(gameState, this));
        }
    }

    public void resetGame() {
        states.put(GameState.PLAYING, stateFactory.create(GameState.PLAYING, this));
    }

    public void update(double deltaTime) {
        currentState.update(deltaTime);
    }

    public void render(Graphics2D g2D) {
        if (currentState.rendersOnTopOfGameplay()) {
            states.get(GameState.PLAYING).render(g2D);
            currentState.render(g2D);
        }
        else currentState.render(g2D);
    }

    public void changeState(GameState gameState) {
        currentState = states.get(gameState);
    }

    public boolean haveOverlay() {
        return currentState instanceof PausedOverlay;
    }

    public void setGameOverData(int score) {
        this.score = score;
        this.record = Math.max(score, record);
    }

    public int getScore() {
        return score;
    }

    public int getRecord() {
        return record;
    }

}
