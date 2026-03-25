package com.game.shooting2DGame.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SoundManager {
    private static final Logger logger = Logger.getLogger(SoundManager.class.getName());
    private static volatile SoundManager instance;

    private final Map<String, Sound> sounds = new HashMap<>();
    private float sfxVolumePercent = SoundConstants.DEFAULT_SFX_VOLUME;
    private float bgmVolumePercent = SoundConstants.DEFAULT_BGM_VOLUME;

    private SoundManager() {
        initializeSounds();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            synchronized (SoundManager.class) {
                if (instance == null) {
                    instance = new SoundManager();
                }
            }
        }
        return instance;
    }

    private void initializeSounds() {
        sounds.put(SoundConstants.BGM_MENU, new Sound("/sound/MenuBGM.wav", 1));
        sounds.put(SoundConstants.BGM_PLAYING, new Sound("/sound/PlayingBGM.wav", 1));
        sounds.put(SoundConstants.SFX_PLASMA, new Sound("/sound/PlasmaShot.wav", 20));
        sounds.put(SoundConstants.SFX_LASER, new Sound("/sound/Laser.wav", 1));
        sounds.put(SoundConstants.SFX_BOOST, new Sound("/sound/Boost.wav", 1));
        sounds.put(SoundConstants.SFX_EXPLOSION, new Sound("/sound/Explosion.wav", 20));
        sounds.put(SoundConstants.SFX_ELECTRIC, new Sound("/sound/ElectricSpark.wav", 20));
        updateAllVolumes();
    }

    public void playSfx(String name) {
        Sound sound = sounds.get(name);
        if (sound == null) {
            logger.warning("SFX not found: " + name);
            return;
        }
        sound.play();
    }

    public void playBgm(String name) {
        Sound sound = sounds.get(name);
        if (sound == null) {
            logger.warning("BGM not found: " + name);
            return;
        }
        sound.loop();
    }

    public void stop(String name) {
        Sound sound = sounds.get(name);
        if (sound != null) {
            sound.stop();
        }
    }

    public void setSfxVolume(float percent) {
        float clampedPercent = clampPercent(percent);
        if (Math.abs(this.sfxVolumePercent - clampedPercent) < 0.0001f) {
            return;
        }
        this.sfxVolumePercent = clampedPercent;
        updateSfxVolumes();
    }

    public void setBgmVolume(float percent) {
        float clampedPercent = clampPercent(percent);
        if (Math.abs(this.bgmVolumePercent - clampedPercent) < 0.0001f) {
            return;
        }
        this.bgmVolumePercent = clampedPercent;
        updateBgmVolumes();
    }

    public void updateAllVolumes() {
        for (String name : sounds.keySet()) {
            Sound sound = sounds.get(name);
            if (isBgmName(name)) {
                sound.setVolume(bgmVolumePercent);
            } else {
                sound.setVolume(sfxVolumePercent);
            }
        }
    }

    private void updateBgmVolumes() {
        for (String name : sounds.keySet()) {
            if (isBgmName(name)) {
                sounds.get(name).setVolume(bgmVolumePercent);
            }
        }
    }

    private void updateSfxVolumes() {
        for (String name : sounds.keySet()) {
            if (!isBgmName(name)) {
                sounds.get(name).setVolume(sfxVolumePercent);
            }
        }
    }

    public void stopAllSFX() {
        for (String name : sounds.keySet()) {
            if (!isBgmName(name)) {
                sounds.get(name).stop();
            }
        }
    }

    private boolean isBgmName(String name) {
        return name.contains("Music") || name.contains("BGM");
    }

    private float clampPercent(float percent) {
        return Math.max(0f, Math.min(1f, percent));
    }

    public void resetAll() {
        for (Sound sound : sounds.values()) {
            sound.reset();
        }
    }

}