package com.game.shooting2DGame.state;


import com.game.shooting2DGame.config.GameConfig;
import com.game.shooting2DGame.config.UIConfig;
import com.game.shooting2DGame.core.GameEngine;
import com.game.shooting2DGame.input.MouseInputs;
import com.game.shooting2DGame.sound.SoundConstants;
import com.game.shooting2DGame.sound.SoundManager;
import com.game.shooting2DGame.ui.Button;
import com.game.shooting2DGame.utils.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu implements State{
    private final StateManager stateManager;
    private final MouseInputs mouseInputs;

    private Button playButton;
    private Button infoButton;
    private Button exitButton;
    private final BufferedImage background;

    private static final Logger logger = Logger.getLogger(Menu.class.getName());


    public Menu(GameEngine gameEngine, StateManager stateManager) {
        SoundManager.getInstance().playBgm(SoundConstants.BGM_MENU);
        this.stateManager = stateManager;
        mouseInputs = gameEngine.getGamePanel().getMouseInputs();
        background = ImageLoader.loadImage("/background/MenuBackground.png");
        initButton();
    }

    private void initButton() {
        int buttonWidth = UIConfig.MENU_BUTTON_WIDTH;
        int buttonHeight = UIConfig.MENU_BUTTON_HEIGHT;
        int centerX = (GameConfig.PANEL_WIDTH - buttonWidth) / 2;
        int spacing = 20;
        int totalHeight = (buttonHeight * 3) + (spacing * 2);
        int startY = (GameConfig.PANEL_HEIGHT - totalHeight) / 2 + 200;

        playButton = new Button(centerX, startY, buttonWidth, buttonHeight, "Start");
        infoButton = new Button(centerX, startY + buttonHeight + spacing, buttonWidth, buttonHeight, "Info");
        exitButton = new Button(centerX, startY + (buttonHeight + spacing) * 2, buttonWidth, buttonHeight, "Exit");
    }

    @Override
    public void update(double deltaTime) {
        playButton.update(mouseInputs);
        infoButton.update(mouseInputs);
        exitButton.update(mouseInputs);
        if (playButton.isClicked()) {
            stateManager.resetGame();
            stateManager.changeState(GameState.PLAYING);
            SoundManager.getInstance().stop(SoundConstants.BGM_MENU);
            SoundManager.getInstance().playBgm(SoundConstants.BGM_PLAYING);
            playButton.resetButton();
        }
        if (exitButton.isClicked()) {
            System.exit(0);
        }
        if (infoButton.isClicked()) {
            openWebpage("https://www.facebook.com/tran.quang.hien.518714");
            infoButton.resetButton();
        }
    }

    private void openWebpage(String url) {
        try {
            if (Desktop.isDesktopSupported()
                  && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (IOException | URISyntaxException e) {
            logger.log(Level.WARNING, "Failed to open link: " + url, e);
        }
    }

    @Override
    public void render(Graphics2D g2D) {
        g2D.drawImage(background, 0, 0, GameConfig.PANEL_WIDTH, GameConfig.PANEL_HEIGHT, null);
        playButton.render(g2D);
        infoButton.render(g2D);
        exitButton.render(g2D);
    }
}
