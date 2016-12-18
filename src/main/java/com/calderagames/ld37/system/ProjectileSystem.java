package com.calderagames.ld37.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.component.EnemyComponent;
import com.calderagames.ld37.system.component.PositionComponent;
import com.calderagames.ld37.system.event.HitEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.calderagames.ld37.LD37Game.NATIVE_HEIGHT;
import static com.calderagames.ld37.LD37Game.NATIVE_WIDTH;
import static com.calderagames.ld37.system.component.EnemyComponent.*;

public class ProjectileSystem extends BaseSystem {

    private static final Logger logger = LogManager.getLogger();

    private EventSystem eventSystem;
    private GroupManager groupManager;
    private EntityFactory entityFactory;
    private PlayerSystem playerSystem;
    private CollisionSystem collisionSystem;
    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<EnemyComponent> enemyMapper;
    private ComponentMapper<PositionComponent> posMapper;

    @Override
    protected void processSystem() {
        int playerId = playerSystem.getPlayerId();


        // check enemy projectiles
        IntBag projectiles = groupManager.getEntities("enemy-projectiles");

        for(int i = 0, id; i < projectiles.size(); i++) {
            id = projectiles.get(i);

            if(collisionSystem.isOverlap(id, playerId)) {
                eventSystem.send(new HitEvent(playerId, id, 1));
                entityFactory.removeEntity(id);
            }
        }

        // check player projectiles
        projectiles = groupManager.getEntities("player-arrows");
        IntBag enemies = groupManager.getEntities("enemies");

        for(int i = 0, projId; i < projectiles.size(); i++) {
            projId = projectiles.get(i);

            for(int j = 0, enemyId; j < enemies.size(); j++) {
                enemyId = enemies.get(j);
                if(collisionSystem.isOverlap(projId, enemyId)) {
                    EnemyComponent enemyComp = enemyMapper.get(enemyId);
                    if(isTypeMatchArrow(enemyComp.type, actorMapper.get(projId).actor)) {
                        eventSystem.send(new HitEvent(enemyId, projId, 1));
                    }
                    entityFactory.removeEntity(projId);
                }
            }
        }

        projectiles = groupManager.getEntities("projectiles");

        for(int i = 0, id; i < projectiles.size(); i++) {
            id = projectiles.get(i);

            if(checkOutOfWorld(id)) {
                entityFactory.removeEntity(id);
            }
        }
    }

    private boolean checkOutOfWorld(int id) {
        PositionComponent pos = posMapper.get(id);
        return pos.x < 0 || pos.x > NATIVE_WIDTH || pos.y < 0 || pos.y > NATIVE_HEIGHT;
    }

    private boolean isTypeMatchArrow(int type, Actor arrowActor) {
        return (type == TYPE_YELLOW && arrowActor.getName().equals("arrow-yellow")) ||
               (type == TYPE_BLUE && arrowActor.getName().equals("arrow-blue")) ||
               (type == TYPE_RED && arrowActor.getName().equals("arrow-red"));
    }
}
