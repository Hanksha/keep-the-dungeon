package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.component.PositionComponent;

public class ActorSystem extends IteratingSystem {

    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<PositionComponent> posMapper;

    public ActorSystem() {
        super(Aspect.all(PositionComponent.class, ActorComponent.class));
    }

    @Override
    protected void process(int entityId) {
        ActorComponent actorComp = actorMapper.get(entityId);
        PositionComponent pos = posMapper.get(entityId);

        // check if actor is on a path
        for(Action action: actorComp.actor.getActions()) {
            if(action instanceof SequenceAction) {
                pos.oldX = pos.x;
                pos.x = actorComp.actor.getX() + actorComp.alignX;
                pos.oldY = pos.y;
                pos.y = actorComp.actor.getY() + actorComp.alignX;
                return;
            }
        }

        actorComp.actor.setPosition(pos.x - actorComp.alignX, pos.y - actorComp.alignY);
    }
}
