package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.calderagames.ld37.system.component.CollisionComponent;
import com.calderagames.ld37.system.component.PhysicsComponent;
import com.calderagames.ld37.system.component.PositionComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.calderagames.ld37.system.RoomSystem.TILE_SIZE;

public class CollisionSystem extends IteratingSystem {

    private static final Logger logger = LogManager.getLogger();

    public boolean debug;

    private RoomSystem roomSystem;
    private ComponentMapper<PositionComponent> posMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;

    public CollisionSystem() {
        super(Aspect.all(PhysicsComponent.class, PositionComponent.class, CollisionComponent.class));
    }

    @Override
    protected void process(int entityId) {
        PositionComponent pos = posMapper.get(entityId);
        PhysicsComponent physics = physicsMapper.get(entityId);
        CollisionComponent collision = collisionMapper.get(entityId);

        if(!collision.enabled || (physics.vx == 0 && physics.vy == 0))
            return;

        Vector2 nextPos = new Vector2(pos.x, pos.y);
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
                pos.x = nextPos.x;
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
              pos.y = nextPos.y;
              physics.vy = 0;
            }
        }

    }

    public Rectangle getBounds(int entityId) {
        if(!collisionMapper.has(entityId) || !posMapper.has(entityId))
            return null;

        PositionComponent pos = posMapper.get(entityId);
        CollisionComponent collision = collisionMapper.get(entityId);
        return new Rectangle(pos.x + collision.offX - collision.width / 2, pos.y + collision.offY,
                             collision.width, collision.height);
    }

    public boolean isOverlap(int entityIdA, int entityIdB) {
        Rectangle rectA = getBounds(entityIdA);
        Rectangle rectB = getBounds(entityIdB);

        if(rectA == null || rectB == null)
            return false;

        return rectA.overlaps(rectB);
    }

    public void renderDebug(ShapeRenderer renderer) {
        IntBag entities = getEntityIds();

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(1f, 0f, 0f, 0.5f);
        for(int i = 0, id; i < entities.size(); i++) {
            id = entities.get(i);
            CollisionComponent collision = collisionMapper.get(id);
            PositionComponent pos = posMapper.get(id);
            renderer.rect(pos.x + collision.offX - collision.width / 2,
                          pos.y + collision.offY,
                          collision.width, collision.height);
        }
        renderer.end();
    }
}
