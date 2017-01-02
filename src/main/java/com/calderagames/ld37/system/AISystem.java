package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.calderagames.ld37.system.component.AIComponent;
import com.calderagames.ld37.system.component.AnimationComponent;
import com.calderagames.ld37.system.component.PhysicsComponent;
import com.calderagames.ld37.system.component.PositionComponent;
import com.calderagames.ld37.system.event.SystemEvent;
import com.calderagames.ld37.system.event.SystemEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AISystem extends IteratingSystem implements SystemEventListener {

    private static final Logger logger = LogManager.getLogger();

    private ComponentMapper<AIComponent> AIMapper;
    private ComponentMapper<PositionComponent> posMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private ComponentMapper<AnimationComponent> animMapper;

    private EntityFactory entityFactory;

    private final SteeringAcceleration<Vector2> steeringOuput = new SteeringAcceleration<Vector2>(new Vector2());

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

        if(aiComp.steeringBehavior != null) {
            aiComp.steeringBehavior.calculateSteering(steeringOuput);
            physics.vx += steeringOuput.linear.x * world.delta;
            physics.vy += steeringOuput.linear.y * world.delta;
        }

        processState(entityId, aiComp.state);
    }

    private void processState(int entityId, int state) {
        AIComponent aiComp = AIMapper.get(entityId);

        switch (state) {
            case STATE_INIT:
                enterState(entityId, STATE_ON_PATH);
                break;
            case STATE_ON_PATH: {
                AnimationComponent animComp = animMapper.get(entityId);
                PhysicsComponent physics = physicsMapper.get(entityId);

                if(physics.vx > 0.5f)
                    animComp.direction = AnimationComponent.Direction.RIGHT;
                else if(physics.vx < -0.5f)
                    animComp.direction = AnimationComponent.Direction.LEFT;
                else if(physics.vy > 0.5f)
                    animComp.direction = AnimationComponent.Direction.UP;
                else if(physics.vy < -0.5f)
                    animComp.direction = AnimationComponent.Direction.DOWN;

                PositionComponent pos = posMapper.get(entityId);
                FollowPath followPath = (FollowPath) aiComp.steeringBehavior;

                if(((Vector2) followPath.getPath().getEndPoint()).dst(pos.x, pos.y) <= 5)
                    entityFactory.removeEntity(entityId);

                break;
            }
            case STATE_ATTACK: {

                break;
            }
        }
    }

    private void enterState(int entityId, int state) {
        AIComponent aiComp = AIMapper.get(entityId);

        switch (state) {
            case STATE_ON_PATH: {

                Array<Vector2> wayPoints = new Array<>();
                wayPoints.add(new Vector2(580, 60));
                wayPoints.add(new Vector2(580, 300));
                wayPoints.add(new Vector2(60, 300));
                wayPoints.add(new Vector2(60, 60));
                wayPoints.add(new Vector2(360, 60));
                LinePath<Vector2> path = new LinePath<>(wayPoints, true);

                FollowPath<Vector2, LinePath.LinePathParam> followPath = new FollowPath<>(aiComp.entity, path, 15);
                aiComp.steeringBehavior = followPath;
                break;
            }
            case STATE_ATTACK: {
                break;
            }

        }
        aiComp.prevState = aiComp.state;
        aiComp.state = state;
    }

    private void exitState(int entityId, int state) {

    }

    @Override
    public void receive(SystemEvent event) {

    }

    public static final int STATE_INIT = 0;
    public static final int STATE_ON_PATH = 1;
    public static final int STATE_ATTACK = 2;
}
