package com.calderagames.ld37.system.component;

import com.artemis.Component;

public class AnimationComponent extends Component {

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

}
