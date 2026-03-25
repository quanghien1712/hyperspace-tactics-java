package com.game.shooting2DGame.state;

import com.game.shooting2DGame.config.GameConfig;
import com.game.shooting2DGame.config.UIConfig;
import com.game.shooting2DGame.core.GameEngine;
import com.game.shooting2DGame.input.MouseInputs;
import com.game.shooting2DGame.sound.SoundConstants;
import com.game.shooting2DGame.sound.SoundManager;
import com.game.shooting2DGame.ui.Button;
import com.game.shooting2DGame.utils.FontLoader;
import com.game.shooting2DGame.utils.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameOver implements State {
    private int bgPosX, bgPosY;
    private final StateManager stateManager;
    private BufferedImage background;
    private BufferedImage table;
    private Font ethnocentricFont;
    private Button mainMenu;
    private Button replay;
    private Button github;
    private final MouseInputs mouseInputs;
    private static final Logger logger = Logger.getLogger(GameOver.class.getName());

    public GameOver(GameEngine gameEngine, StateManager stateManager) {
        this.mouseInputs = gameEngine.getGamePanel().getMouseInputs();
        this.stateManager = stateManager;
        initBackground();
        initButton();
        initFont();
    }

    private void initButton() {
        int buttonWidth = UIConfig.PAUSE_BUTTON_WIDTH;
        int buttonHeight = UIConfig.PAUSE_BUTTON_HEIGHT;
        int buttonY = bgPosY + 450;
        int spacing = 90;
        int totalWidth = (buttonWidth * 3) + (spacing * 2);
        int startX = bgPosX + (UIConfig.PAUSE_OVERLAY_WIDTH - totalWidth) / 2;
        mainMenu = new Button(startX, buttonY, buttonWidth, buttonHeight, "Menu");
        replay = new Button(startX + buttonWidth + spacing, buttonY, buttonWidth, buttonHeight, "Reset");
        github = new Button(startX + 2 * (buttonWidth + spacing), buttonY, buttonWidth, buttonHeight, "Github");
    }

    private void initBackground() {
        background = ImageLoader.loadImage("/background/PauseBackground.png");
        table = ImageLoader.loadImage("/background/Table.png");
        bgPosX = (GameConfig.PANEL_WIDTH - UIConfig.PAUSE_OVERLAY_WIDTH) / 2;
        bgPosY = (GameConfig.PANEL_HEIGHT - UIConfig.PAUSE_OVERLAY_HEIGHT) / 2;
    }

    private void initFont() {
        ethnocentricFont = FontLoader.loadEthnocentricOrFallback(45f, "Arial", Font.BOLD);
    }


    @Override
    public void update(double deltaTime) {
        mainMenu.update(mouseInputs);
        replay.update(mouseInputs);
        github.update(mouseInputs);
        if (mainMenu.isClicked()) {
            stateManager.changeState(GameState.MENU);
            SoundManager.getInstance().resetAll();
            SoundManager.getInstance().playBgm(SoundConstants.BGM_MENU);
            mainMenu.resetButton();
        }
        if (replay.isClicked()) {
            stateManager.resetGame();
            stateManager.changeState(GameState.PLAYING);
            SoundManager.getInstance().resetAll();
            SoundManager.getInstance().playBgm(SoundConstants.BGM_PLAYING);
            replay.resetButton();
        }
        if (github.isClicked()) {
            openWebpage("https://github.com/quanghien1712");
            github.resetButton();
        }
    }

    @Override
    public void render(Graphics2D g2D) {
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
              RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.drawImage(background, bgPosX, bgPosY, UIConfig.PAUSE_OVERLAY_WIDTH, UIConfig.PAUSE_OVERLAY_HEIGHT, null);
        g2D.drawImage(table, bgPosX + UIConfig.PAUSE_OVERLAY_WIDTH / 2 - 20,
              bgPosY + 130, UIConfig.TABLE_WIDTH, UIConfig.TABLE_HEIGHT, null);
        g2D.drawImage(table, bgPosX + UIConfig.PAUSE_OVERLAY_WIDTH / 2 - 20,
              bgPosY + 270, UIConfig.TABLE_WIDTH, UIConfig.TABLE_HEIGHT, null);
        drawText(g2D);
        mainMenu.render(g2D);
        replay.render(g2D);
        github.render(g2D);
    }

    private void drawText(Graphics2D g2D) {
        g2D.setColor(Color.WHITE);
        g2D.setFont(ethnocentricFont);
        g2D.drawString("GAME OVER", bgPosX + 110, bgPosY + 55);

        g2D.setFont(ethnocentricFont.deriveFont(40f));
        FontMetrics fm = g2D.getFontMetrics();
        int tableCenterX = bgPosX + UIConfig.PAUSE_OVERLAY_WIDTH / 2 - 20 + UIConfig.TABLE_WIDTH / 2;

        g2D.drawString("SCORE", bgPosX + UIConfig.PAUSE_OVERLAY_WIDTH / 2 - 270, bgPosY + 190);
        String scoreText = String.valueOf(stateManager.getScore());
        int scoreTextWidth = fm.stringWidth(scoreText);
        g2D.drawString(scoreText, tableCenterX - scoreTextWidth / 2, bgPosY + 190);

        g2D.drawString("RECORD", bgPosX + UIConfig.PAUSE_OVERLAY_WIDTH / 2 - 270, bgPosY + 330);
        String recordText = String.valueOf(stateManager.getRecord());
        int recordTextWidth = fm.stringWidth(recordText);
        g2D.drawString(recordText, tableCenterX - recordTextWidth / 2, bgPosY + 330);

    }

    private void openWebpage(String url) {
        try {
            if (Desktop.isDesktopSupported()
                  && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (IOException | URISyntaxException e) {
            logger.log(Level.WARNING, "Failed to open link" + url, e);
        }
    }

    @Override
    public boolean rendersOnTopOfGameplay() {
        return true;
    }
}
