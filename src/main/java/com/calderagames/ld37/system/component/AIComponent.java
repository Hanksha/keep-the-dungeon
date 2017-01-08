package com.calderagames.ld37.system.component;

import com.artemis.Component;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;
import com.calderagames.ld37.ai.AIEntity;
import com.calderagames.ld37.ai.SteeringEntity;

public class AIComponent extends Component {

    public SteeringBehavior<Vector2> steeringBehavior;

    public SteeringEntity steeringEntity;

    public DefaultStateMachine<AIEntity, State<AIEntity>> fsm;

}
