package com.game.shooting2DGame;

import com.game.shooting2DGame.core.GameEngine;

public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("sun.java2d.uiScale", "1");
        new GameEngine();
    }
}
