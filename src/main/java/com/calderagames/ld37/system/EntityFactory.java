package com.calderagames.ld37.system;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.calderagames.ld37.actor.BaseActor;
import com.calderagames.ld37.system.component.*;

import java.util.HashMap;

public class EntityFactory extends PassiveSystem {

    private GroupManager groupManager;

    private HashMap<String, Archetype> archetypes;
    private ComponentMapper<HealthComponent> healthMapper;
    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<MoveComponent> moveMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;

    @Wire
    private Stage stage;
    private Group playerGroup;
    private Group enemiesGroup;
    private Group projectileGroup;

    @Wire
    private AssetManager assets;
    private TextureAtlas atlas;

    public EntityFactory() {
        super();
        archetypes = new HashMap<>();
    }

    @Override
    protected void initialize() {
        setUpArchetypes();
        atlas = assets.get("textures/textures.atlas");
        enemiesGroup = new Group();
        enemiesGroup.setPosition(0, 0, Align.bottomLeft);

        stage.addActor(enemiesGroup);

        playerGroup = new Group();
        playerGroup.setPosition(0, 0, Align.bottomLeft);
        stage.addActor(playerGroup);

        projectileGroup = new Group();
        projectileGroup.setPosition(0, 0, Align.bottomLeft);
        stage.addActor(projectileGroup);
    }

    private void setUpArchetypes() {
        Archetype archetype = new ArchetypeBuilder()
                .add(ActorComponent.class)
                .add(PhysicsComponent.class)
                .add(CollisionComponent.class)
                .add(MoveComponent.class)
                .add(HealthComponent.class)
                .build(world);

        archetypes.put("basic-entity", archetype);

        archetype = new ArchetypeBuilder(archetypes.get("basic-entity"))
                .build(world);
        archetypes.put("player", archetype);

        archetype = new ArchetypeBuilder(archetypes.get("basic-entity"))
                .build(world);
        archetypes.put("moblin-orange", archetype);

        archetype = new ArchetypeBuilder()
                .add(ActorComponent.class)
                .add(PhysicsComponent.class)
                .build(world);
        archetypes.put("arrow", archetype);
    }

    public int makeEntity(String name) {
        int id = world.create(archetypes.get(name));

        switch(name) {

            case "player": {
                groupManager.addTo("player", id);

                // set movement stuff
                MoveComponent moveComp = moveMapper.get(id);
                moveComp.xAcc = moveComp.yAcc = 1000;

                // set health
                HealthComponent healthComp = healthMapper.get(id);
                healthComp.health = healthComp.maxHealth = 3;
                healthComp.immunitySpan = 1000;

                CollisionComponent collision = collisionMapper.get(id);
                collision.width = 12;
                collision.height = 11;
                collision.offX = 0;
                collision.offY = 5;

                // set actors
                ActorComponent actorComp = actorMapper.get(id);
                Group parentActor = new Group();
                parentActor.setName("player");
                parentActor.setTouchable(Touchable.childrenOnly);
                playerGroup.addActor(parentActor);
                actorComp.actor = parentActor;

                Actor collisionActor = new Actor();
                collisionActor.setName("collisionBox");
                collisionActor.setTouchable(Touchable.enabled);
                collisionActor.setSize(collision.width, collision.height);
                collisionActor.setPosition(collision.offX, collision.offY, Align.center | Align.bottom);
                collisionActor.setOrigin(Align.center | Align.bottom);
                parentActor.addActor(collisionActor);

                BaseActor bodyActor = new BaseActor(atlas.findRegion("player/player-idle-down"));
                bodyActor.setPosition(0, 0, Align.center | Align.bottom);
                bodyActor.setOrigin(Align.center | Align.bottom);
                bodyActor.setName("player-body");
                bodyActor.setTouchable(Touchable.disabled);
                bodyActor.setZIndex(4);
                parentActor.addActor(bodyActor);

                BaseActor crossbowActor = new BaseActor(atlas.findRegion("crossbow", 1));
                crossbowActor.setPosition(0, 13, Align.center | Align.bottom);
                crossbowActor.setOrigin(Align.center | Align.bottom);
                crossbowActor.setName("crossbow");
                crossbowActor.setTouchable(Touchable.disabled);
                parentActor.addActor(crossbowActor);

                break;
            }

            case "moblin-orange": {
                groupManager.addTo("enemies", id);

                MoveComponent moveComp = moveMapper.get(id);
                moveComp.xAcc = moveComp.yAcc = 1200;

                HealthComponent healthComp = healthMapper.get(id);
                healthComp.health = healthComp.maxHealth = 2;
                healthComp.immunitySpan = 500;

                CollisionComponent collision = collisionMapper.get(id);
                collision.width = 18;
                collision.height = 15;
                collision.offX = 0;
                collision.offY = 4;

                ActorComponent actorComp = actorMapper.get(id);
                Group parentActor = new Group();
                parentActor.setName("moblin-orange");
                parentActor.setTouchable(Touchable.childrenOnly);
                enemiesGroup.addActor(parentActor);
                actorComp.actor = parentActor;

                Actor collisionActor = new Actor();
                collisionActor.setName("collisionBox");
                collisionActor.setTouchable(Touchable.enabled);
                collisionActor.setSize(collision.width, collision.height);
                collisionActor.setPosition(collision.offX, collision.offY, Align.center | Align.bottom);
                collisionActor.setOrigin(Align.center | Align.bottom);
                parentActor.addActor(collisionActor);

                BaseActor bodyActor = new BaseActor(atlas.findRegion("enemies/moblin-throw-down", 1));
                bodyActor.setPosition(0, 0, Align.center | Align.bottom);
                bodyActor.setTouchable(Touchable.disabled);
                bodyActor.setOrigin(Align.center | Align.bottom);
                bodyActor.setName("moblin-orange");
                parentActor.addActor(bodyActor);

                break;
            }

            case "arrow": {
                ActorComponent actorComp = actorMapper.get(id);
                BaseActor actor = new BaseActor(atlas.findRegion("arrow-red"));
                actor.setOrigin(Align.center);
                actor.setName("arrow");
                actorComp.actor = actor;
                projectileGroup.addActor(actor);

                PhysicsComponent physicsComp = physicsMapper.get(id);
                physicsComp.frictionX = 0;
                physicsComp.frictionY = 0;
                groupManager.addTo("projectiles", id);
            }
        }

        return id;
    }

    public int makeEntity(String name, float x, float y) {
        int id = makeEntity(name);
        ActorComponent actor = actorMapper.get(id);
        actor.actor.setPosition(x, y, Align.center);
        return id;
    }

    public int fireProjectile(String name, float x, float y, float angle, float speed) {
        int id = makeEntity(name, x, y);
        Actor actor = actorMapper.get(id).actor;
        actor.setRotation(angle);
        PhysicsComponent physicsComp = physicsMapper.get(id);
        physicsComp.vx = speed * MathUtils.cosDeg(angle + 90);
        physicsComp.vy = speed * MathUtils.sinDeg(angle + 90);
        return id;
    }

    public void removeEntity(int entityId) {
        if(actorMapper.has(entityId)) {
            actorMapper.get(entityId).actor.remove();
        }
        groupManager.removeFromAllGroups(entityId);
        world.delete(entityId);
    }

    public Group getEnemiesGroup() {
        return enemiesGroup;
    }

}
