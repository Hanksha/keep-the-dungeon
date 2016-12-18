package com.calderagames.ld37.system.component;

import com.artemis.PooledComponent;

public class PositionComponent extends PooledComponent {

    public float x;
    public float y;
    public float oldX;
    public float oldY;

    @Override
    protected void reset() {
        x = y = oldX = oldY = 0;
    }
}
