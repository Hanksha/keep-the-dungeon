package com.calderagames.ld37.system;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.calderagames.ld37.actor.AnimationActor;
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
    private ComponentMapper<EnemyComponent> enemyMapper;
    private ComponentMapper<AnimationComponent> animMapper;

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
                .add(AnimationComponent.class)
                .build(world);
        archetypes.put("player", archetype);

        archetype = new ArchetypeBuilder(archetypes.get("basic-entity"))
                .add(EnemyComponent.class)
                .build(world);
        archetypes.put("enemy", archetype);
        
        archetype = new ArchetypeBuilder(archetypes.get("enemy"))
                .build(world);
        archetypes.put("enemy-javeliner", archetype);

        archetype = new ArchetypeBuilder()
                .add(ActorComponent.class)
                .add(PhysicsComponent.class)
                .build(world);
        archetypes.put("arrow-red", archetype);
        archetypes.put("arrow-blue", archetype);
        archetypes.put("arrow-yellow", archetype);
    }

    public int makeEntity(String name) {
        int id = world.create(archetypes.get(name));

        if(name.equals("player")) {
            makePlayer(id);
        }
        else if(name.contains("enemy")) {
            makeEnemy(id, name);
        }
        else if(name.startsWith("arrow")) {
            makeArrow(id, name);
        }

        return id;
    }

    private void makePlayer(int id) {
        groupManager.addTo("player", id);

        // set movement stuff
        MoveComponent moveComp = moveMapper.get(id);
        moveComp.xAcc = moveComp.yAcc = 1000;

        // set health
        HealthComponent healthComp = healthMapper.get(id);
        healthComp.health = healthComp.maxHealth = 3;
        healthComp.immunitySpan = healthComp.immunityCooldown = 1f;

        CollisionComponent collision = collisionMapper.get(id);
        collision.width = 12;
        collision.height = 11;
        collision.offX = 0;
        collision.offY = 5;

        AnimationComponent animComp = animMapper.get(id);
        animComp.anim = "player-idle";
        animComp.direction = AnimationComponent.Direction.DOWN;

        // set actors
        ActorComponent actorComp = actorMapper.get(id);
        Group parentActor = new Group();
        parentActor.setName("player");
        parentActor.setUserObject(id);
        parentActor.setTouchable(Touchable.childrenOnly);
        playerGroup.addActor(parentActor);
        actorComp.actor = parentActor;

        Actor collisionActor = new Actor();
        collisionActor.setName("collisionBox");
        collisionActor.setUserObject(id);
        collisionActor.setTouchable(Touchable.enabled);
        collisionActor.setSize(collision.width, collision.height);
        collisionActor.setPosition(collision.offX, collision.offY, Align.center | Align.bottom);
        collisionActor.setOrigin(Align.center | Align.bottom);
        parentActor.addActor(collisionActor);

        AnimationActor bodyActor = new AnimationActor(null);
        bodyActor.setSize(30, 37);
        bodyActor.setPosition(0, 0, Align.center | Align.bottom);
        bodyActor.setOrigin(Align.center | Align.bottom);
        bodyActor.setName("body");
        bodyActor.setTouchable(Touchable.disabled);
        bodyActor.setZIndex(4);
        parentActor.addActor(bodyActor);

        BaseActor crossbowActor = new BaseActor(atlas.findRegion("crossbow", 1));
        crossbowActor.setPosition(0, 13, Align.center | Align.bottom);
        crossbowActor.setOrigin(Align.center | Align.bottom);
        crossbowActor.setName("crossbow");
        crossbowActor.setTouchable(Touchable.disabled);
        parentActor.addActor(crossbowActor);
    }

    private void makeEnemy(int id, String name) {
        groupManager.addTo("enemies", id);

        MoveComponent moveComp = moveMapper.get(id);
        moveComp.xAcc = moveComp.yAcc = 1200;

        HealthComponent healthComp = healthMapper.get(id);
        healthComp.health = healthComp.maxHealth = 2;
        healthComp.immunitySpan = healthComp.immunityCooldown = 0.5f;

        CollisionComponent collision = collisionMapper.get(id);
        collision.width = 18;
        collision.height = 18;
        collision.offX = 0;
        collision.offY = 4;

        ActorComponent actorComp = actorMapper.get(id);
        Group parentActor = new Group();
        parentActor.setUserObject(id);
        parentActor.setName(name);
        parentActor.setTouchable(Touchable.childrenOnly);
        enemiesGroup.addActor(parentActor);
        actorComp.actor = parentActor;

        Actor collisionActor = new Actor();
        collisionActor.setName("collisionBox");
        collisionActor.setUserObject(id);
        collisionActor.setTouchable(Touchable.enabled);
        collisionActor.setSize(collision.width, collision.height);
        collisionActor.setPosition(collision.offX, collision.offY, Align.center | Align.bottom);
        collisionActor.setOrigin(Align.center | Align.bottom);
        parentActor.addActor(collisionActor);

        if(name.contains("javeliner"))
            makeJaveliner(id, parentActor);
    }

    private void makeJaveliner(int id, Group parentActor) {
        BaseActor bodyActor = new BaseActor(atlas.findRegion("enemies/moblin-idle-down", 1));
        bodyActor.setPosition(0, 0, Align.center | Align.bottom);
        bodyActor.setTouchable(Touchable.disabled);
        bodyActor.setOrigin(Align.center | Align.bottom);
        bodyActor.setName("body");
        parentActor.addActor(bodyActor);
    }

    private void makeArrow(int id, String name) {
        ActorComponent actorComp = actorMapper.get(id);
        BaseActor actor = new BaseActor(atlas.findRegion(name));
        actor.setUserObject(id);
        actor.setOrigin(Align.center);
        actor.setName(name);
        actorComp.actor = actor;
        projectileGroup.addActor(actor);

        PhysicsComponent physicsComp = physicsMapper.get(id);
        physicsComp.frictionX = 0;
        physicsComp.frictionY = 0;
        groupManager.addTo("projectiles", id);
    }

    public int makeEntity(String name, float x, float y) {
        int id = makeEntity(name);
        ActorComponent actor = actorMapper.get(id);
        actor.actor.setPosition(x, y, Align.center);
        return id;
    }

    public int makeEnemy(String name, int type, float x, float y) {
        int id = makeEntity(name, x, y);
        EnemyComponent enemyComp = enemyMapper.get(id);
        enemyComp.type = type;

        Group parentActor = (Group) actorMapper.get(id).actor;

        Animation anim;

        if(type == EnemyComponent.TYPE_YELLOW)
            anim = new Animation(0.3f, atlas.findRegions("marker-yellow"));
        else if(type == EnemyComponent.TYPE_BLUE)
            anim = new Animation(0.3f, atlas.findRegions("marker-blue"));
        else
            anim = new Animation(0.3f, atlas.findRegions("marker-red"));

        anim.setPlayMode(Animation.PlayMode.LOOP);

        AnimationActor typeActor = new AnimationActor(anim);
        typeActor.setTouchable(Touchable.disabled);
        typeActor.setPosition(0, 50, Align.center);
        typeActor.setOrigin(Align.center);
        parentActor.addActorAt(0, typeActor);

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
