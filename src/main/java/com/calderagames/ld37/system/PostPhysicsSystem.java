package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.component.PhysicsComponent;

public class PostPhysicsSystem extends IteratingSystem {

    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;

    public PostPhysicsSystem() {
        super(Aspect.all(PhysicsComponent.class, ActorComponent.class));
    }

    @Override
    protected void process(int entityId) {
        ActorComponent actor = actorMapper.get(entityId);
        PhysicsComponent physics = physicsMapper.get(entityId);

        physics.prevX = actor.actor.getX();
        physics.prevY = actor.actor.getY();
        actor.actor.moveBy(physics.vx * world.delta, physics.vy * world.delta);
    }
}
