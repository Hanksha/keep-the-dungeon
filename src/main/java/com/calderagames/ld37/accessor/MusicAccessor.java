package com.calderagames.ld37.accessor;

import aurelienribon.tweenengine.TweenAccessor;
import com.calderagames.ld37.audio.MusicInstance;

public class MusicAccessor implements TweenAccessor<MusicInstance> {

    public static final int VOLUME = 0;

    @Override
    public int getValues(MusicInstance musicInstance, int type, float[] values) {
        switch (type) {
            case VOLUME:
                values[0] = musicInstance.getVolume();
                return 1;
            default:
                throw new UnsupportedOperationException("Unsupported music accessor type " + type);
        }
    }

    @Override
    public void setValues(MusicInstance musicInstance, int type, float[] values) {
        switch (type) {
            case VOLUME:
                musicInstance.setVolume(values[0]);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported music accessor type " + type);
        }
    }
}
