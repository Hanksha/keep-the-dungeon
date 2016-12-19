package com.calderagames.ld37.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class ColorFlickerAction extends TemporalAction {

    private Color originalColor;
    private Color flickerColor;
    private float stateTime;
    private float interval;

    public ColorFlickerAction(Color originalColor, Color flickerColor, float duration, float interval) {
        super(duration);
        this.originalColor = new Color(originalColor);
        this.flickerColor = new Color(flickerColor);
        this.interval = interval;
    }

    @Override
    protected void update(float percent) {
    }

    @Override
    public boolean act(float delta) {
        boolean complete = super.act(delta);

        stateTime += delta;

        if(!complete) {
            int index = (int) (stateTime / interval);

            if(index % 2 == 0)
                getActor().setColor(originalColor);
            else
                getActor().setColor(flickerColor);
        }
        else
            getActor().setColor(originalColor);

        return complete;
    }
}
