package com.game.shooting2DGame.state;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.InputStream;

public interface State {
    void update(double deltaTime);
    void render(Graphics2D g2D);
    default boolean rendersOnTopOfGameplay() {
        return false;
    }
}
