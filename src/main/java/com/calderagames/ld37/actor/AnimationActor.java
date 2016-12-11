package com.calderagames.ld37.actor;

import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationActor extends BaseActor {

    protected float stateTime;
    protected Animation anim;

    public AnimationActor(Animation anim) {
        super(anim == null? null: anim.getKeyFrame(0));
        this.anim = anim;
    }

    public void setAnimation(Animation anim) {
        this.anim = anim;
        stateTime = 0;
    }

    @Override
    public void act(float delta) {
        if(anim == null)
            return;


        stateTime += delta;

        setRegion(anim.getKeyFrame(stateTime));

        super.act(delta);
    }
}
