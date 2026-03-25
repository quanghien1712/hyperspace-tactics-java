package com.game.shooting2DGame.ui;

import com.game.shooting2DGame.config.GameConfig;
import com.game.shooting2DGame.core.GameEngine;
import com.game.shooting2DGame.input.KeyBoardInputs;
import com.game.shooting2DGame.input.MouseInputs;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel {
    private GameWindow gameWindow;
    private MouseInputs mouseInputs;
    private KeyBoardInputs keyBoardInputs;
    private GameEngine gameEngine;

    public GamePanel(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        setDefaultPanel();
        mouseInputs = new MouseInputs();
        keyBoardInputs = new KeyBoardInputs();
        addKeyListener(keyBoardInputs);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void initWindow() {
        gameWindow = new GameWindow(this);
        requestFocus();
    }

    private void setDefaultPanel() {
        setPreferredSize(new Dimension(GameConfig.PANEL_WIDTH, GameConfig.PANEL_HEIGHT));
        setDoubleBuffered(true);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g2D);
        if (gameEngine.getStateManager() != null) {
            gameEngine.getStateManager().render(g2D);
        }
    }

    public void windowFocusLost() {
        keyBoardInputs.clearKeyPressed();
    }

    public MouseInputs getMouseInputs() {
        return mouseInputs;
    }

    public KeyBoardInputs getKeyBoardInputs() {
        return keyBoardInputs;
    }

}
