package com.calderagames.ld37.system.event;

public class HitMissEvent extends SystemEvent {

    public int sourceId;

    public HitMissEvent(int sourceId) {
        this.sourceId = sourceId;
    }
}
