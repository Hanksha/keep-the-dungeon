package com.calderagames.ld37.system.event;

public class ArrowTypeEvent extends SystemEvent {

    public int oldArrowType;
    public int newArrowType;

    public ArrowTypeEvent(int oldArrowType, int newArrowType) {
        this.oldArrowType = oldArrowType;
        this.newArrowType = newArrowType;
    }

}
