package com.calderagames.ld37.system;

import com.artemis.annotations.Wire;
import com.calderagames.ld37.audio.AudioManager;
import com.calderagames.ld37.system.event.HitEvent;
import com.calderagames.ld37.system.event.SystemEvent;
import com.calderagames.ld37.system.event.SystemEventListener;
import org.apache.commons.lang3.RandomUtils;

public class AudioSystem extends PassiveSystem implements SystemEventListener {

    @Wire(name = "audio")
    AudioManager audio;

    @Wire
    EventSystem eventSystem;

    @Override
    protected void initialize() {
        eventSystem.subscribe(HitEvent.class, this);
    }

    @Override
    public void receive(SystemEvent event) {
        if(event instanceof HitEvent) {
            audio.playSound("hit.ogg", 0.5f, RandomUtils.nextFloat(0.5f, 1.5f), 0f);
        }
    }
}
