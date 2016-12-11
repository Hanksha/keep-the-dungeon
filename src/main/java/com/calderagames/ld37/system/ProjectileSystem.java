package com.calderagames.ld37.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.component.EnemyComponent;
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
    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<EnemyComponent> enemyMapper;

    @Override
    protected void processSystem() {
        Actor playerActor = playerSystem.getActor();

        Actor projActor;

        // check projectile out of room
        IntBag projectiles = groupManager.getEntities("projectiles");

        for(int i = 0, id; i < projectiles.size(); i++) {
            id = projectiles.get(i);

            if(!actorMapper.has(id))
                continue;

            projActor = actorMapper.get(id).actor;

            if(isOutStage(projActor))
                entityFactory.removeEntity(id);
        }

        // check player projectiles
        projectiles = groupManager.getEntities("player-arrows");

        EnemyComponent enemyComp;

        for(int i = 0, id; i < projectiles.size(); i++) {
            id = projectiles.get(i);

            if(!actorMapper.has(id))
                continue;

            projActor = actorMapper.get(id).actor;
            Actor actor = entityFactory.getEnemiesGroup().hit(projActor.getX(Align.center), projActor.getY(Align.center), true);

            if(actor != null) {
                entityFactory.removeEntity(id);
                int targetId = (int) actor.getUserObject();

                enemyComp = enemyMapper.get(targetId);

                if(isTypeMatchArrow(enemyComp.type, projActor))
                    eventSystem.send(new HitEvent(targetId, id, 1));
                else
                    logger.info("wrong arrow type");
            }
        }

        // check enemy projectiles
        projectiles = groupManager.getEntities("enemy-projectiles");

        for(int i = 0, id; i < projectiles.size(); i++) {
            id = projectiles.get(i);

            if(!actorMapper.has(id))
                continue;

            projActor = actorMapper.get(id).actor;

            if(playerActor.hit(projActor.getX(), projActor.getY(), true) != null) {
                eventSystem.send(new HitEvent(playerSystem.getPlayerId(), id, 1));
                entityFactory.removeEntity(id);
            }
        }
    }

    private boolean isTypeMatchArrow(int type, Actor arrowActor) {
        return (type == TYPE_YELLOW && arrowActor.getName().equals("arrow-yellow")) ||
               (type == TYPE_BLUE && arrowActor.getName().equals("arrow-blue")) ||
               (type == TYPE_RED && arrowActor.getName().equals("arrow-red"));
    }

    private boolean isOutStage(Actor actor) {
        return actor.getX() < 0 || actor.getX() > NATIVE_WIDTH ||
                actor.getY() < 0 || actor.getY() > NATIVE_HEIGHT;
    }
}
