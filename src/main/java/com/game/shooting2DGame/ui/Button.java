package com.game.shooting2DGame.ui;

import com.game.shooting2DGame.input.MouseInputs;
import com.game.shooting2DGame.utils.ImageLoader;
import com.game.shooting2DGame.utils.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Button{
    private final int x, y;
    private final int width, height;
    private BufferedImage[] image;
    private Rectangle bounds;
    private boolean mouseOver;
    private boolean mousePressed;
    private boolean wasPressedInside;
    private boolean isClicked;

    public Button(int x, int y, int width, int height, String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        loadButton(name);
        initBounds();
    }

    private void loadButton(String name) {
        image = new BufferedImage[2];
        for (int i = 0; i < image.length; ++i) {
            image[i] = ImageLoader.loadImage("/button/" + name + "_BTN" + (i + 1) + ".png");
        }
    }

    private void initBounds() {
        bounds = new Rectangle(x, y, width, height);
    }

    private boolean inBound(Vector2D mousePos) {
        return bounds.contains(mousePos.getX(), mousePos.getY());
    }

    public void update(MouseInputs mouseInputs) {
        mouseOver = inBound(mouseInputs.getMousePos());
        isClicked = false;
        boolean currentMousePressed = mouseInputs.isMousePressed();
        if (currentMousePressed && !mousePressed) {
            wasPressedInside = mouseOver;
        }
        if (!currentMousePressed && mousePressed) {
            if (wasPressedInside && mouseOver) {
                isClicked = true;
            }
            wasPressedInside = false;
        }
        mousePressed = currentMousePressed;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void render(Graphics2D g2D) {
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
              RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING,
              RenderingHints.VALUE_RENDER_QUALITY);

        int index = mouseOver ? 1 : 0;
        g2D.drawImage(image[index], x, y,
              width, height, null);
    }

    public void resetButton() {
        mousePressed = mouseOver = false;
    }
}
