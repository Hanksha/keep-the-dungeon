package com.calderagames.ld37.system.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.calderagames.ld37.actor.BaseActor;

public class ActorComponent extends PooledComponent {

    public Actor actor;

    @Override
    protected void reset() {
        actor = null;
    }

}
