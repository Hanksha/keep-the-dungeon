package com.calderagames.ld37.ai;

import com.calderagames.ld37.system.AISystem;
import com.calderagames.ld37.system.component.AIComponent;

public class AIEntity {

    public AISystem system;
    public AIComponent ai;
    public int id;

    public AIEntity(AISystem system, AIComponent ai, int id) {
        this.system = system;
        this.ai = ai;
        this.id = id;
    }
}
