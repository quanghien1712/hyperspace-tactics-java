package com.game.shooting2DGame.render;

import com.game.shooting2DGame.state.PlayerState;
import com.game.shooting2DGame.config.PlayerConfig;
import com.game.shooting2DGame.entity.Player;
import com.game.shooting2DGame.utils.ImageLoader;
import com.game.shooting2DGame.utils.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class PlayerRenderer {
    private BufferedImage[] boostAni;
    private BufferedImage idle;
    private BoostTrail boostTrail = new BoostTrail();

    public PlayerRenderer() {
        loadAnimation();
    }


    private void loadAnimation() {
        boostAni = new BufferedImage[42];
        BufferedImage boostSheet = ImageLoader.loadImage("/playerboost/BoostSpriteSheet.png");
        int n = boostAni.length;
        for (int i = 0; i < n / 2; ++i) {
            boostAni[i] = boostSheet.getSubimage(i * 32, 0, 32, 32);
        }
        int j = n / 2 - 1;
        for (int i = n / 2; i < n; ++i) boostAni[i] = boostAni[j--];
        idle = ImageLoader.loadImage("/aircraft/MainAircraft2.png");
    }

    private int getBoostFrameIndex(Player player) {
        double progress = player.getTrailProgress();
        if (player.getPlayerState() == PlayerState.BOOSTING) {
            progress = player.getBoostProgress() * 0.5;
        }
        return (int) (progress * (boostAni.length - 1));
    }

    public void render(Player player, Camera camera, Graphics2D g2D, boolean haveOverlay) {
        Vector2D screenCenter = camera.getScreenPos(player.getPosition());
        double angle = player.getCurrentAngle();
        BufferedImage boost = null;
        if (player.getPlayerState() != PlayerState.BOOSTING) {
            angle = player.getViewAngle();
        }
        if (player.shouldRenderTrail() && !haveOverlay) {
            boostTrail.addTrailFrame(player.getPosition(), angle);
            int aniIndex = getBoostFrameIndex(player);
            boost = boostAni[aniIndex];
        }
        boostTrail.render(idle, camera, g2D, haveOverlay);
        AffineTransform oldTransform = g2D.getTransform();
        g2D.rotate(angle, screenCenter.getX(), screenCenter.getY()); //rotate the Oxy axis
        int screenX = (int) Math.round(screenCenter.getX());
        int screenY = (int) Math.round(screenCenter.getY());
        g2D.drawImage(idle, screenX - PlayerConfig.SPRITE_SIZE / 2,
              screenY - PlayerConfig.SPRITE_SIZE / 2,
              PlayerConfig.SPRITE_SIZE, PlayerConfig.SPRITE_SIZE, null);
        if (boostAni != null) {
            Composite oldComposite = g2D.getComposite();
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
            g2D.drawImage(boost, screenX - 20, screenY + 7, 40, 40, null);
            g2D.setComposite(oldComposite);
        }
        g2D.setTransform(oldTransform);
    }

}


