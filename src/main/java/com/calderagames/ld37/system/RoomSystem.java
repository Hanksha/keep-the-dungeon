package com.calderagames.ld37.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.calderagames.ld37.LD37Game;
import com.calderagames.ld37.system.component.AIComponent;
import com.calderagames.ld37.system.component.ActorComponent;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RoomSystem extends BaseSystem {

    private static final Logger logger = LogManager.getLogger();
    public static final int TILE_SIZE = 40;



    private EntityFactory entityFactory;
    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap room;

    private ComponentMapper<ActorComponent> actorMapper;
    private ComponentMapper<AIComponent> AIMapper;

    private Vector2 enemySpawnPos;

    @Wire(name="camera")
    private Camera camera;

    @Wire
    private SpriteBatch batch;

    @Wire
    private AssetManager assets;

    private Sprite enemyGateOpened;
    private Sprite exitGateClosed;

    @Override
    protected void initialize() {
        mapLoader = new TmxMapLoader();
        renderer = new OrthogonalTiledMapRenderer(null, batch);
        renderer.setView((OrthographicCamera) camera);

        enemySpawnPos = new Vector2(580, 60);

        TextureAtlas atlas = assets.get("textures/textures.atlas");
        enemyGateOpened = atlas.createSprite("grill-opened2");
        enemyGateOpened.setPosition(560, 0);

        exitGateClosed = atlas.createSprite("gate-closed");
        exitGateClosed.setCenterX(LD37Game.NATIVE_WIDTH / 2);
        exitGateClosed.setY(LD37Game.NATIVE_HEIGHT - exitGateClosed.getHeight());

        loadRoom("room-1");
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                popEnemy();
            }
        }, 5, 4);
    }

    private void loadRoom(String roomId) {
        if(room != null)
            room.dispose();

        room = mapLoader.load(roomId + ".tmx");
        renderer.setMap(room);
    }

    private void popEnemy() {
        int id = entityFactory.makeEnemy("enemy-javeliner", RandomUtils.nextInt(0, 3), enemySpawnPos.x, enemySpawnPos.y);
    }

    @Override
    protected void processSystem() {

    }

    public void draw() {
        renderer.render();
        batch.begin();
        enemyGateOpened.draw(batch);
        exitGateClosed.draw(batch);
        batch.end();
    }


    public MapObject getObject(String name) {
        return room.getLayers().get("objects").getObjects().get(name);
    }

    public TiledMapTileLayer getTileLayer(String name) {
        return (TiledMapTileLayer) room.getLayers().get(name);
    }

    public boolean isTileAt(String layer, int col, int row) {
        TiledMapTileLayer.Cell cell = getTileLayer(layer).getCell(col, row);

        return cell != null && cell.getTile() != null;
    }

    public boolean isTilePosition(String layer, float x, float y) {
        int row = (int) (y / TILE_SIZE);
        int col = (int) (x / TILE_SIZE);

        return isTileAt(layer, col, row);
    }

    private class RoomRules {

        public float speedOnPath;

        public int maxNumEnemies;

    }

}
