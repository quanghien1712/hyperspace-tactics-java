package com.game.shooting2DGame.render;

import com.game.shooting2DGame.config.GameConfig;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;

public class SpaceWarpTransition {
    private static class WarpStar {
        private double angle;
        private double distance;
        private double brightness;
    }

    private static final int STAR_COUNT = 380;
    private static final double DURATION = 7.5;
    private static final double HUD_FLASH_HOLD_SECONDS = 1.5;
    private static final double HUD_FLASH_FADE_SECONDS = 1.0;

    private static final double FLASH_START_T = 0.82;
    private static final double FLASH_FULL_WHITE_T = 0.90;
    private static final double FLASH_HOLD_END_T = 0.95;
    private static final double FLASH_END_T = 1.0;

    private final WarpStar[] stars;
    private final Random random;
    private final double centerX;
    private final double centerY;
    private final double maxRadius;

    private boolean active;
    private double elapsed;

    public SpaceWarpTransition() {
        random = new Random();
        stars = new WarpStar[STAR_COUNT];
        centerX = GameConfig.PANEL_WIDTH / 2.0;
        centerY = GameConfig.PANEL_HEIGHT / 2.0;
        maxRadius = Math.hypot(GameConfig.PANEL_WIDTH, GameConfig.PANEL_HEIGHT) / 2.0 + 120.0;
        initStars();
    }

    private void initStars() {
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new WarpStar();
            resetStar(stars[i], true);
        }
    }

    public void start() {
        elapsed = 0.0;
        active = true;
        for (WarpStar star : stars) {
            resetStar(star, true);
        }
    }

    public void update(double deltaTime) {
        if (!active) {
            return;
        }
        elapsed += deltaTime;
        if (elapsed <= DURATION) {
            double t = getProgress();
            double acceleration = 300.0 + 3200.0 * easeInCubic(t);
            for (WarpStar star : stars) {
                double speedFactor = 0.48 + star.brightness;
                star.distance += acceleration * speedFactor * deltaTime;
                if (star.distance > maxRadius) {
                    resetStar(star, false);
                }
            }
        }

        if (elapsed >= getHudOverlayEndTime()) {
            active = false;
        }
    }

    private double getProgress() {
        return Math.min(1.0, elapsed / DURATION);
    }

    public void render(Graphics2D g2D) {
        if (!active) {
            return;
        }

        if (elapsed > DURATION) {
            return;
        }

        double t = getProgress();
        Object oldAntialias = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        Stroke oldStroke = g2D.getStroke();
        Composite oldComposite = g2D.getComposite();
        Color oldColor = g2D.getColor();

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderStarTrails(g2D, t);
        renderCoreGlow(g2D, t);
        renderWhiteBurst(g2D, t);

        g2D.setColor(oldColor);
        g2D.setStroke(oldStroke);
        g2D.setComposite(oldComposite);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialias);
    }

    public boolean isActive() {
        return active;
    }

    public float getHudFlashOverlayAlpha() {
        if (!active) {
            return 0f;
        }

        double start = DURATION * FLASH_FULL_WHITE_T;
        if (elapsed < start) {
            return 0f;
        }
        if (elapsed < start + HUD_FLASH_HOLD_SECONDS) {
            return 1f;
        }
        double fadeElapsed = elapsed - start - HUD_FLASH_HOLD_SECONDS;
        if (fadeElapsed < HUD_FLASH_FADE_SECONDS) {
            return (float) (1.0 - clamp01(fadeElapsed / HUD_FLASH_FADE_SECONDS));
        }
        return 0f;
    }

    public boolean shouldRevealGameplay() {
        return active && elapsed >= DURATION * FLASH_HOLD_END_T;
    }

    private void renderStarTrails(Graphics2D g2D, double t) {
        if (t >= FLASH_START_T) {
            return;
        }
        double warpStrength = easeOutCubic(t);
        for (WarpStar star : stars) {
            double dx = Math.cos(star.angle);
            double dy = Math.sin(star.angle);
            double trailLength = 10.0 + (42.0 + star.brightness * 30.0) * warpStrength;
            trailLength += star.distance * 0.055 * warpStrength;
            double startDistance = Math.max(0.0, star.distance - trailLength);
            double x1 = centerX + dx * startDistance;
            double y1 = centerY + dy * startDistance;
            double x2 = centerX + dx * star.distance;
            double y2 = centerY + dy * star.distance;

            float alpha = (float) Math.min(1.0, (0.35 + star.brightness * 0.65) * (0.60 + warpStrength * 0.7));
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            float width = (float) (0.8 + star.brightness * 1.8 + warpStrength * 1.1);
            g2D.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2D.setColor(Color.WHITE);
            g2D.draw(new Line2D.Double(x1, y1, x2, y2));
        }
    }

    private void renderCoreGlow(Graphics2D g2D, double t) {
        double radius;
        if (t < 0.7) {
            radius = 24.0 + t * 155.0;
        } else {
            double burst = (t - 0.7) / 0.3;
            radius = 132.0 + burst * burst * 980.0;
        }
        float glowAlpha = (float) Math.min(1.0, 0.58 + t);

        RadialGradientPaint glow = new RadialGradientPaint(
              new Point2D.Float((float) centerX, (float) centerY),
              (float) radius,
              new float[]{0f, 1f},
              new Color[]{
                    new Color(255, 255, 255, (int) (255 * glowAlpha)),
                    new Color(255, 255, 255, 0)
              }
        );

        Paint oldPaint = g2D.getPaint();
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2D.setPaint(glow);
        int size = (int) Math.round(radius * 2.0);
        g2D.fillOval((int) Math.round(centerX - radius), (int) Math.round(centerY - radius), size, size);
        g2D.setPaint(oldPaint);
    }

    private void renderWhiteBurst(Graphics2D g2D, double t) {
        float alpha = getFlashAlpha(t);
        if (alpha <= 0f) {
            return;
        }

        if (isFlashPeakWhitePhase(t)) {
            g2D.setComposite(AlphaComposite.Src);
        } else {
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, alpha)));
        }
        g2D.setColor(Color.WHITE);
        g2D.fillRect(0, 0, GameConfig.PANEL_WIDTH, GameConfig.PANEL_HEIGHT);
    }

    private void resetStar(WarpStar star, boolean fillField) {
        star.angle = random.nextDouble() * Math.PI * 2.0;
        star.distance = fillField ? random.nextDouble() * maxRadius : random.nextDouble() * 14.0;
        star.brightness = 0.25 + random.nextDouble() * 0.75;
    }

    private double getHudOverlayEndTime() {
        return DURATION * FLASH_FULL_WHITE_T + HUD_FLASH_HOLD_SECONDS + HUD_FLASH_FADE_SECONDS;
    }

    private float getFlashAlpha(double t) {
        if (t <= FLASH_START_T) {
            return 0f;
        }
        if (t < FLASH_FULL_WHITE_T) {
            double x = (t - FLASH_START_T) / (FLASH_FULL_WHITE_T - FLASH_START_T);
            return (float) clamp01(x);
        }
        if (t < FLASH_HOLD_END_T) {
            return 1f;
        }
        if (t < FLASH_END_T) {
            double x = (t - FLASH_HOLD_END_T) / (FLASH_END_T - FLASH_HOLD_END_T);
            return (float) (1.0 - clamp01(x));
        }
        return 0f;
    }

    private boolean isFlashPeakWhitePhase(double t) {
        return t >= FLASH_FULL_WHITE_T && t < FLASH_HOLD_END_T;
    }

    private double clamp01(double x) {
        return Math.max(0.0, Math.min(1.0, x));
    }

    private double easeInCubic(double x) {
        return x * x * x;
    }

    private double easeOutCubic(double x) {
        double inverted = 1.0 - x;
        return 1.0 - inverted * inverted * inverted;
    }


}

