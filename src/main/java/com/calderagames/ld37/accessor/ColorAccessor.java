package com.calderagames.ld37.accessor;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.Color;

public class ColorAccessor implements TweenAccessor<Color> {

    public static final int R = 0;
    public static final int G = 1;
    public static final int B = 2;
    public static final int A = 3;
    public static final int RGBA = 4;
    public static final int RGB = 5;

    @Override
    public int getValues(Color color, int type, float[] values) {
        switch(type) {
            case RGBA:
                values[0] = color.r;
                values[1] = color.g;
                values[2] = color.b;
                values[3] = color.a;
                return 4;
            case RGB:
                values[0] = color.r;
                values[1] = color.g;
                values[2] = color.b;
                return 3;
            case R:
                values[0] = color.r;
                return 1;
            case G:
                values[0] = color.g;
                return 1;
            case B:
                values[0] = color.b;
                return 1;
            case A:
                values[0] = color.a;
                return 1;
            default:
                throw new UnsupportedOperationException("Unsupported color accessor type " + type);
        }

    }

    @Override
    public void setValues(Color color, int type, float[] values) {
        switch(type) {
            case RGBA:
                color.set(values[0], values[1], values[2], values[3]);
                break;
            case RGB:
                color.set(values[0], values[1], values[2], color.a);
                break;
            case R:
                color.r = values[0];
                break;
            case G:
                color.g = values[0];
                break;
            case B:
                color.b = values[0];
                break;
            case A:
                color.a = values[0];
                break;
            default:
                throw new UnsupportedOperationException("Unsupported color accessor type " + type);
        }
    }
}
