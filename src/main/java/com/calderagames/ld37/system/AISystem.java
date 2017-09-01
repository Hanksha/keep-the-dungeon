package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector2;
import com.calderagames.ld37.ai.AIEntity;
import com.calderagames.ld37.system.component.*;
import com.calderagames.ld37.system.event.SystemEvent;
import com.calderagames.ld37.system.event.SystemEventListener;
import com.calderagames.ld37.utils.Maths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AISystem extends IteratingSystem implements SystemEventListener {

    private static final Logger logger = LogManager.getLogger();

    public static final int ATTACK = 0;
    public static final int FIRE = 1;

    public ComponentMapper<AIComponent> AIMapper;
    public ComponentMapper<PositionComponent> posMapper;
    public ComponentMapper<PhysicsComponent> physicsMapper;
    public ComponentMapper<AnimationComponent> animMapper;
    public ComponentMapper<CollisionComponent> collisionMapper;

    public EntityFactory entityFactory;
    public PlayerSystem playerSystem;
    public CollisionSystem collisionSystem;
    public EventSystem eventSystem;
    private GroupManager groupManager;

    private final SteeringAcceleration<Vector2> steeringOuput = new SteeringAcceleration<Vector2>(new Vector2());

    public Vector2 playerPos;

    public AISystem() {
        super(Aspect.all(AIComponent.class, PositionComponent.class, PhysicsComponent.class));
    }

    @Override
    protected void initialize() {
        playerPos = new Vector2();
    }

    @Override
    protected void removed(int entityId) {
        AIComponent aiComp = AIMapper.get(entityId);
        aiComp.fsm.getCurrentState().exit(aiComp.fsm.getOwner());
    }

    @Override
    protected void begin() {
        collisionSystem.getBounds(playerSystem.getPlayerId()).getCenter(playerPos);

        GdxAI.getTimepiece().update(world.delta);
        MessageManager.getInstance().update();
    }

    @Override
    protected void process(int entityId) {
        AIComponent aiComp = AIMapper.get(entityId);
        PositionComponent pos = posMapper.get(entityId);
        PhysicsComponent physics = physicsMapper.get(entityId);

        aiComp.steeringEntity.update(aiComp, pos, physics);

        if(aiComp.steeringBehavior != null) {
            aiComp.steeringBehavior.calculateSteering(steeringOuput);
            physics.vx += steeringOuput.linear.x * world.delta;
            physics.vy += steeringOuput.linear.y * world.delta;
        }

        aiComp.fsm.update();
    }

    @Override
    public void receive(SystemEvent event) {

    }

    public void changeGlobalState(int entityId, State<AIEntity> state) {
        AIComponent aiComp = AIMapper.get(entityId);

        if(aiComp.fsm.getGlobalState() != null)
            aiComp.fsm.getGlobalState().exit(aiComp.fsm.getOwner());

        state.enter(aiComp.fsm.getOwner());
        aiComp.fsm.setGlobalState(state);
    }

    public void fireProjectile(float x, float y, float targetX, float targetY, float speed) {
        float angle = Maths.getAngle(x, y, targetX, targetY) - 90;

        int id = entityFactory.fireProjectile("javelin", x, y, angle, speed);
        groupManager.addTo("enemy-projectiles", id);
    }

}
