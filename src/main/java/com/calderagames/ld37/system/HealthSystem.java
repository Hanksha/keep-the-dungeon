package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.calderagames.ld37.system.component.HealthComponent;
import com.calderagames.ld37.system.event.SystemEvent;
import com.calderagames.ld37.system.event.SystemEventListener;

public class HealthSystem extends IteratingSystem implements SystemEventListener {

    private ComponentMapper<HealthComponent> hm;

    public HealthSystem() {
        super(Aspect.all(HealthComponent.class));
    }

    @Override
    protected void process(int entityId) {

    }

    @Override
    public void receive(SystemEvent event) {

    }
}
