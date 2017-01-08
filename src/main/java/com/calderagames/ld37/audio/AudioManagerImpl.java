package com.calderagames.ld37.audio;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.audio.OpenALAudio;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.Pool;
import com.calderagames.ld37.accessor.MusicAccessor;
import com.google.inject.Inject;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.openal.AL10;

import java.lang.reflect.Field;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class AudioManagerImpl implements AudioManager {

    private static final Logger logger = LogManager.getLogger();

    private final String soundEffectsFolder = "audio/effects/";
    private final String musicFolder = "audio/music/";

    private float effectsVol = 1;
    private float musicVol = 1;
    private float masterVol = 1;

    private Pool<SoundInstance> soundPool;
    private LongMap<SoundInstance> soundIdToInstance;
    private LongMap<Integer> soundIdToSourceId;

    private String musicNameToLoad;
    private boolean musicLoading;
    private boolean musicLooping;
    private boolean musicStopping;
    private MusicInstance musicInstance;

    @Inject
    private AssetManager assets;

    @Inject
    private TweenManager tweenManager;

    public AudioManagerImpl() {
        soundPool = new SoundInstancePool();
        soundIdToInstance = new LongMap<>();

        try {
            Field field =  FieldUtils.getField(OpenALAudio.class, "soundIdToSource", true);

            if(field != null)
                soundIdToSourceId = (LongMap<Integer>) field.get(Gdx.audio);
            else
                logger.error("field \"soundIdToSource\" doesn't exist for OpenALAudio");

        } catch(IllegalAccessException e) {
            logger.error("could not access soundIdToSource map in OpenALAudio instance", e.getCause());
        }
    }

    public void update(float delta) {
        updateEffects();
        updateMusic();
    }

    private void updateEffects() {
        Iterator<LongMap.Entry<SoundInstance>> iter = soundIdToInstance.iterator();

        LongMap.Entry<SoundInstance> entry;
        while(iter.hasNext()) {
            entry = iter.next();

            if(AL10.alGetSourcei(soundIdToSourceId.get(entry.key), AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED) {
                logger.debug(String.format("sound %s id %s stopped", entry.value.getName(), entry.key));
                iter.remove();
                soundPool.free(entry.value);
            }
        }
    }

    private void updateMusic() {
        if(musicLoading && !musicStopping && assets.isLoaded(musicFolder + musicNameToLoad)) {
            Music music = assets.get(musicFolder + musicNameToLoad);
            musicInstance = new MusicInstance(musicNameToLoad, music, masterVol * musicVol, 0, musicLooping);
            music.play();
            musicLoading = false;
            logger.debug(String.format("Finished loading music %s, now playing", musicInstance.getName()));
        }
    }

    @Override
    public void setMasterVolume(float volume) {
        masterVol = volume;
        updateMusicVolume();
        updateEffectsVolume();
    }

    @Override
    public void setEffectsVolume(float volume) {
        effectsVol = volume;
        updateEffectsVolume();
    }

    @Override
    public void setMusicVolume(float volume) {
        musicVol = volume;
        updateMusicVolume();
    }

    private void updateEffectsVolume() {
        for(SoundInstance instance: soundIdToInstance.values()) {
            instance.setVolume(instance.getVolume() * masterVol * effectsVol);
        }
    }

    private void updateMusicVolume() {
        if(musicInstance == null || musicLoading)
            return;

        musicInstance.setVolume(masterVol * musicVol);
    }

    @Override
    public long playSound(String name) {
        return playSound(name, 1, 1, 0);
    }

    @Override
    public long playSound(String name, float volume) {
        return playSound(name, volume, 1, 0);
    }

    @Override
    public long playSound(String name, float volume, float pitch) {
        return playSound(name, volume, pitch, 0);
    }

    @Override
    public long playSound(String name, float volume, float pitch, float pan) {
        Sound sound = assets.get(soundEffectsFolder + name);
        long id = sound.play(volume * effectsVol * masterVol, pitch, pan);
        SoundInstance instance = soundPool.obtain();
        instance.init(name, sound, id, volume);
        logger.debug(String.format("playing %s id %s", instance.getName(), instance.getId()));
        soundIdToInstance.put(instance.getId(), instance);
        return instance.getId();
    }

    @Override
    public void setVolume(long id, float volume) {
        soundIdToInstance.get(id).setVolume(volume);
    }

    @Override
    public void setPitch(long id, float pitch) {
        soundIdToInstance.get(id).setPitch(pitch);
    }

    @Override
    public void setLooping(long id, boolean looping) {
        soundIdToInstance.get(id).setLooping(looping);
    }

    @Override
    public void playMusic(String name, boolean loop) {
        if(musicLoading)
            return;

        musicNameToLoad = name;
        assets.load(musicFolder + musicNameToLoad, Music.class);
        musicLoading = true;
        musicLooping = loop;
        logger.debug("Started loading music " + musicNameToLoad);

        if(musicInstance != null)
            stopMusic();
    }

    @Override
    public void pauseMusic() {
        musicInstance.getMusic().pause();
    }

    @Override
    public void resumeMusic() {
        musicInstance.getMusic().play();
    }

    @Override
    public void stopMusic() {
        Tween.to(musicInstance, MusicAccessor.VOLUME, 2)
                .target(0)
                .ease(TweenEquations.easeInOutSine)
                .setCallback((type, tween) -> {
                    musicInstance.getMusic().stop();
                    assets.unload(musicFolder + musicInstance.getName());
                    logger.debug("Stopped music " + musicInstance.getName());
                    musicInstance = null;
                    musicStopping = false;
                })
                .start(tweenManager);
        musicStopping = true;
    }

    public void setMusicLooping(boolean looping) {
        if(musicInstance == null || musicLoading)
            return;

        musicInstance.setLooping(looping);
    }

    private class SoundInstancePool extends Pool<SoundInstance> {
        @Override
        protected SoundInstance newObject() {
            return new SoundInstance();
        }
    }

}
