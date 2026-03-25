package com.game.shooting2DGame.render;

import com.game.shooting2DGame.effect.Effect;
import com.game.shooting2DGame.effect.EffectType;
import com.game.shooting2DGame.utils.ImageLoader;
import com.game.shooting2DGame.utils.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class EffectRender {
    private Map<EffectType, BufferedImage> effects = new HashMap<>();

    public EffectRender() {
        loadAnimation();
    }

    private void loadAnimation() {
        effects.put(EffectType.BULLET_EXPLOSION, ImageLoader.loadImage("/effect/BulletExplosion.png"));
        effects.put(EffectType.PLASMA_EXPLOSION, ImageLoader.loadImage("/effect/PlasmaExplosion.png"));
        effects.put(EffectType.DEATH, ImageLoader.loadImage("/effect/Death.png"));
    }

    public void render(Effect effect, Graphics2D g2D, Camera camera) {
        BufferedImage effectSheet = effects.get(effect.getEffectType());
        int aniIndex = effect.getAniIndex();
        int frameSize = effectSheet.getHeight();
        int width = effect.getWidth();
        int height = effect.getHeight();
        BufferedImage effectImg =
              effectSheet.getSubimage(frameSize * aniIndex, 0, frameSize, frameSize);
        Vector2D screenPos = camera.getScreenPos(effect.getPosition());
        g2D.drawImage(effectImg, (int)(screenPos.getX() - width / 2.0) ,
              (int)(screenPos.getY() - height / 2.0), width, height, null);

    }
}
