package com.calderagames.ld37.actor;

import com.badlogic.gdx.scenes.scene2d.Group;

public class EnemyActor extends Group {

    private boolean paused;

    public EnemyActor() {
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    @Override
    public void act(float delta) {
        if(paused)
            return;

        super.act(delta);
    }
}
