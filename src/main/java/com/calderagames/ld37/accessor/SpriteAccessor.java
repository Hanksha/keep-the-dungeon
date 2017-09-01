package com.calderagames.ld37.accessor;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteAccessor implements TweenAccessor<Sprite> {

    public static final int POSITION = 0;
    public static final int X = 1;
    public static final int Y = 2;
    public static final int COLOR = 3;
    public static final int SCALE = 4;
    public static final int ROTATION = 5;

    @Override
    public int getValues(Sprite sprite, int type, float[] values) {
        switch (type) {
            case POSITION:
                values[0] = sprite.getX() - sprite.getWidth() / 2;
                values[1] = sprite.getY() - sprite.getHeight() / 2;
                return 2;
            case X:
                values[0] = sprite.getX() - sprite.getWidth() / 2;
                return 1;
            case Y:
                values[0] = sprite.getY() - sprite.getHeight() / 2;
                return 1;
            case COLOR:
                Color color = sprite.getColor();
                values[0] = color.r;
                values[1] = color.g;
                values[2] = color.b;
                values[3] = color.a;
                return 4;
            case SCALE:
                values[0] = sprite.getScaleX();
                values[1] = sprite.getScaleY();
                return 2;
            case ROTATION:
                values[0] = sprite.getRotation();
                return 1;
            default:
                throw new UnsupportedOperationException("Unsupported sprite accessor type " + type);
        }
    }

    @Override
    public void setValues(Sprite sprite, int type, float[] values) {
        switch (type) {
            case POSITION:
                sprite.setCenter(values[0], values[1]);
                break;
            case X:
                sprite.setCenter(values[0], sprite.getY() + sprite.getHeight() / 2);
                break;
            case Y:
                sprite.setCenter(sprite.getX() + sprite.getWidth() / 2, values[0]);
                break;
            case COLOR:
                sprite.setColor(values[0], values[1], values[2], values[3]);
                break;
            case SCALE:
                sprite.setScale(values[0], values[1]);
                break;
            case ROTATION:
                sprite.setRotation(values[0]);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported sprite accessor type " + type);
        }
    }
}
