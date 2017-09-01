package com.calderagames.ld37.system.event;

public class ShootEvent extends SystemEvent {

    public int sourceId;

    public ShootEvent(int sourceId) {
        this.sourceId = sourceId;
    }
}
