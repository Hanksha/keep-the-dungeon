package com.calderagames.ld37.accessor;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.math.Vector2;

public class Vector2Accessor implements TweenAccessor<Vector2> {

    public static final int X = 0;
    public static final int Y = 1;
    public static final int XY = 2;

    @Override
    public int getValues(Vector2 vector2, int type, float[] values) {
        switch(type) {
            case X: values[0] = vector2.x; return 1;
            case Y: values[0] = vector2.y; return 1;
            case XY:
                values[0] = vector2.x;
                values[1] = vector2.y;
                return 2;
            default:
                throw new UnsupportedOperationException("Unsupported vector2 accessor type " + type);
        }
    }

    @Override
    public void setValues(Vector2 vector2, int type, float[] values) {
        switch(type) {
            case X: vector2.x = values[0]; break;
            case Y: vector2.y = values[0]; break;
            case XY:
                vector2.x = values[0];
                vector2.y = values[0];
                break;
            default:
                throw new UnsupportedOperationException("Unsupported vector2 accessor type " + type);
        }
    }
}
