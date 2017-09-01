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
import com.calderagames.ld37.utils.Maths;

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

        // moblin javeliner idle
        anim = new Animation(0.4f, atlas.findRegions("enemies/moblin-idle-down"), Animation.PlayMode.LOOP);
        animations.put("enemy-javeliner-idle-down", anim);

        anim = new Animation(0.4f, atlas.findRegions("enemies/moblin-idle-up"), Animation.PlayMode.LOOP);
        animations.put("enemy-javeliner-idle-up", anim);

        anim = new Animation(0.4f, atlas.findRegions("enemies/moblin-idle-right"), Animation.PlayMode.LOOP);
        animations.put("enemy-javeliner-idle-right", anim);

        anim = new Animation(0.4f, atlas.findRegions("enemies/moblin-idle-right"), Animation.PlayMode.LOOP);
        animations.put("enemy-javeliner-idle-left", anim);

        // moblin javeliner walk
        anim = new Animation(0.1f, atlas.findRegions("enemies/moblin-walk-down"), Animation.PlayMode.LOOP);
        animations.put("enemy-javeliner-walk-down", anim);

        anim = new Animation(0.1f, atlas.findRegions("enemies/moblin-walk-up"), Animation.PlayMode.LOOP);
        animations.put("enemy-javeliner-walk-up", anim);

        anim = new Animation(0.1f, atlas.findRegions("enemies/moblin-walk-right"), Animation.PlayMode.LOOP);
        animations.put("enemy-javeliner-walk-right", anim);

        anim = new Animation(0.1f, atlas.findRegions("enemies/moblin-walk-right"), Animation.PlayMode.LOOP);
        animations.put("enemy-javeliner-walk-left", anim);

        // moblin javeliner throw
        anim = new Animation(0.125f, atlas.findRegions("enemies/moblin-throw-down"), Animation.PlayMode.LOOP);
        animations.put("enemy-javeliner-throw-down", anim);

        anim = new Animation(0.125f, atlas.findRegions("enemies/moblin-throw-up"), Animation.PlayMode.LOOP);
        animations.put("enemy-javeliner-throw-up", anim);

        anim = new Animation(0.125f, atlas.findRegions("enemies/moblin-throw-right"), Animation.PlayMode.LOOP);
        animations.put("enemy-javeliner-throw-right", anim);

        anim = new Animation(0.125f, atlas.findRegions("enemies/moblin-throw-right"), Animation.PlayMode.LOOP);
        animations.put("enemy-javeliner-throw-left", anim);

        // redmoblin swordman idle
        anim = new Animation(0.4f, atlas.findRegions("enemies/redmoblin-idle-down"), Animation.PlayMode.LOOP);
        animations.put("enemy-swordman-idle-down", anim);

        anim = new Animation(0.4f, atlas.findRegions("enemies/redmoblin-idle-up"), Animation.PlayMode.LOOP);
        animations.put("enemy-swordman-idle-up", anim);

        anim = new Animation(0.4f, atlas.findRegions("enemies/redmoblin-idle-right"), Animation.PlayMode.LOOP);
        animations.put("enemy-swordman-idle-right", anim);

        anim = new Animation(0.4f, atlas.findRegions("enemies/redmoblin-idle-right"), Animation.PlayMode.LOOP);
        animations.put("enemy-swordman-idle-left", anim);

        // redmoblin swordman walk
        anim = new Animation(0.1f, atlas.findRegions("enemies/redmoblin-walk-down"), Animation.PlayMode.LOOP);
        animations.put("enemy-swordman-walk-down", anim);

        anim = new Animation(0.1f, atlas.findRegions("enemies/redmoblin-walk-up"), Animation.PlayMode.LOOP);
        animations.put("enemy-swordman-walk-up", anim);

        anim = new Animation(0.1f, atlas.findRegions("enemies/redmoblin-walk-right"), Animation.PlayMode.LOOP);
        animations.put("enemy-swordman-walk-right", anim);

        anim = new Animation(0.1f, atlas.findRegions("enemies/redmoblin-walk-right"), Animation.PlayMode.LOOP);
        animations.put("enemy-swordman-walk-left", anim);

        // redmoblin swordman attack
        anim = new Animation(0.1f, atlas.findRegions("enemies/redmoblin-attack-down"), Animation.PlayMode.LOOP);
        animations.put("enemy-swordman-attack-down", anim);

        anim = new Animation(0.1f, atlas.findRegions("enemies/redmoblin-attack-up"), Animation.PlayMode.LOOP);
        animations.put("enemy-swordman-attack-up", anim);

        anim = new Animation(0.1f, atlas.findRegions("enemies/redmoblin-attack-right"), Animation.PlayMode.LOOP);
        animations.put("enemy-swordman-attack-right", anim);

        anim = new Animation(0.1f, atlas.findRegions("enemies/redmoblin-attack-right"), Animation.PlayMode.LOOP);
        animations.put("enemy-swordman-attack-left", anim);
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
                actor.setFlipX(true);
            else
                actor.setFlipX(false);
        }
    }

    public static AnimationComponent.Direction getDirectionFromAngle(float angle) {
        if((angle <= 45 && angle >= 0) || (angle >= 315 && angle <= 360))
            return AnimationComponent.Direction.RIGHT;
        else if(angle > 45 && angle < 135)
            return AnimationComponent.Direction.UP;
        else if(angle >= 135 && angle < 225)
            return AnimationComponent.Direction.LEFT;
        else if(angle >= 225 && angle < 315)
            return AnimationComponent.Direction.DOWN;

        return AnimationComponent.Direction.LEFT;
    }

    public static AnimationComponent.Direction getDirectionFromAngle(float x, float y, float targetX, float targetY) {

        float angle = Maths.getAngle(
                x,
                y,
                targetX, targetY);

        if(angle < 0)
            angle = angle + 360;

        return getDirectionFromAngle(angle);
    }
}
