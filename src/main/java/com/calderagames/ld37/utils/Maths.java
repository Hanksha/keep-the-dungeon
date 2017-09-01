package com.calderagames.ld37.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;

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

    public static Polygon getPolygon(float x, float y, float originX, float originY,
                              float width, float height, float scaleX, float scaleY,
                              float degrees) {
        float cos = MathUtils.cosDeg(degrees);
        float sin = MathUtils.sinDeg(degrees);
        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;

        if (scaleX != 1 || scaleY != 1) {
            fx *= scaleX;
            fy *= scaleY;
            fx2 *= scaleX;
            fy2 *= scaleY;
        }

        float worldOriginX = x + originX;
        float worldOriginY = y + originY;

        float x1 = cos * fx - sin * fy + worldOriginX;
        float y1 = sin * fx + cos * fy + worldOriginY;

        float x2 = cos * fx2 - sin * fy + worldOriginX;
        float y2 = sin * fx2 + cos * fy + worldOriginY;

        float x3 = cos * fx2 - sin * fy2 + worldOriginX;
        float y3 = sin * fx2 + cos * fy2 + worldOriginY;

        float x4 = x1 + (x3 - x2);
        float y4 = y3 - (y2 - y1);

        Polygon polygon = new Polygon();
        polygon.setVertices(new float[] {x1, y1, x2, y2, x3, y3, x4, y4});
        return polygon;
    }
}
