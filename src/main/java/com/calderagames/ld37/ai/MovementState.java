package com.calderagames.ld37.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.calderagames.ld37.system.AnimationSystem;
import com.calderagames.ld37.system.component.AIComponent;
import com.calderagames.ld37.system.component.AnimationComponent;
import com.calderagames.ld37.system.component.PhysicsComponent;
import com.calderagames.ld37.system.component.PositionComponent;

public enum MovementState implements State<AIEntity> {
    ON_PATH() {
        @Override
        public void enter(AIEntity entity) {
            AIComponent aiComp = entity.system.AIMapper.get(entity.id);
            Array<Vector2> wayPoints = new Array<>();
            wayPoints.add(new Vector2(580, 60));
            wayPoints.add(new Vector2(580, 300));
            wayPoints.add(new Vector2(60, 300));
            wayPoints.add(new Vector2(60, 60));
            wayPoints.add(new Vector2(360, 60));
            LinePath<Vector2> path = new LinePath<>(wayPoints, true);

            FollowPath<Vector2, LinePath.LinePathParam> followPath = new FollowPath<>(aiComp.steeringEntity, path, 15);
            aiComp.steeringBehavior = followPath;
        }

        @Override
        public void update(AIEntity entity) {
            AIComponent aiComp = entity.system.AIMapper.get(entity.id);
            AnimationComponent animComp = entity.system.animMapper.get(entity.id);
            PhysicsComponent physics = entity.system.physicsMapper.get(entity.id);

            if(physics.vx > 0.5f)
                animComp.direction = AnimationComponent.Direction.RIGHT;
            else if(physics.vx < -0.5f)
                animComp.direction = AnimationComponent.Direction.LEFT;
            else if(physics.vy > 0.5f)
                animComp.direction = AnimationComponent.Direction.UP;
            else if(physics.vy < -0.5f)
                animComp.direction = AnimationComponent.Direction.DOWN;

            PositionComponent pos = entity.system.posMapper.get(entity.id);
            FollowPath followPath = (FollowPath) aiComp.steeringBehavior;

            if(((Vector2) followPath.getPath().getEndPoint()).dst(pos.x, pos.y) <= 5) {
                entity.system.changeGlobalState(entity.id, WANDER);
            }

        }
    },

    WANDER() {
        @Override
        public void enter(AIEntity entity) {
            Wander<Vector2> wander = new Wander<>(entity.ai.steeringEntity);
            wander.setFaceEnabled(false);
            wander.setWanderRadius(10);
            wander.setWanderRate(MathUtils.PI2 * 5f);

            entity.ai.steeringBehavior = wander;

            entity.system.collisionMapper.get(entity.id).enabled = true;
        }

        @Override
        public void update(AIEntity entity) {
            AnimationComponent animComp = entity.system.animMapper.get(entity.id);
            PositionComponent pos = entity.system.posMapper.get(entity.id);
            Vector2 target = entity.system.playerPos;
            animComp.direction = AnimationSystem.getDirectionFromAngle(pos.x, pos.y, target.x, target.y);
        }
    }
    ;

    @Override
    public void enter(AIEntity entity) {

    }

    @Override
    public void update(AIEntity entity) {

    }

    @Override
    public void exit(AIEntity entity) {

    }

    @Override
    public boolean onMessage(AIEntity entity, Telegram telegram) {
        return false;
    }
}
