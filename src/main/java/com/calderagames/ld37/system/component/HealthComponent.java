package com.calderagames.ld37.system.component;

import com.artemis.PooledComponent;

public class HealthComponent extends PooledComponent {

    public int health;
    public int maxHealth;
    public long immunitySpan;

    @Override
    protected void reset() {
        health = 0;
        maxHealth = 0;
        immunitySpan = 0;
    }
}
