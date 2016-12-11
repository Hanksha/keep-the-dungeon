package com.calderagames.ld37.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.event.HitEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.calderagames.ld37.LD37Game.NATIVE_HEIGHT;
import static com.calderagames.ld37.LD37Game.NATIVE_WIDTH;

public class ProjectileSystem extends BaseSystem {

    private static final Logger logger = LogManager.getLogger();

    private EventSystem eventSystem;
    private GroupManager groupManager;
    private EntityFactory entityFactory;
    private ComponentMapper<ActorComponent> actorMapper;

    @Override
    protected void processSystem() {

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

        projectiles = groupManager.getEntities("player-arrows");

        for(int i = 0, id; i < projectiles.size(); i++) {
            id = projectiles.get(i);

            if(!actorMapper.has(id))
                continue;

            projActor = actorMapper.get(id).actor;
            Actor actor = entityFactory.getEnemiesGroup().hit(projActor.getX(Align.center), projActor.getY(Align.center), true);

            if(actor != null) {
                entityFactory.removeEntity(id);
                eventSystem.send(new HitEvent((int) actor.getUserObject(), id, 1));
            }
        }

    }

    private boolean isOutStage(Actor actor) {
        return actor.getX() < 0 || actor.getX() > NATIVE_WIDTH ||
                actor.getY() < 0 || actor.getY() > NATIVE_HEIGHT;
    }
}
