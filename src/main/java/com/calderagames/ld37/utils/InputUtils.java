package com.calderagames.ld37.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public abstract class InputUtils {

    private InputUtils() {}

    public static Vector2 getMouse(Camera camera) {
        Vector3 pos = camera.unproject(
                new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        return new Vector2((int) pos.x, (int) pos.y);
    }

}
