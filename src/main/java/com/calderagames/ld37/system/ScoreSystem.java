package com.calderagames.ld37.system;

import com.calderagames.ld37.system.event.*;

public class ScoreSystem extends PassiveSystem implements SystemEventListener {

    private EventSystem eventSystem;
    private GroupManager groupManager;

    private int multiplier = 1;
    private int hitCounter;
    private int score;

    @Override
    protected void initialize() {
        eventSystem.subscribe(HitEvent.class, this);
        eventSystem.subscribe(HitMissEvent.class, this);
        eventSystem.subscribe(HitResistEvent.class, this);
        eventSystem.subscribe(DeathEvent.class, this);
    }

    @Override
    protected void processSystem() {

    }

    private void setMultiplier() {
        if(hitCounter >= 16)
            multiplier = 4;
        else if(hitCounter >= 8)
            multiplier = 3;
        else if(hitCounter >= 4)
            multiplier = 2;
    }

    private void reset() {
        hitCounter = 0;
        multiplier = 1;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void receive(SystemEvent event) {
        if(event instanceof HitEvent) {
            hitCounter++;
            setMultiplier();
            System.out.println(multiplier);
        }
        else if(event instanceof HitMissEvent) {
            reset();
        }
        else if(event instanceof DeathEvent) {
            DeathEvent deathEvent = (DeathEvent) event;
            if(groupManager.getGroups(deathEvent.id).contains("enemies")) {
                score += multiplier;
            }
        }
    }
}
