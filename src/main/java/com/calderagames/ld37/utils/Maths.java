package com.calderagames.ld37.utils;

import com.badlogic.gdx.math.MathUtils;

public abstract class Maths {

    private Maths() {}

    public static float dist(float x0, float y0, float x1, float y1) {
        float distX = x1 - x0;
        float distY = y1 - y0;
        return (float) Math.sqrt(distX * distX + distY * distY);
    }

    public static float getAngle(float x0, float y0, float x1, float y1) {
        float adj = x1 - x0;
        float opp = y1 - y0;
        return MathUtils.radiansToDegrees * MathUtils.atan2(opp, adj);
    }

}
