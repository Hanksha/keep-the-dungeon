package com.calderagames.ld37.system.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorComponent extends PooledComponent {

    public Actor actor;
    public float alignX;
    public float alignY;

    @Override
    protected void reset() {
        actor = null;
        alignX = 0;
        alignY = 0;
    }

}
