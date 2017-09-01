package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.calderagames.ld37.system.component.PhysicsComponent;
import com.calderagames.ld37.system.component.PositionComponent;

public class PostPhysicsSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> posMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;

    public PostPhysicsSystem() {
        super(Aspect.all(PhysicsComponent.class, PositionComponent.class));
    }

    @Override
    protected void process(int entityId) {
        PositionComponent pos = posMapper.get(entityId);
        PhysicsComponent physics = physicsMapper.get(entityId);

        pos.oldX = pos.x;
        pos.oldY = pos.y;
        pos.x += physics.vx * world.delta;
        pos.y += physics.vy * world.delta;
    }
}
