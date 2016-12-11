package com.calderagames.ld37.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.calderagames.ld37.system.component.HealthComponent;
import com.calderagames.ld37.system.event.HealthEvent;
import com.calderagames.ld37.system.event.SystemEvent;
import com.calderagames.ld37.system.event.SystemEventListener;

import static com.calderagames.ld37.LD37Game.NATIVE_HEIGHT;

public class HUDSystem extends BaseSystem implements SystemEventListener {

    @Wire
    private AssetManager assets;

    private EventSystem eventSystem;
    private PlayerSystem playerSystem;
    private ComponentMapper<HealthComponent> healthMapper;

    @Wire
    private Stage stage;
    private Group hudGroup;
    private HorizontalGroup healthGroup;

    private TextureRegion heartFullImg;
    private TextureRegion heartEmptyImg;


    @Override
    protected void initialize() {
        hudGroup = new Group();
        stage.addActor(hudGroup);

        healthGroup = new HorizontalGroup();
        hudGroup.addActor(healthGroup);
        healthGroup.setPosition(20, NATIVE_HEIGHT - 20);

        TextureAtlas atlas = assets.get("textures/textures.atlas");
        heartFullImg = atlas.findRegion("heart-full");
        heartEmptyImg = atlas.findRegion("heart-empty");

        setHealth();

        eventSystem.subscribe(HealthEvent.class, this);
    }

    @Override
    protected void processSystem() {

    }

    private void setHealth() {
        healthGroup.clear();

        HealthComponent healthComp = healthMapper.get(playerSystem.getPlayerId());

        int currentHealth = healthComp.health;
        int maxHealth = healthComp.maxHealth;

        for(int i = 0; i < maxHealth; i++) {
            if(i < currentHealth)
                healthGroup.addActor(new Image(heartFullImg));
            else
                healthGroup.addActor(new Image(heartEmptyImg));
        }
    }

    @Override
    public void receive(SystemEvent event) {
        if(event.getClass() == HealthEvent.class) {
            if(((HealthEvent) event).id == playerSystem.getPlayerId())
                setHealth();
        }

    }
}
