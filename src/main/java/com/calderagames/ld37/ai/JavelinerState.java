package com.calderagames.ld37.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.calderagames.ld37.system.AISystem;
import com.calderagames.ld37.system.AnimationSystem;
import com.calderagames.ld37.system.EnemySystem;
import com.calderagames.ld37.system.component.AnimationComponent;
import com.calderagames.ld37.system.component.PositionComponent;

public enum JavelinerState implements State<AIEntity> {
    WALK() {
        @Override
        public void enter(AIEntity entity) {
            MessageManager.getInstance().addListener(entity.ai.fsm, AISystem.ATTACK);
            AnimationComponent animComp = entity.system.animMapper.get(entity.id);
            animComp.anim = "enemy-javeliner-walk";
            entity.ai.steeringEntity.setMaxLinearAcceleration(entity.ai.defaultLinearAcceleration);
        }

        @Override
        public void exit(AIEntity entity) {
            MessageManager.getInstance().removeListener(entity.ai.fsm, AISystem.ATTACK);
        }

        @Override
        public boolean onMessage(AIEntity entity, Telegram telegram) {
            entity.ai.fsm.changeState(ATTACK);
            return true;
        }
    },

    ATTACK() {
        @Override
        public void enter(AIEntity entity) {
            MessageManager.getInstance().addListener(entity.ai.fsm, AISystem.FIRE);
            MessageManager.getInstance().dispatchMessage(0.4f, entity.ai.fsm, entity.ai.fsm, AISystem.FIRE, entity.system.playerPos);
            AnimationComponent animComp = entity.system.animMapper.get(entity.id);
            animComp.anim = "enemy-javeliner-throw";
            entity.ai.steeringEntity.setMaxLinearAcceleration(entity.ai.defaultLinearAcceleration / 3);
        }

        @Override
        public void update(AIEntity entity) {
            AnimationComponent animComp = entity.system.animMapper.get(entity.id);
            PositionComponent pos = entity.system.posMapper.get(entity.id);
            Vector2 target = entity.system.playerPos;
            animComp.direction = AnimationSystem.getDirectionFromAngle(pos.x, pos.y, target.x, target.y);
        }

        @Override
        public void exit(AIEntity entity) {
            MessageManager.getInstance().removeListener(entity.ai.fsm, AISystem.FIRE);
        }

        @Override
        public boolean onMessage(AIEntity entity, Telegram telegram) {
            PositionComponent pos = entity.system.posMapper.get(entity.id);
            Vector2 target = (Vector2) telegram.extraInfo;
            entity.system.fireProjectile(pos.x, pos.y, target.x, target.y, 150);
            entity.ai.fsm.changeState(WALK);
            return true;
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
