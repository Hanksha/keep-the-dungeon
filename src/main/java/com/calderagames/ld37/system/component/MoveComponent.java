package com.calderagames.ld37.system.component;

import com.artemis.PooledComponent;

public class MoveComponent extends PooledComponent {

    public boolean left;
    public boolean right;
    public boolean up;
    public boolean down;

    public float xAcc;
    public float yAcc;

    @Override
    protected void reset() {
        left = false;
        right = false;
        up = false;
        down = false;

        xAcc = 0;
        yAcc = 0;
    }


}
