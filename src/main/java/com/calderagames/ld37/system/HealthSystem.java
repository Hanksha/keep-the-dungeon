package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.calderagames.ld37.system.component.HealthComponent;
import com.calderagames.ld37.system.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HealthSystem extends IteratingSystem implements SystemEventListener {

    private static final Logger logger = LogManager.getLogger();

    private EventSystem eventSystem;
    private ComponentMapper<HealthComponent> healthMapper;

    public HealthSystem() {
        super(Aspect.all(HealthComponent.class));
    }

    @Override
    protected void initialize() {
        eventSystem.subscribe(HitEvent.class, this);
    }

    @Override
    protected void process(int entityId) {
        HealthComponent healthComp = healthMapper.get(entityId);

        if(healthComp.immunityCooldown < healthComp.immunitySpan)
            healthComp.immunityCooldown += world.delta;
    }

    @Override
    public void receive(SystemEvent event) {
        if(event.getClass() == HitEvent.class) {
            HitEvent hit = (HitEvent) event;
            applyHit(hit.id, hit.damage);
        }
    }

    private void applyHit(int id, int damage) {
        if(!healthMapper.has(id))
            return;

        HealthComponent healthComp = healthMapper.get(id);

        if(healthComp.immunityCooldown < healthComp.immunitySpan)
            return;

        healthComp.health -= damage;
        healthComp.immunityCooldown = 0;

        if(healthComp.health <= 0) {
            healthComp.health = 0;
            eventSystem.send(new DeathEvent(id));
        }
        else {
            eventSystem.post(new HealthEvent(id));
        }
    }
}
