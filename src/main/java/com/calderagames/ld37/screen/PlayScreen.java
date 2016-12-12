package com.calderagames.ld37.screen;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.calderagames.ld37.system.*;
import com.google.inject.Inject;

public class PlayScreen extends ScreenAdapter {

    private World world;

    @Inject
    private Stage stage;

    @Inject
    private SpriteBatch batch;

    @Inject
    private AssetManager assets;

    @Inject
    private InputMultiplexer inputMultiplexer;

    @Inject
    private Camera camera;

    private ShapeRenderer shapeRenderer;

    @Override
    public void show() {
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(new GroupManager())
                .with(new EntityFactory())
                .with(new EventSystem())
                .with(new PlayerSystem())
                .with(new EnemySystem())
                .with(new MoveSystem())
                .with(new PhysicsSystem())
                .with(new CollisionSystem())
                .with(new PostPhysicsSystem())
                .with(new AnimationSystem())
                .with(new HealthSystem())
                .with(new ProjectileSystem())
                .with(new RoomSystem())
                .with(new HUDSystem())
                .with(new DeathSystem())
                .build()
                .register(stage)
                .register(batch)
                .register("camera", camera)
                .register(assets.get("textures/textures.atlas", TextureAtlas.class))
                .register(assets);
        world = new World(config);

        inputMultiplexer.addProcessor(world.getSystem(PlayerSystem.class));
//        stage.setDebugAll(true);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float delta) {
        world.getSystem(RoomSystem.class).draw();
        stage.act(delta);
        world.setDelta(delta);
        world.process();
        stage.draw();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 0.5f);
        world.getSystem(ProjectileSystem.class).drawDebug(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        world.dispose();
        shapeRenderer.dispose();
    }
}
