package com.calderagames.ld37.system.component;

import com.artemis.PooledComponent;

public class EnemyComponent extends PooledComponent {

    public static final int TYPE_YELLOW = 0;
    public static final int TYPE_BLUE = 1;
    public static final int TYPE_RED = 2;

    public int type;

    @Override
    protected void reset() {
        type = -1;
    }
}
