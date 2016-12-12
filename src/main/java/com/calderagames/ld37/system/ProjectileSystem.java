package com.calderagames.ld37.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.calderagames.ld37.actor.CollisionActor;
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
        CollisionActor playerActor = playerSystem.getActor().findActor("collision");
        Polygon playerPoly = playerActor.getBounds();

        Group projActor;

        // check projectile out of room
        IntBag projectiles = groupManager.getEntities("projectiles");

        for(int i = 0, id; i < projectiles.size(); i++) {
            id = projectiles.get(i);

            if(!actorMapper.has(id))
                continue;

            projActor = (Group) actorMapper.get(id).actor;

            /*if(isOutStage(projActor))
                entityFactory.removeEntity(id);*/
        }

        // check player projectiles
        projectiles = groupManager.getEntities("player-arrows");
        IntBag enemies = groupManager.getEntities("enemies");
        EnemyComponent enemyComp;

        for(int i = 0, id; i < projectiles.size(); i++) {
            id = projectiles.get(i);

            if(!actorMapper.has(id))
                continue;

            projActor = (Group) actorMapper.get(id).actor;
            Polygon p1 = ((CollisionActor) projActor.findActor("collision")).getBounds();

            int targetId = checkHit(enemies, p1.getBoundingRectangle());

            if(targetId != -1) {

                if(!enemyMapper.has(targetId))
                    continue;

                enemyComp = enemyMapper.get(targetId);

                if(isTypeMatchArrow(enemyComp.type, projActor)) {
                    eventSystem.send(new HitEvent(targetId, id, 1));
                }
                else
                    logger.info("wrong arrow type");

                entityFactory.removeEntity(id);
            }
        }

        // check enemy projectiles
        projectiles = groupManager.getEntities("enemy-projectiles");

        for(int i = 0, id; i < projectiles.size(); i++) {
            id = projectiles.get(i);

            if(!actorMapper.has(id))
                continue;
            projActor = (Group) actorMapper.get(id).actor;
            Polygon p1 = ((CollisionActor) projActor.findActor("collision")).getBounds();

            if(p1.getBoundingRectangle().overlaps(playerPoly.getBoundingRectangle())) {
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

    private int checkHit(IntBag entities, Rectangle bounds) {
        for(int i = 0, id; i < entities.size(); i++) {
            id = entities.get(i);

            if(!actorMapper.has(id))
                continue;

            CollisionActor actor = ((Group) actorMapper.get(id).actor).findActor("collision");

            if(actor.getBounds().getBoundingRectangle().overlaps(bounds))
                return id;
        }

        return -1;
    }

    private boolean isOutStage(Actor actor) {
        return (actor.getX(Align.center) < 0 || actor.getX(Align.center) > NATIVE_WIDTH) &&
                (actor.getY(Align.center) < 0 || actor.getY(Align.center) > NATIVE_HEIGHT);
    }

    public void drawDebug(ShapeRenderer renderer) {
        IntBag entities = groupManager.getEntities("projectiles");
        CollisionActor actor;

        for(int i = 0, id; i < entities.size(); i++) {
            id = entities.get(i);
            if(!actorMapper.has(id))
                continue;

            actor = ((Group) actorMapper.get(id).actor).findActor("collision");

            renderer.polygon(actor.getBounds().getTransformedVertices());
        }

        actor = playerSystem.getActor().findActor("collision");

        renderer.polygon(actor.getBounds().getTransformedVertices());
    }
}
