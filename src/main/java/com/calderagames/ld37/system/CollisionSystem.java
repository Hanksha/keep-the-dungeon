package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.component.CollisionComponent;
import com.calderagames.ld37.system.component.PhysicsComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.calderagames.ld37.system.RoomSystem.TILE_SIZE;

public class CollisionSystem extends IteratingSystem {

    private static final Logger logger = LogManager.getLogger();

    private RoomSystem roomSystem;
    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;

    public CollisionSystem() {
        super(Aspect.all(PhysicsComponent.class, ActorComponent.class, CollisionComponent.class));
    }

    @Override
    protected void process(int entityId) {
        Actor actor = actorMapper.get(entityId).actor;
        PhysicsComponent physics = physicsMapper.get(entityId);
        CollisionComponent collision = collisionMapper.get(entityId);

        if(physics.vx == 0 && physics.vy == 0)
            return;

        Vector2 prevPos = new Vector2(actor.getX(), actor.getY());
        Vector2 nextPos = new Vector2(actor.getX(), actor.getY());
        Rectangle bounds = new Rectangle();
        bounds.width = collision.width;
        bounds.height = collision.height;

        boolean collidedX = false;
        boolean collidedY = false;

        // move x axis first;
        nextPos.x += physics.vx * world.delta;
        if(physics.vx != 0) {
            bounds.x = nextPos.x + collision.offX - collision.width / 2;
            bounds.y = nextPos.y + collision.offY;

            if(roomSystem.isTilePosition("collision", bounds.x, bounds.y) ||
               roomSystem.isTilePosition("collision", bounds.x, bounds.y + bounds.height - 1)) {
                nextPos.x = (int) ((bounds.x) / TILE_SIZE) * TILE_SIZE + TILE_SIZE + bounds.width / 2;
                collidedX = true;
            }
            else if(roomSystem.isTilePosition("collision", bounds.x + bounds.width, bounds.y) ||
                    roomSystem.isTilePosition("collision", bounds.x + bounds.width, bounds.y + bounds.height - 1)) {
                nextPos.x = (int) ((bounds.x + bounds.width) / TILE_SIZE) * TILE_SIZE - bounds.width / 2;
                collidedX = true;
            }

            if(collidedX) {
                actor.moveBy(nextPos.x - prevPos.x, 0);
                physics.vx = 0;
            }
        }

        // move y axis
        nextPos.y += physics.vy * world.delta;
        if(physics.vy != 0) {
            bounds.x = nextPos.x + collision.offX - collision.width / 2;
            bounds.y = nextPos.y + collision.offY;

            if(roomSystem.isTilePosition("collision", bounds.x, bounds.y + bounds.height) ||
               roomSystem.isTilePosition("collision", bounds.x + bounds.width - 1, bounds.y + bounds.height)) {
                nextPos.y = (int) ((bounds.y + bounds.height) / TILE_SIZE) * TILE_SIZE - bounds.height - collision.offY;
                collidedY = true;
            }
            else if(roomSystem.isTilePosition("collision", bounds.x, bounds.y) ||
                    roomSystem.isTilePosition("collision", bounds.x + bounds.width - 1, bounds.y)) {
                nextPos.y = (int) (bounds.y / TILE_SIZE) * TILE_SIZE + TILE_SIZE - collision.offY;
                collidedY = true;
            }

            if(collidedY) {
              actor.moveBy(0, nextPos.y - prevPos.y);
              physics.vy = 0;
            }
        }

    }
}
