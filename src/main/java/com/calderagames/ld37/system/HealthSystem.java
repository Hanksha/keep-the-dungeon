package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.calderagames.ld37.actor.ColorFlickerAction;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.component.HealthComponent;
import com.calderagames.ld37.system.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HealthSystem extends IteratingSystem implements SystemEventListener {

    private static final Logger logger = LogManager.getLogger();

    private EventSystem eventSystem;
    private ComponentMapper<HealthComponent> healthMapper;
    private ComponentMapper<ActorComponent> actorMapper;

    private final Color flickerColor = new Color(1f, 1f, 1f, 0.4f);

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

        setFlicker(id, healthComp.immunitySpan);

        healthComp.health -= damage;
        healthComp.immunityCooldown = 0;

        if(healthComp.health <= 0) {
            healthComp.health = 0;
            eventSystem.send(new DeathEvent(id));
            eventSystem.post(new HealthEvent(id));
        }
        else {
            eventSystem.post(new HealthEvent(id));
        }
    }

    private void setFlicker(int id, float duration) {
        if(actorMapper.has(id)) {
            actorMapper.get(id).actor
                    .addAction(new ColorFlickerAction(Color.WHITE, flickerColor, duration, 0.1f));
        }
    }
}
