package com.game.shooting2DGame.input;

import com.game.shooting2DGame.utils.Vector2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputs implements MouseListener, MouseMotionListener {
    private Vector2D mousePos;
    private volatile boolean isMousePressed;
    private volatile int mouseButton;
    private final Object lock = new Object();

    public MouseInputs() {
        mousePos = new Vector2D(0,0);
    }

    public Vector2D getMousePos() {
        synchronized (lock) {
            return mousePos;
        }
    }

    public void setMousePos(MouseEvent e) {
        synchronized (lock) {
            mousePos.copy(e.getX(), e.getY());
        }
    }

    public boolean isMousePressed() {
        return isMousePressed;
    }

    public boolean isMousePressed(int button) {
        return isMousePressed && mouseButton == button;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        isMousePressed = true;
        mouseButton = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isMousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        setMousePos(e);
        isMousePressed = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        setMousePos(e);
    }
}
