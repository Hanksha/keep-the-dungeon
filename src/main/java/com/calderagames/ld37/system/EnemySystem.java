package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.component.EnemyComponent;
import com.calderagames.ld37.system.component.PositionComponent;
import com.calderagames.ld37.utils.Maths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnemySystem extends IteratingSystem {

    private static final Logger logger = LogManager.getLogger();

    private GroupManager groupManager;
    private EntityFactory entityFactory;
    private PlayerSystem playerSystem;
    private CollisionSystem collisionSystem;
    private ComponentMapper<PositionComponent> posMapper;
    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<EnemyComponent> enemyMapper;

    private float javelinSpeed = 150;

    private float decisionRate = 2f;

    public EnemySystem() {
        super(Aspect.all(EnemyComponent.class));
    }

    @Override
    protected void process(int entityId) {
        Vector2 playerPos = collisionSystem.getBounds(playerSystem.getPlayerId()).getCenter(new Vector2());
        Vector2 enemyPos = collisionSystem.getBounds(entityId).getCenter(new Vector2());
        EnemyComponent enemyComp = enemyMapper.get(entityId);

        enemyComp.brainCooldown += world.delta;

        if(enemyComp.brainCooldown >= decisionRate) {
            fireProjectile(enemyPos.x, enemyPos.y, playerPos.x, playerPos.y);
            enemyComp.brainCooldown = 0;
        }
    }

    private void fireProjectile(float x, float y, float targetX, float targetY) {
        float angle = Maths.getAngle(x, y, targetX, targetY) - 90;

        int id = entityFactory.fireProjectile("javelin", x, y, angle, javelinSpeed);
        groupManager.addTo("enemy-projectiles", id);
    }
}
