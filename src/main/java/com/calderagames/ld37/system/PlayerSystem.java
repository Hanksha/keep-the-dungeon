package com.calderagames.ld37.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.calderagames.ld37.actor.BaseActor;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.component.MoveComponent;
import com.calderagames.ld37.system.component.PhysicsComponent;
import com.calderagames.ld37.utils.InputUtils;
import com.calderagames.ld37.utils.Maths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.badlogic.gdx.Input.Keys.*;
import static com.calderagames.ld37.LD37Game.NATIVE_HEIGHT;
import static com.calderagames.ld37.LD37Game.NATIVE_WIDTH;

public class PlayerSystem extends BaseSystem implements InputProcessor {

    private static final Logger logger = LogManager.getLogger();

    @Wire(name = "camera")
    private Camera camera;

    private EntityFactory entityFactory;
    private GroupManager groupManager;
    private RoomSystem roomSystem;
    private ComponentMapper<MoveComponent> moveMapper;
    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private MoveComponent move;
    private int playerId;

    private float arrowSpeed = 600;
    private float shootRateTime = 0.6f;
    private float shootCooldown = 0;

    @Override
    protected void initialize() {
        playerId = entityFactory.makeEntity("player", NATIVE_WIDTH / 2, NATIVE_HEIGHT / 2);
        move = moveMapper.get(playerId);
    }

    @Override
    protected void processSystem() {
        processAim();
        shootCooldown += world.delta;
    }

    private void processAim() {
        Vector2 mouse = InputUtils.getMouse(camera);
        Group playerActor = (Group) actorMapper.get(playerId).actor;
        BaseActor crossbowActor = playerActor.findActor("crossbow");

        float angle = Maths.getAngle(
                playerActor.getX(Align.center),
                playerActor.getY(Align.center),
                mouse.x, mouse.y);
        crossbowActor.setRotation(angle - 90);
    }

    private void fireArrow() {
        if(shootCooldown < shootRateTime)
            return;

        shootCooldown = 0;

        Group playerActor = (Group) actorMapper.get(playerId).actor;
        BaseActor crossbowActor = playerActor.findActor("crossbow");
        Vector2 pos = crossbowActor.localToStageCoordinates(new Vector2(crossbowActor.getOriginX(), 5));
        int id = entityFactory.fireProjectile("arrow-blue", pos.x, pos.y, crossbowActor.getRotation(), arrowSpeed);
        groupManager.addTo("player-arrows", id);
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case LEFT:
                move.left = true;
                move.right = false;
                break;
            case RIGHT:
                move.left = false;
                move.right = true;
                break;
            case UP:
                move.up = true;
                move.down = false;
                break;
            case DOWN:
                move.up = false;
                move.down = true;
                break;
            default: break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case LEFT:
                move.left = false;
                break;
            case RIGHT:
                move.right = false;
                break;
            case UP:
                move.up = false;
                break;
            case DOWN:
                move.down = false;
                break;
            default: break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT)
            fireArrow();
        else if(button == Input.Buttons.RIGHT) {
            Vector2 mouse = InputUtils.getMouse(camera);
            logger.info(roomSystem.isTilePosition("collision", mouse.x, mouse.y));
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
