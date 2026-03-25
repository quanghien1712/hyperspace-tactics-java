package com.game.shooting2DGame.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KeyBoardInputs implements KeyListener {
    private final Set<Integer> pressedKey;
    private final Object lock = new Object();

    public KeyBoardInputs() {
        pressedKey = new HashSet<>();
    }

    public void clearKeyPressed() {
        synchronized (lock) {
            pressedKey.clear();
        }
    }

    public boolean isKeyPressed(int keyCode) {
        synchronized (lock) {
            return pressedKey.contains(keyCode);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        synchronized (lock) {
            pressedKey.add(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        synchronized (lock) {
            pressedKey.remove(e.getKeyCode());
        }
    }


}
