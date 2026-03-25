package com.game.shooting2DGame.ui;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {
    private JFrame window;

    public GameWindow(GamePanel gamePanel) {
        window = new JFrame();

        window.setTitle("Space War");
        window.setResizable(false);
        window.add(gamePanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setFocusable(true);
        window.requestFocusInWindow();
        window.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.windowFocusLost();
            }
        });
    }
}
