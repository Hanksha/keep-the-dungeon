package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.calderagames.ld37.actor.EnemyActor;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.component.EnemyComponent;
import com.calderagames.ld37.utils.Maths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnemySystem extends IteratingSystem {

    private static final Logger logger = LogManager.getLogger();

    private GroupManager groupManager;
    private EntityFactory entityFactory;
    private PlayerSystem playerSystem;
    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<EnemyComponent> enemyMapper;

    private float javelinSpeed = 150;

    private float decisionRate = 2f;

    public EnemySystem() {
        super(Aspect.all(EnemyComponent.class));
    }

    @Override
    protected void process(int entityId) {
        Actor playerActor = playerSystem.getActor();
        EnemyActor enemyActor = (EnemyActor) actorMapper.get(entityId).actor;
        EnemyComponent enemyComp = enemyMapper.get(entityId);

        enemyComp.brainCooldown += world.delta;

        if(enemyComp.brainCooldown >= decisionRate) {
            fireProjectile(enemyActor, playerActor.getX(), playerActor.getY());
            enemyComp.brainCooldown = 0;
        }
    }

    private void fireProjectile(EnemyActor actor, float targetX, float targetY) {
        float angle = Maths.getAngle(actor.getX(), actor.getY(), targetX, targetY) - 90;

        int id = entityFactory.fireProjectile("javelin", actor.getX(), actor.getY(), angle, javelinSpeed);
        groupManager.addTo("enemy-projectiles", id);
    }
}
