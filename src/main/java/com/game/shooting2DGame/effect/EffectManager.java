package com.game.shooting2DGame.effect;

import com.game.shooting2DGame.render.Camera;
import com.game.shooting2DGame.render.EffectRender;
import com.game.shooting2DGame.utils.PoolManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EffectManager {
    private final List<Effect> effects = new ArrayList<>();
    private final EffectRender effectRender;
    private final Object lock = new Object();

    public EffectManager() {
        effectRender = new EffectRender();
    }

    public void update() {
        synchronized (lock) {
            for (int i = 0; i < effects.size(); ++i) {
                Effect effect = effects.get(i);
                effect.updateAnimation();
                if (!effect.isActive()) {
                    int last = effects.size() - 1;
                    effects.set(i, effects.get(last));
                    effects.remove(last);
                    PoolManager.release(effect);
                    i--;
                }
            }
        }
    }

    public void render(Graphics2D g2D, Camera camera) {
        List<Effect> snapshot;
        synchronized (lock) {
            snapshot = new ArrayList<>(effects);
        }
        snapshot.forEach(effect -> effectRender.render(effect, g2D, camera));
    }

    public void addActiveEffect(Effect effect) {
        synchronized (lock) {
            effects.add(effect);
        }
    }
}
