package com.calderagames.ld37;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.calderagames.ld37.accessor.ColorAccessor;
import com.calderagames.ld37.accessor.MusicAccessor;
import com.calderagames.ld37.accessor.SpriteAccessor;
import com.calderagames.ld37.accessor.Vector2Accessor;
import com.calderagames.ld37.audio.AudioManager;
import com.calderagames.ld37.audio.AudioManagerImpl;
import com.calderagames.ld37.audio.MusicInstance;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

public class LD37Module extends AbstractModule {
    @Override
    protected void configure() {
        bind(InputMultiplexer.class).in(Scopes.SINGLETON);
        bind(AudioManager.class).to(AudioManagerImpl.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    SpriteBatch provideSpriteBatch() {
        return new SpriteBatch();
    }

    @Provides
    @Singleton
    ShapeRenderer provideShapeRendner() {
        return new ShapeRenderer();
    }

    @Provides
    @Singleton
    AssetManager provideAssetManager() {
        return new AssetManager();
    }

    @Provides
    @Singleton
    Stage provideStage(Viewport viewport, SpriteBatch batch) {
        Stage stage = new Stage(viewport, batch);
        return stage;
    }

    @Provides
    @Singleton
    Viewport provideViewport(Camera camera) {
        FitViewport viewport = new FitViewport(LD37Game.NATIVE_WIDTH, LD37Game.NATIVE_HEIGHT, camera);
        return viewport;
    }

    @Provides
    @Singleton
    Camera provideCamera() {
        OrthographicCamera camera = new OrthographicCamera(LD37Game.NATIVE_WIDTH, LD37Game.NATIVE_HEIGHT);
        camera.setToOrtho(false);
        camera.update();
        return camera;
    }

    @Provides
    @Singleton
    TweenManager provideTweenManagre() {
        Tween.setCombinedAttributesLimit(4);
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Color.class, new ColorAccessor());
        Tween.registerAccessor(MusicInstance.class, new MusicAccessor());
        Tween.registerAccessor(Vector2.class, new Vector2Accessor());
        return new TweenManager();
    }
}
