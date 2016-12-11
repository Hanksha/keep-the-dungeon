package com.calderagames.ld37;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.calderagames.ld37.screen.PlayScreen;
import com.calderagames.ld37.utils.FontUtils;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LD37Game extends Game {

    public static final int NATIVE_WIDTH = 640;
    public static final int NATIVE_HEIGHT = 360;

    private static final Logger logger = LogManager.getLogger();

    private Injector injector;

    @Inject
    private Viewport viewport;

    @Inject
    private SpriteBatch batch;

    @Inject
    private Stage stage;

    @Inject
    private InputMultiplexer inputMultiplexer;

    @Inject
    private AssetManager assets;

    public LD37Game() {
        injector = Guice.createInjector(new LD37Module());
    }

    @Override
    public void create() {
        injector.injectMembers(this);

        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        loadAssets();

        setScreen(new PlayScreen());
    }

    private void loadAssets() {
        // load fonts
        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
        assets.setLoader(BitmapFont.class, new FreetypeFontLoader(new InternalFileHandleResolver()));
        assets.load("mithril.ttf", FreeTypeFontGenerator.class);
        assets.load("font-16", BitmapFont.class, new FontUtils.FontParamBuilder("mithril.ttf")
                .setSize(16)
                .setSmoothing(false)
                .setBorderWidth(1)
                .setTextureFilter(Texture.TextureFilter.Nearest)
                .build());

        // load texture atlas
        assets.load("textures/textures.atlas", TextureAtlas.class);

        assets.finishLoading();
    }

    @Override
    public void setScreen(Screen screen) {
        injector.injectMembers(screen);
        super.setScreen(screen);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        if(screen != null)
            screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        logger.info("disposing resources");

        if(screen != null)
            screen.hide();

        stage.dispose();
        assets.dispose();
        batch.dispose();
    }

    @Override
    public void pause() {
        if(screen != null)
            screen.pause();
    }

    @Override
    public void resume() {
        if(screen != null)
            screen.resume();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        if(screen != null)
            screen.resize(width, height);
    }
}
