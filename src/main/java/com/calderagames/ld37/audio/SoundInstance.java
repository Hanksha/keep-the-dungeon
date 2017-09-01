package com.calderagames.ld37.audio;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Pool;

public class SoundInstance implements Pool.Poolable {

    private String name;
    private Sound sound;
    private long id;
    private float volume;

    public SoundInstance() {}

    public void init(String name, Sound sound, long id, float volume) {
        this.name = name;
        this.sound = sound;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Sound getSound() {
        return sound;
    }

    public long getId() {
        return id;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        sound.setVolume(id, volume);
    }

    public void setPitch(float pitch) {
        sound.setPitch(id, pitch);
    }

    public void setLooping(boolean looping) {
        sound.setLooping(id, looping);
    }

    @Override
    public void reset() {
        name = null;
        sound = null;
        id = 0;
    }
}
