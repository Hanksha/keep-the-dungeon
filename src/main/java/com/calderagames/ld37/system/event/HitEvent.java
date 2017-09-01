package com.calderagames.ld37.system.event;

public class HitEvent extends EntityEvent {

    public int sourceId;
    public int damage;

    public HitEvent(int targetId, int sourceId, int damage) {
        super(targetId);
        this.sourceId = sourceId;
        this.damage = damage;
    }

}
