package com.calderagames.ld37.system;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.calderagames.ld37.ai.AIEntity;
import com.calderagames.ld37.system.component.AnimationComponent;
import com.calderagames.ld37.system.component.PositionComponent;
import com.calderagames.ld37.system.event.HitEvent;

public enum SwordmanState implements State<AIEntity> {
    WALK() {
        @Override
        public void enter(AIEntity entity) {
            AnimationComponent animComp = entity.system.animMapper.get(entity.id);
            animComp.anim = "enemy-swordman-walk";
            entity.ai.steeringEntity.setMaxLinearAcceleration(entity.ai.defaultLinearAcceleration);
        }

        @Override
        public void update(AIEntity entity) {
            if(entity.ai.steeringEntity.getPosition().dst(entity.system.playerPos) < 32)
                entity.ai.fsm.changeState(ATTACK);
        }

        @Override
        public void exit(AIEntity entity) {
        }
    },

    ATTACK() {
        @Override
        public void enter(AIEntity entity) {
            MessageManager.getInstance().addListener(entity.ai.fsm, AISystem.FIRE);
            MessageManager.getInstance().dispatchMessage(0.4f, entity.ai.fsm, entity.ai.fsm, AISystem.FIRE, entity.system.playerPos);
            AnimationComponent animComp = entity.system.animMapper.get(entity.id);
            animComp.anim = "enemy-swordman-attack";
            entity.ai.steeringEntity.setMaxLinearAcceleration(0);
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
            if(entity.ai.steeringEntity.getPosition().dst(entity.system.playerPos) < 32) {
                Vector2 pos = entity.ai.steeringEntity.getPosition();
                Vector2 playerPos = entity.system.playerPos;
                AnimationComponent.Direction currentFacing = entity.system.animMapper.get(entity.id).direction;
                AnimationComponent.Direction facingToPlayer = AnimationSystem.getDirectionFromAngle(pos.x, pos.y, playerPos.x, playerPos.y);

                if(currentFacing == facingToPlayer) {
                    entity.system.eventSystem.send(new HitEvent(entity.system.playerSystem.getPlayerId(), entity.id, 1));
                }

            }

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
