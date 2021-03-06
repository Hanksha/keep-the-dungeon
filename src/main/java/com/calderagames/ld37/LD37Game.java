package com.calderagames.ld37;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.calderagames.ld37.audio.AudioManager;
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

    public static Cursor cursorRed;
    public static Cursor cursorYellow;
    public static Cursor cursorBlue;

    private static final Logger logger = LogManager.getLogger();

    private Injector injector;

    @Inject
    AudioManager audio;

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
        loadFonts();
        loadTextures();
        loadAudio();

        assets.finishLoading();

        cursorRed = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursor-red.png")), 32, 32);
        cursorBlue = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursor-blue.png")), 32, 32);
        cursorYellow = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursor-yellow.png")), 32, 32);
        Gdx.graphics.setCursor(cursorRed);
    }

    private void loadFonts() {
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
    }

    private void loadTextures() {
        // load texture atlas
        assets.load("textures/textures.atlas", TextureAtlas.class);
    }

    private void loadAudio() {
        assets.load("audio/effects/break.ogg", Sound.class);
        assets.load("audio/effects/death.ogg", Sound.class);
        assets.load("audio/effects/door.ogg", Sound.class);
        assets.load("audio/effects/heal.ogg", Sound.class);
        assets.load("audio/effects/hit.ogg", Sound.class);
        assets.load("audio/effects/resist.ogg", Sound.class);
        assets.load("audio/effects/select.ogg", Sound.class);
        assets.load("audio/effects/selectdone.ogg", Sound.class);
        assets.load("audio/effects/shoot.ogg", Sound.class);
        assets.load("audio/effects/slash.ogg", Sound.class);
    }

    @Override
    public void setScreen(Screen screen) {
        injector.injectMembers(screen);
        super.setScreen(screen);
    }

    @Override
    public void render() {
        assets.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        audio.update(Gdx.graphics.getDeltaTime());

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
