package com.calderagames.ld37.audio;

public interface AudioManager {

    long playSound(String name);

    long playSound(String name, float volume);

    long playSound(String name, float volume, float pitch);

    long playSound(String name, float volume, float pitch, float pan);

    void setVolume(long id, float volume);

    void setPitch(long id, float pitch);

    void setLooping(long id, boolean looping);

    void playMusic(String name, boolean loop);

    void pauseMusic();

    void resumeMusic();

    void stopMusic();

    void update(float delta);

    void setMasterVolume(float volume);

    void setEffectsVolume(float volume);

    void setMusicVolume(float volume);
}
