package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.calderagames.ld37.actor.AnimationActor;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.component.AnimationComponent;

import java.util.HashMap;

public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<AnimationComponent> animMapper;
    private ComponentMapper<ActorComponent> actorMapper;

    @Wire
    private AssetManager assets;

    private TextureAtlas atlas;

    private HashMap<String, Animation> animations;

    public AnimationSystem() {
        super(Aspect.all(AnimationComponent.class, ActorComponent.class));
    }

    @Override
    protected void initialize() {
        atlas = assets.get("textures/textures.atlas");

        animations = new HashMap<>();

        // load all animations
        Animation anim;

        // player idle
        anim = new Animation(0.4f, atlas.findRegions("player/player-idle-down"), Animation.PlayMode.LOOP);
        animations.put("player-idle-down", anim);

        anim = new Animation(0.4f, atlas.findRegions("player/player-idle-up"), Animation.PlayMode.LOOP);
        animations.put("player-idle-up", anim);

        anim = new Animation(0.4f, atlas.findRegions("player/player-idle-right"), Animation.PlayMode.LOOP);
        animations.put("player-idle-right", anim);

        anim = new Animation(0.4f, atlas.findRegions("player/player-idle-right"), Animation.PlayMode.LOOP);
        animations.put("player-idle-left", anim);

        // player walk
        anim = new Animation(0.08f, atlas.findRegions("player/player-walk-down"), Animation.PlayMode.LOOP);
        animations.put("player-walk-down", anim);

        anim = new Animation(0.08f, atlas.findRegions("player/player-walk-up"), Animation.PlayMode.LOOP);
        animations.put("player-walk-up", anim);

        anim = new Animation(0.08f, atlas.findRegions("player/player-walk-right"), Animation.PlayMode.LOOP);
        animations.put("player-walk-right", anim);

        anim = new Animation(0.08f, atlas.findRegions("player/player-walk-right"), Animation.PlayMode.LOOP);
        animations.put("player-walk-left", anim);
    }

    @Override
    protected void process(int entityId) {
        AnimationComponent animComp = animMapper.get(entityId);
        ActorComponent actorComp = actorMapper.get(entityId);

        AnimationActor actor = ((Group) actorComp.actor).findActor("body");

        if(animComp.direction != animComp.prevDirection || !animComp.anim.equals(animComp.prevAnim)) {
            animComp.prevDirection = animComp.direction;
            animComp.prevAnim = animComp.anim;

            Animation animation = animations.get(animComp.anim + "-" + animComp.direction);
            actor.setAnimation(animation);

            if(animComp.direction == AnimationComponent.Direction.LEFT)
                actor.setScaleX(-1);
            else
                actor.setScaleX(1);
        }
    }
}
