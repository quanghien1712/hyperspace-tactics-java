package com.game.shooting2DGame.ui;


import com.game.shooting2DGame.config.UIConfig;
import com.game.shooting2DGame.input.MouseInputs;
import com.game.shooting2DGame.utils.ImageLoader;
import com.game.shooting2DGame.utils.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Slider {
    private BufferedImage track, fill, knob;
    private final int x, y;
    private final int width, height;
    private int knobPosX;
    private Rectangle sliderBound;
    private final int fillOffsetX = 7;
    private final int fillOffsetY = 4;
    private int minFillX, maxFillX;


    public Slider(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        loadImage();
        initBound();
        minFillX = x + fillOffsetX - 4;
        maxFillX = x + fillOffsetX + fill.getWidth() - 10;
        knobPosX = minFillX + (maxFillX - minFillX) / 2;
    }

    private void initBound() {
        sliderBound = new Rectangle(x, y, width, height);
    }

    private BufferedImage resize(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }

    private void loadImage() {
        track = resize(ImageLoader.loadImage("/slider/track.png"), width, height);
        fill = resize(ImageLoader.loadImage("/slider/fill.png"), width - 10, height - 8);
        knob = resize(ImageLoader.loadImage("/slider/knob.png"), UIConfig.KNOB_WIDTH, UIConfig.KNOB_HEIGHT);
    }

    private boolean inBound(Rectangle bound, Vector2D mousePos) {
        return bound.contains(mousePos.getX(), mousePos.getY());
    }

    public void update(MouseInputs mouseInputs) {
        Vector2D mousePos = mouseInputs.getMousePos();
        boolean pressedOnSlider = inBound(sliderBound, mousePos)
                        && mouseInputs.isMousePressed();
        if (pressedOnSlider) {
            knobPosX = Math.max(minFillX, Math.min((int) mousePos.getX(), maxFillX));
        }

    }

    public void render(Graphics2D g2D) {
        int subFillWidth = Math.max(1, knobPosX - minFillX);
        BufferedImage subFill = fill.getSubimage(0, 0, subFillWidth, fill.getHeight());
        g2D.drawImage(track, x, y, null);
        g2D.drawImage(subFill, x + fillOffsetX, y + fillOffsetY, null);
        g2D.drawImage(knob, knobPosX, y - 5, null);
    }

    public float getFilledPercent() {
        return (float)(knobPosX - minFillX)/ (maxFillX - minFillX);
    }

}
