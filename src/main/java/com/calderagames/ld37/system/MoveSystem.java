package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.calderagames.ld37.system.component.MoveComponent;
import com.calderagames.ld37.system.component.PhysicsComponent;

public class MoveSystem extends IteratingSystem {

    private ComponentMapper<PhysicsComponent> physicsMapper;
    private ComponentMapper<MoveComponent> moveMapper;

    public MoveSystem() {
        super(Aspect.all(MoveComponent.class, PhysicsComponent.class));
    }

    @Override
    protected void process(int entityId) {
        PhysicsComponent physics = physicsMapper.get(entityId);
        MoveComponent move = moveMapper.get(entityId);

        if(move.left) {
            physics.vx -= move.xAcc * world.delta;
        }
        else if(move.right) {
            physics.vx += move.xAcc * world.delta;
        }

        if(move.up)
            physics.vy += move.yAcc * world.delta;
        else if(move.down)
            physics.vy -= move.yAcc * world.delta;
    }
}
