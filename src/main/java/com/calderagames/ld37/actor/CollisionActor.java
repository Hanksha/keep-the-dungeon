package com.calderagames.ld37.actor;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CollisionActor extends Actor {

    public Polygon getBounds() {
        Polygon polygon = new Polygon(new float[] {0, 0, getWidth(), 0, getWidth(), getHeight(), 0, getHeight()});
        Vector2 pos = localToStageCoordinates(new Vector2());
        polygon.setPosition(pos.x, pos.y);
        polygon.setOrigin(getOriginX(), getOriginY());
        polygon.setRotation(getRotation());
        return polygon;
    }

}
