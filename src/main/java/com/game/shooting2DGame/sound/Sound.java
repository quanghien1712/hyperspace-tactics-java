package com.game.shooting2DGame.sound;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sound {
    private final List<Clip> clips = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(Sound.class.getName());
    private int index;
    private final int poolSize;

    public Sound(String path, int poolSize) {
        this.poolSize = poolSize;
        for (int i = 0; i < poolSize; ++i) {
            try {
                Clip clip = loadClip(path);
                clips.add(clip);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to load sound: " + path, e);
            }
        }
        if (clips.isEmpty()) {
            logger.warning("No Clips loaded for" + path);
        }
    }

    private Clip loadClip (String path) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("Sound file not found: " + path);
            }
            try (AudioInputStream ais = AudioSystem.
                  getAudioInputStream(new BufferedInputStream(is))) {
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                return clip;
            }
        }
    }

    public synchronized void play() {
        if (clips.isEmpty()) return;
        Clip clip = clips.get(index);
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
        index = (index + 1) % poolSize;
    }

    public synchronized void loop() {
        if (clips.isEmpty()) return;
        Clip clip = clips.get(index);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        index = (index + 1) % poolSize;
    }

    public synchronized void stop() {
        clips.forEach(Clip::stop);
    }

    public synchronized void setVolume(float percent) {
        float p = Math.max(0f, Math.min(1f, percent));
        for (Clip clip : clips) {
            try {
                FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB;
                if (p <= 0.0001f) {
                    dB = control.getMinimum();
                } else {
                    dB = (float) (20.0 * Math.log10(p));
                    dB = Math.max(control.getMinimum(), Math.min(control.getMaximum(), dB));
                }
                control.setValue(dB);
            } catch (IllegalArgumentException e) {
                logger.log(Level.FINE, "Clip does not support MASTER_GAIN", e);
            }
        }
    }

    public void reset() {
        for (Clip clip : clips) {
            clip.stop();
            clip.setFramePosition(0);
        }
    }
}
