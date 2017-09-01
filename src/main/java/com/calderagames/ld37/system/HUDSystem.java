package com.calderagames.ld37.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.calderagames.ld37.LD37Game;
import com.calderagames.ld37.system.component.HealthComponent;
import com.calderagames.ld37.system.event.ArrowTypeEvent;
import com.calderagames.ld37.system.event.HealthEvent;
import com.calderagames.ld37.system.event.SystemEvent;
import com.calderagames.ld37.system.event.SystemEventListener;

import static com.calderagames.ld37.LD37Game.NATIVE_HEIGHT;

public class HUDSystem extends BaseSystem implements SystemEventListener {

    @Wire
    private AssetManager assets;

    private EventSystem eventSystem;
    private PlayerSystem playerSystem;
    private ScoreSystem scoreSystem;

    private ComponentMapper<HealthComponent> healthMapper;

    @Wire
    private Stage stage;
    private Group hudGroup;
    private HorizontalGroup healthGroup;
    private Image quiverImage;
    private NinePatchDrawable[] reloadBars;
    private TextureRegionDrawable[] reloadBarBgs;
    private Image reloadBarImg;
    private Image reloadBarBgImg;
    private Label scoreMultiplierLabel;
    private Label scoreLabel;

    private TextureRegion heartFullImg;
    private TextureRegion heartEmptyImg;
    private TextureRegionDrawable[] quiverRegions;

    @Override
    protected void initialize() {
        eventSystem.subscribe(HealthEvent.class, this);
        eventSystem.subscribe(ArrowTypeEvent.class, this);

        hudGroup = new Group();
        stage.addActor(hudGroup);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = assets.get("font-16");
        labelStyle.fontColor = Color.WHITE;

        Table scoreTable = new Table();
        scoreTable.setPosition(20, NATIVE_HEIGHT - 40);
        scoreTable.align(Align.topLeft);
        hudGroup.addActor(scoreTable);

        scoreLabel = new Label("score: 0", labelStyle);
        scoreTable.add(scoreLabel).align(Align.left);
        scoreTable.row().space(4);

        scoreMultiplierLabel = new Label("X 1", labelStyle);
        scoreTable.add(scoreMultiplierLabel).align(Align.left);


        healthGroup = new HorizontalGroup();
        hudGroup.addActor(healthGroup);
        healthGroup.setPosition(20, NATIVE_HEIGHT - 20);

        TextureAtlas atlas = assets.get("textures/textures.atlas");
        heartFullImg = atlas.findRegion("heart-full");
        heartEmptyImg = atlas.findRegion("heart-empty");
        setHealth();

        quiverRegions = new TextureRegionDrawable[3];
        quiverRegions[0] = new TextureRegionDrawable(atlas.findRegion("quiver-yellow"));
        quiverRegions[1] = new TextureRegionDrawable(atlas.findRegion("quiver-blue"));
        quiverRegions[2] = new TextureRegionDrawable(atlas.findRegion("quiver-red"));

        quiverImage = new Image(quiverRegions[0]);
        quiverImage.setPosition(30, 30, Align.center);
        hudGroup.addActor(quiverImage);

        reloadBars = new NinePatchDrawable[3];
        reloadBars[0] = new NinePatchDrawable(atlas.createPatch("reload-yellow-full"));
        reloadBars[1] = new NinePatchDrawable(atlas.createPatch("reload-blue-full"));
        reloadBars[2] = new NinePatchDrawable(atlas.createPatch("reload-red-full"));

        reloadBarBgs = new TextureRegionDrawable[3];
        reloadBarBgs[0] = new TextureRegionDrawable(atlas.findRegion("reload-yellow-empty"));
        reloadBarBgs[1] = new TextureRegionDrawable(atlas.findRegion("reload-blue-empty"));
        reloadBarBgs[2] = new TextureRegionDrawable(atlas.findRegion("reload-red-empty"));

        Group playerActor = playerSystem.getActor();

        reloadBarBgImg = new Image(reloadBarBgs[0]);
        reloadBarBgImg.setTouchable(Touchable.disabled);
        reloadBarBgImg.setPosition(0, 45, Align.center | Align.top);
        playerActor.addActor(reloadBarBgImg);

        reloadBarImg = new Image(reloadBars[0]);
        reloadBarImg.setTouchable(Touchable.disabled);
        reloadBarImg.setPosition(-7, 45, Align.center | Align.top);
        playerActor.addActor(reloadBarImg);
    }

    @Override
    protected void processSystem() {
        reloadBarImg.setVisible(playerSystem.getShootCooldown() < 1f);
        reloadBarImg.setSize(20 * playerSystem.getShootCooldown(), 4);
        reloadBarBgImg.setVisible(playerSystem.getShootCooldown() < 1f);
        scoreMultiplierLabel.setText("X " + scoreSystem.getMultiplier());
        scoreLabel.setText("score: " + scoreSystem.getScore());
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
        else if(event.getClass() == ArrowTypeEvent.class) {
            final int arrowType = ((ArrowTypeEvent) event).newArrowType;
            quiverImage.setDrawable(quiverRegions[arrowType]);
            reloadBarImg.setDrawable(reloadBars[arrowType]);
            reloadBarBgImg.setDrawable(reloadBarBgs[arrowType]);

            switch (arrowType) {
                case PlayerSystem.ARROW_RED:
                    Gdx.graphics.setCursor(LD37Game.cursorRed);
                    break;
                case PlayerSystem.ARROW_BLUE:
                    Gdx.graphics.setCursor(LD37Game.cursorBlue);
                    break;
                case PlayerSystem.ARROW_YELLOW:
                    Gdx.graphics.setCursor(LD37Game.cursorYellow);
                    break;
            }
        }

    }
}
