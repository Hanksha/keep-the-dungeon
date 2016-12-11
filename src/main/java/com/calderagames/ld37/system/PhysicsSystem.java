package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.calderagames.ld37.system.component.PhysicsComponent;

public class PhysicsSystem extends IteratingSystem {

    private ComponentMapper<PhysicsComponent> physicsMapper;

    public PhysicsSystem() {
        super(Aspect.all(PhysicsComponent.class));
    }

    @Override
    protected void process(int entityId) {
        PhysicsComponent physics = physicsMapper.get(entityId);

        if(Math.abs(physics.vx) > 0.5f) {
            if(physics.vx > 0)
                physics.vx = Math.max(physics.vx - physics.vx * physics.frictionX * world.delta, 0);
            else if(physics.vx < 0)
                physics.vx = Math.min(physics.vx - physics.vx * physics.frictionX * world.delta, 0);
        }
        else
            physics.vx = 0;

        if(Math.abs(physics.vy) > 0.5f) {
            if(physics.vy > 0)
                physics.vy =  Math.max(physics.vy - physics.vy * physics.frictionY * world.delta, 0);
            else if(physics.vy < 0)
                physics.vy =  Math.min(physics.vy - physics.vy * physics.frictionY * world.delta, 0);
        }
        else
            physics.vy = 0;
    }

}
