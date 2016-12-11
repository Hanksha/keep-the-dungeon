package com.calderagames.ld37.actor;

import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationActor extends BaseActor {

    protected float stateTime;
    protected Animation anim;

    public AnimationActor(Animation animation) {
        super(animation.getKeyFrame(0));
        anim = animation;
    }

    @Override
    public void act(float delta) {
        stateTime += delta;

        region = anim.getKeyFrame(stateTime);

        super.act(delta);
    }
}
