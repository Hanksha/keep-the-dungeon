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
import com.calderagames.ld37.actor.EnemyActor;
import com.calderagames.ld37.ai.SteeringEntity;
import com.calderagames.ld37.system.component.*;

import java.util.HashMap;

public class EntityFactory extends PassiveSystem {

    private GroupManager groupManager;

    private HashMap<String, Archetype> archetypes;
    private ComponentMapper<HealthComponent> healthMapper;
    private ComponentMapper<PositionComponent> posMapper;
    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<MoveComponent> moveMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;
    private ComponentMapper<EnemyComponent> enemyMapper;
    private ComponentMapper<AnimationComponent> animMapper;
    private ComponentMapper<AIComponent> AIMapper;

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
                .add(PositionComponent.class)
                .add(PhysicsComponent.class)
                .add(CollisionComponent.class)
                .add(MoveComponent.class)
                .add(ActorComponent.class)
                .add(HealthComponent.class)
                .add(AnimationComponent.class)
                .build(world);

        archetypes.put("basic-entity", archetype);

        archetype = new ArchetypeBuilder(archetypes.get("basic-entity"))
                .build(world);
        archetypes.put("player", archetype);

        archetype = new ArchetypeBuilder(archetypes.get("basic-entity"))
                .add(EnemyComponent.class)
                .add(AIComponent.class)
                .build(world);
        archetypes.put("enemy", archetype);

        archetype = new ArchetypeBuilder(archetypes.get("enemy"))
                .build(world);
        archetypes.put("enemy-javeliner", archetype);

        archetype = new ArchetypeBuilder()
                .add(PositionComponent.class)
                .add(PhysicsComponent.class)
                .add(CollisionComponent.class)
                .add(ActorComponent.class)
                .build(world);
        archetypes.put("arrow-red", archetype);
        archetypes.put("arrow-blue", archetype);
        archetypes.put("arrow-yellow", archetype);
        archetypes.put("javelin", archetype);
    }

    public int makeEntity(String name) {
        int id = world.create(archetypes.get(name));

        if(name.equals("player")) {
            makePlayer(id);
        }
        else if(name.contains("enemy")) {
            makeEnemy(id, name);
        }
        else if(name.startsWith("arrow") || name.equals("javelin")) {
            makeProjectile(id, name);
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

        AnimationComponent animComp = animMapper.get(id);
        animComp.anim = "player-idle";
        animComp.direction = AnimationComponent.Direction.DOWN;

        // set actors
        ActorComponent actorComp = actorMapper.get(id);

        Group parentActor = new Group();
        parentActor.setUserObject(id);
        stage.addActor(parentActor);
        actorComp.actor = parentActor;

        BaseActor shadowActor = new BaseActor(atlas.findRegion("player/shadow"));
        shadowActor.setPosition(0, 0, Align.center);
        shadowActor.setOrigin(Align.center);
        shadowActor.setName("shadow");
        parentActor.addActor(shadowActor);

        AnimationActor bodyActor = new AnimationActor(null);
        bodyActor.setPosition(0, -6, Align.center | Align.bottom);
        bodyActor.setOrigin(Align.center | Align.bottom);
        bodyActor.setName("body");
        parentActor.addActor(bodyActor);

        BaseActor crossbowActor = new BaseActor(atlas.findRegion("crossbow", 1));
        crossbowActor.setPosition(0, 13, Align.center | Align.bottom);
        crossbowActor.setOrigin(Align.center | Align.bottom);
        crossbowActor.setName("crossbow");
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
        collision.enabled = false;

        AnimationComponent animComp = animMapper.get(id);
        animComp.anim = name + "-walk";
        animComp.direction = AnimationComponent.Direction.DOWN;

        ActorComponent actorComp = actorMapper.get(id);
        Group parentActor = new Group();
        actorComp.actor = parentActor;
        stage.addActor(parentActor);

        EnemyActor bodyActor = new EnemyActor();
        bodyActor.setUserObject(id);
        bodyActor.setName(name);

        AIComponent aiComp = AIMapper.get(id);
        aiComp.entity = new SteeringEntity();
        aiComp.state = AISystem.STATE_INIT;

        if(name.contains("javeliner"))
            makeJaveliner(id, parentActor);
    }

    private void makeJaveliner(int id, Group parentActor) {
        AnimationActor bodyActor = new AnimationActor(null);
        bodyActor.setPosition(0, 0, Align.center | Align.bottom);
        bodyActor.setOrigin(Align.center | Align.bottom);
        bodyActor.setName("body");
        parentActor.addActor(bodyActor);
    }

    private void makeProjectile(int id, String name) {
        CollisionComponent collision = collisionMapper.get(id);
        collision.width = 6;
        collision.height = 6;
        collision.offY = -collision.height / 2;
        collision.enabled = false;

        ActorComponent actorComp = actorMapper.get(id);

        BaseActor actor = new BaseActor(atlas.findRegion(name));
        actorComp.alignX = actor.getWidth() / 2;
        actorComp.alignY = actor.getHeight() - collision.height;
        actor.setUserObject(id);
        actor.setOrigin(actorComp.alignX, actorComp.alignY);
        actor.setName(name);
        stage.addActor(actor);
        actorComp.actor = actor;


        PhysicsComponent physicsComp = physicsMapper.get(id);
        physicsComp.frictionX = 0;
        physicsComp.frictionY = 0;
        groupManager.addTo("projectiles", id);
    }

    public int makeEntity(String name, float x, float y) {
        int id = makeEntity(name);
        PositionComponent pos = posMapper.get(id);
        pos.x = x;
        pos.y = y;

        if(actorMapper.has(id)) {
            ActorComponent actorComp = actorMapper.get(id);
            actorComp.actor.setPosition(pos.x - actorComp.alignX, pos.y - actorComp.alignY);
        }
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
