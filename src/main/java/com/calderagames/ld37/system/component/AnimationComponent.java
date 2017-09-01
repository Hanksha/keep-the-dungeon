package com.calderagames.ld37.system.component;

import com.artemis.PooledComponent;

public class AnimationComponent extends PooledComponent {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public Direction direction;
    public Direction prevDirection;
    public String anim;
    public String prevAnim;

    @Override
    protected void reset() {
        anim = null;
        prevAnim = null;
        direction = null;
        prevDirection = null;
    }

}
