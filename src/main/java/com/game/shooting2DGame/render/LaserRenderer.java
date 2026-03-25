package com.game.shooting2DGame.render;

import com.game.shooting2DGame.projectiles.Laser;
import com.game.shooting2DGame.utils.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LaserRenderer {
    private final Random random = new Random();
    private final Map<Integer, LinearGradientPaint> cache = new HashMap<>();

    public void render(Laser laser, Camera camera, Graphics2D g2D) {
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        double dw = laser.getRenderWidth();
        Vector2D screenPos = camera.getScreenPos(laser.getPosition());
        double angle = laser.getCurrentAngle() - Math.PI / 2;
        AffineTransform oldTransform = g2D.getTransform();
        g2D.translate(screenPos.getX(), screenPos.getY());
        g2D.rotate(angle);
        drawBeam(g2D, laser.getLength(), (float)dw);
        g2D.setTransform(oldTransform);
    }

    private void drawBeam(Graphics2D g2D, int length, float dw) {
        float flicker = (dw > 4f) ? (0.88f + random.nextFloat() * 0.12f) : 1f;

        paintGradientBeam(g2D, length, Math.round(dw * 4f),    new Color(10,  30,  220, 0), new Color(10,  30,  220, 60));
        paintGradientBeam(g2D, length, Math.round(dw * 2.5f), new Color(30,  90,  230, 0), new Color(30,  90,  230, 160));
        paintGradientBeam(g2D, length, Math.round(dw * 1.6f), new Color(60,  150, 255, 0), new Color(60,  150, 255, 210));
        paintGradientBeam(g2D, length, Math.round(dw), new Color(140, 200, 255, 0), new Color(140, 200, 255, (int)(230 * flicker)));

        paintInnerCore(g2D, length, dw, flicker);
    }

    private void paintInnerCore(Graphics2D g2d, int length, float dw, float flicker) {
        float widthPulse = 0.96f + random.nextFloat() * 0.08f;
        float coreW = Math.max(1.1f, dw * 1.34f * widthPulse);
        float coreY = -coreW / 2f;

        LinearGradientPaint coreLong = cache.computeIfAbsent(1, k ->
              new LinearGradientPaint(
                    0f, 0f, length, 0f,
                    new float[]{0f, 0.06f, 0.35f, 0.75f, 1f},
                    new Color[]{
                          new Color(255, 255, 255, Math.min(255, (int) (248 * flicker))),
                          new Color(255, 255, 255, Math.min(255, (int) (238 * flicker))),
                          new Color(240, 248, 255, Math.min(255, (int) (228 * flicker))),
                          new Color(225, 240, 255, Math.min(255, (int) (206 * flicker))),
                          new Color(205, 230, 255, Math.min(255, (int) (170 * flicker)))
                    }
              ));
        g2d.setPaint(coreLong);
        g2d.fill(new RoundRectangle2D.Float(0f, coreY, length, coreW, coreW, coreW));

        float filamentW = Math.max(0.8f, coreW * 0.36f);
        float filamentY = -filamentW / 2f;

        LinearGradientPaint filamentLong = cache.computeIfAbsent(1, k ->
              new LinearGradientPaint(
                    0f, 0f, length, 0f,
                    new float[]{0f, 0.12f, 0.7f, 1f},
                    new Color[]{
                          new Color(255, 255, 255, Math.min(255, (int) (255 * flicker))),
                          new Color(255, 255, 255, Math.min(255, (int) (248 * flicker))),
                          new Color(245, 252, 255, Math.min(255, (int) (232 * flicker))),
                          new Color(230, 244, 255, Math.min(255, (int) (198 * flicker)))
                    }
              ));
        g2d.setPaint(filamentLong);
        g2d.fill(new RoundRectangle2D.Float(0f, filamentY, length, filamentW, filamentW, filamentW));
    }


    private void paintGradientBeam(Graphics2D g2d, int length, int beamW, Color edgeColor, Color centerColor) {
        if (beamW < 1) return;
        GradientPaint gp = new GradientPaint(0, -(beamW / 2f), edgeColor, 0, 0, centerColor, true);
        g2d.setPaint(gp);
        g2d.fillRect(0, -beamW / 2, length, beamW);
    }
}
