package com.calderagames.ld37.audio;

import com.badlogic.gdx.audio.Music;

public class MusicInstance {

    private String name;
    private Music music;
    private float volume;
    private float pan;
    private boolean looping;

    public MusicInstance(String name, Music music, float volume, float pan, boolean looping) {
        this.name = name;
        this.music = music;
        this.volume = volume;
        this.pan = pan;
        this.looping = looping;

        music.setLooping(looping);
        music.setPan(pan, volume);
    }

    public String getName() {
        return name;
    }

    public Music getMusic() {
        return music;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        music.setVolume(volume);
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
        music.setLooping(looping);
    }

    public float getPan() {
        return pan;
    }

    public void setPan(float pan) {
        this.pan = pan;
        music.setPan(pan, volume);
    }
}
