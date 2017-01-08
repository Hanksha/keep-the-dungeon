package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.utils.Timer;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.component.EnemyComponent;
import com.calderagames.ld37.system.component.PositionComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnemySystem extends IteratingSystem {

    private static final Logger logger = LogManager.getLogger();

    public static final float DEFAULT_LINEAR_ACCELERATION = 400;

    private GroupManager groupManager;
    private EntityFactory entityFactory;
    private AISystem aiSystem;
    private PlayerSystem playerSystem;
    private CollisionSystem collisionSystem;
    private ComponentMapper<PositionComponent> posMapper;
    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<EnemyComponent> enemyMapper;

    public EnemySystem() {
        super(Aspect.all(EnemyComponent.class));
    }

    @Override
    protected void initialize() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                MessageManager.getInstance().dispatchMessage(AISystem.ATTACK);
            }
        }, 5, 4);
    }

    @Override
    protected void process(int entityId) {
    }

}
