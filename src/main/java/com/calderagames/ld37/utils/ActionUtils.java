package com.calderagames.ld37.utils;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

public abstract class ActionUtils {

    private ActionUtils() {}

    public static SequenceAction moveTo(float speed, Actor actor, MoveToAction... actions) {

        MoveToAction prevAction = null;
        float dist = 0;
        for(MoveToAction action: actions) {
            if(prevAction == null) {
                dist = Maths.dist(actor.getX(Align.center), actor.getY(Align.center), action.getX(), action.getY());
            }
            else {
                dist = Maths.dist(prevAction.getX(), prevAction.getY(), action.getX(), action.getY());
            }

            action.setDuration(dist / speed);
            prevAction = action;
        }

        return Actions.sequence(actions);
    }

}
