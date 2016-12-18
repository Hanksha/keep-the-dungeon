package com.calderagames.ld37.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.calderagames.ld37.actor.BaseActor;
import com.calderagames.ld37.screen.PlayScreen;
import com.calderagames.ld37.system.component.ActorComponent;
import com.calderagames.ld37.system.component.AnimationComponent;
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

    @Wire
    private TextureAtlas atlas;

    @Wire
    PlayScreen playScreen;

    private EntityFactory entityFactory;
    private GroupManager groupManager;
    private RoomSystem roomSystem;
    private ComponentMapper<MoveComponent> moveMapper;
    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private ComponentMapper<AnimationComponent> animMapper;
    private MoveComponent move;
    private int playerId;

    private final int numArrowType = 3;
    public final int ARROW_YELLOW = 0;
    public final int ARROW_BLUE = 1;
    public final int ARROW_RED = 2;
    private final String[] arrowTypes = new String[] {"arrow-yellow", "arrow-blue", "arrow-red"};

    private float arrowSpeed = 600;
    private int currentArrowType;
    private float shootRateTime = 0.6f;
    private float shootCooldown = 0;
    private float aimAngle;

    @Override
    protected void initialize() {
        playerId = entityFactory.makeEntity("player", NATIVE_WIDTH / 2, NATIVE_HEIGHT / 2);
        move = moveMapper.get(playerId);
    }

    @Override
    protected void processSystem() {
        processAim();
        processAnim();
        processCooldown();
    }

    private void processAim() {
        Vector2 mouse = InputUtils.getMouse(camera);
        Group playerActor = (Group) actorMapper.get(playerId).actor;
        BaseActor crossbowActor = playerActor.findActor("crossbow");

        Vector2 pos = crossbowActor.localToStageCoordinates(
                new Vector2(crossbowActor.getOriginX(), crossbowActor.getOriginY()));

        aimAngle = Maths.getAngle(
                pos.x,
                pos.y,
                mouse.x, mouse.y);
        crossbowActor.setRotation(aimAngle - 90);

        if(aimAngle < 0)
            aimAngle = aimAngle + 360;

    }

    private void processAnim() {
        AnimationComponent animComp = animMapper.get(playerId);
        if((aimAngle <= 45 && aimAngle >= 0) || (aimAngle >= 315 && aimAngle <= 360))
            animComp.direction = AnimationComponent.Direction.RIGHT;
        else if(aimAngle > 45 && aimAngle < 135)
            animComp.direction = AnimationComponent.Direction.UP;
        else if(aimAngle >= 135 && aimAngle < 225)
            animComp.direction = AnimationComponent.Direction.LEFT;
        else if(aimAngle >= 225 && aimAngle < 315)
            animComp.direction = AnimationComponent.Direction.DOWN;

        PhysicsComponent physicsComp = physicsMapper.get(playerId);

        if(Math.abs(physicsComp.vx) > 2 || Math.abs(physicsComp.vy) > 2)
            animComp.anim = "player-walk";
        else
            animComp.anim = "player-idle";

        BaseActor crossbowActor = ((Group) actorMapper.get(playerId).actor).findActor("crossbow");

        if(animComp.direction == AnimationComponent.Direction.DOWN)
            crossbowActor.setZIndex(10);
        else
            crossbowActor.setZIndex(0);
    }

    private void processCooldown() {
        if(shootCooldown < shootRateTime) {
            shootCooldown += world.delta;

            if(shootCooldown >= shootRateTime) {
                Group playerActor = (Group) actorMapper.get(playerId).actor;
                BaseActor crossbowActor = playerActor.findActor("crossbow");
                crossbowActor.setRegion(atlas.findRegion("crossbow", 1));
            }
        }

    }

    private void fireArrow() {
        if(shootCooldown < shootRateTime)
            return;

        shootCooldown = 0;

        Group playerActor = (Group) actorMapper.get(playerId).actor;
        BaseActor crossbowActor = playerActor.findActor("crossbow");
        crossbowActor.setRegion(atlas.findRegion("crossbow", 2));
        Vector2 pos = crossbowActor.localToStageCoordinates(new Vector2(crossbowActor.getOriginX(), 5));
        int id = entityFactory.fireProjectile(arrowTypes[currentArrowType], pos.x, pos.y, crossbowActor.getRotation(), arrowSpeed);
        groupManager.addTo("player-arrows", id);
    }

    private void setArrowType() {
        currentArrowType++;

        if(currentArrowType >= numArrowType)
            currentArrowType = 0;

        shootCooldown = shootRateTime / 2;
    }

    public int getCurrentArrowType() {
        return currentArrowType;
    }

    public int getPlayerId() {
        return playerId;
    }

    public float getShootCooldown() {
        return shootCooldown / shootRateTime;
    }

    public Group getActor() {
        return (Group) actorMapper.get(playerId).actor;
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
            case F4:
                playScreen.setDebug(!playScreen.getDebug());
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
            setArrowType();
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
