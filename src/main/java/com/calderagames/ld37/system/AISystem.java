package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector2;
import com.calderagames.ld37.system.component.AIComponent;
import com.calderagames.ld37.system.component.PhysicsComponent;
import com.calderagames.ld37.system.component.PositionComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AISystem extends IteratingSystem {

    private static final Logger logger = LogManager.getLogger();

    private ComponentMapper<AIComponent> AIMapper;
    private ComponentMapper<PositionComponent> posMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;

    private final SteeringAcceleration<Vector2> steertingOuput = new SteeringAcceleration<Vector2>(new Vector2());

    public AISystem() {
        super(Aspect.all(AIComponent.class, PositionComponent.class, PhysicsComponent.class));
    }

    @Override
    protected void begin() {
        GdxAI.getTimepiece().update(world.delta);
    }

    @Override
    protected void process(int entityId) {
        AIComponent aiComp = AIMapper.get(entityId);
        PositionComponent pos = posMapper.get(entityId);
        PhysicsComponent physics = physicsMapper.get(entityId);

        aiComp.entity.update(aiComp, pos, physics);
        aiComp.steeringBehavior.calculateSteering(steertingOuput);
        physics.vx = steertingOuput.linear.x;
        physics.vy = steertingOuput.linear.y;

        logger.info(steertingOuput.linear.toString());
    }

}
