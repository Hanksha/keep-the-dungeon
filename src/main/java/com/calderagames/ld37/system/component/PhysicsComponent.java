package com.calderagames.ld37.system.component;

import com.artemis.PooledComponent;

public class PhysicsComponent extends PooledComponent {

    public static final float DEFAULT_FRICTION_X = 8;
    public static final float DEFAULT_FRICTION_Y = 8;

    public float vx;
    public float vy;
    public float prevX;
    public float prevY;
    public float frictionX = DEFAULT_FRICTION_X;
    public float frictionY = DEFAULT_FRICTION_Y;
    public float bounce;

    @Override
    protected void reset() {
        vx = 0;
        vy = 0;
        bounce = 0;
        prevX = 0;
        prevY = 0;
        frictionX = DEFAULT_FRICTION_X;
        frictionY = DEFAULT_FRICTION_Y;
    }
}
