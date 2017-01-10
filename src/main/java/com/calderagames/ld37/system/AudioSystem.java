package com.calderagames.ld37.system;

import com.artemis.annotations.Wire;
import com.calderagames.ld37.audio.AudioManager;
import com.calderagames.ld37.system.event.*;
import org.apache.commons.lang3.RandomUtils;

public class AudioSystem extends PassiveSystem implements SystemEventListener {

    @Wire(name = "audio")
    private AudioManager audio;

    @Wire
    private EventSystem eventSystem;

    @Override
    protected void initialize() {
        eventSystem.subscribe(HitEvent.class, this);
        eventSystem.subscribe(HitResistEvent.class, this);
        eventSystem.subscribe(ShootEvent.class, this);
        eventSystem.subscribe(DeathEvent.class, this);
    }

    @Override
    public void receive(SystemEvent event) {
        if(event instanceof HitEvent) {
            audio.playSound("hit.ogg", 0.5f, RandomUtils.nextFloat(0.5f, 1.5f), 0f);
        }
        else if(event instanceof HitResistEvent) {
            audio.playSound("resist.ogg", 0.8f, RandomUtils.nextFloat(0.5f, 1.5f), 0f);
        }
        else if(event instanceof ShootEvent) {
            audio.playSound("shoot.ogg", 0.5f, RandomUtils.nextFloat(0.8f, 1.2f), 0f);
        }
        else if(event instanceof DeathEvent) {
            audio.playSound("death.ogg", 0.3f, RandomUtils.nextFloat(0.5f, 1f), 0f);
        }
    }
}
