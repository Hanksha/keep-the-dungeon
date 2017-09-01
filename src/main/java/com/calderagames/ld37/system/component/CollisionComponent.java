package com.calderagames.ld37.system.component;

import com.artemis.PooledComponent;

public class CollisionComponent extends PooledComponent {

    public int width;
    public int height;
    public int offX;
    public int offY;
    public boolean enabled = true;

    @Override
    protected void reset() {
        width = 0;
        height = 0;
        offX = 0;
        offY = 0;
        enabled = true;
    }
}
