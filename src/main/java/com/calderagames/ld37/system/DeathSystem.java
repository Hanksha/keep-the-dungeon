package com.calderagames.ld37.system;


import com.calderagames.ld37.system.event.DeathEvent;
import com.calderagames.ld37.system.event.SystemEvent;
import com.calderagames.ld37.system.event.SystemEventListener;

public class DeathSystem extends PassiveSystem implements SystemEventListener {

    private PlayerSystem playerSystem;
    private EventSystem eventSystem;
    private EntityFactory entityFactory;

    @Override
    protected void initialize() {
        eventSystem.subscribe(DeathEvent.class, this);
    }

    @Override
    public void receive(SystemEvent event) {
        if(event.getClass() == DeathEvent.class) {
            DeathEvent deathEvent = (DeathEvent) event;
            if(deathEvent.id != playerSystem.getPlayerId())
                entityFactory.removeEntity(deathEvent.id);
        }
    }
}
