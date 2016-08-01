package com.wolfkhan66.wastedwhale.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.wolfkhan66.wastedwhale.util.Constants;

/**
 * Created by Cai Lehwald on 01/08/2016.
 */
public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    private Stage stage;
    private Skin skinWastedWhale;

    // Menu
    private Image imgBackground;
    private Image imgLogo;
    private Image imgInfo;
    private Image imgCoins;
    private Image imgBunny;
    private Button btnMenuPlay;
    private Button btnMenuOptions;

    // Options
    private Window winOptions;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;
    private CheckBox chkSound;
    private Slider sldSound;
    private CheckBox chkMusic;
    private Slider sldMusic;
    private SelectBox selCharSkin;
    private Image imgCharSkin;
    private CheckBox chkShowFpsCounter;

    // Debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private boolean debugEnabled = false;
    private float debugRebuildStage;

    public MenuScreen(Game game){
        super(game);
    }

    private void rebuildStage() {
        skinWastedWhale = new Skin(Gdx.files.internal(Constants.SKIN_WASTEDWHALE_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

        // Build all layers
        Table layerBackround = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerLogos = buildLogosLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindow();

        // Assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGTH);
        stack.add(layerBackround);
        stack.add(layerObjects);
        stack.add(layerLogos);
        stack.add(layerControls);
        stack.add(layerOptionsWindow);
    }

    private Table buildBackgroundLayer () {
        Table layer = new Table();
        // + Background
        imgBackground = new Image(skinWastedWhale, "background");
        layer.add(imgBackground);
        return layer;
    }

    private Table buildObjectsLayer () {
        Table layer = new Table();
        // + Coins
        imgCoins = new Image(skinWastedWhale, "coins");
        layer.addActor(imgCoins);
        imgCoins.setPosition(135, 80);
        // + Bunny
        imgBunny = new Image(skinWastedWhale, "bunny");
        layer.addActor(imgBunny);
        imgBunny.setPosition(355, 40);
        return layer;
    }

    private Table buildLogosLayer () {
        Table layer = new Table();
        layer.left().top();
        // + GameLogo
        imgLogo = new Image(skinWastedWhale, "logo");
        layer.add(imgLogo);
        layer.row().expandY();
        // + info Logos
        imgInfo = new Image(skinWastedWhale, "info");
        layer.add(imgInfo).bottom();
        if (debugEnabled) layer.debug();
        return layer;
    }

    private Table buildControlsLayer () {
        Table layer = new Table();
        return layer;
    }

    private Table buildOptionsWindow () {
        Table layer = new Table();
        layer.right().bottom();
        // + Play Button
        btnMenuPlay = new Button(skinWastedWhale, "play");
        layer.add(btnMenuPlay);
        btnMenuPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        layer.row();
        // + Options Button
        btnMenuOptions = new Button(skinWastedWhale, "options");
        layer.add(btnMenuOptions);
        btnMenuOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOptionsClicked();
            }
        });
        if (debugEnabled)layer.debug();
        return layer;
    }

    private void onPlayClicked() {
        game.setScreen(new GameScreen(game));
    }

    private void onOptionsClicked () {
    }

    @Override
    public void render (float deltaTime){
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (debugEnabled){
            debugRebuildStage -= deltaTime;
            if (debugRebuildStage <= 0){
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }
        stage.act(deltaTime);
        stage.draw();
        stage.setDebugAll(true);


        if (Gdx.input.isTouched()){
            game.setScreen(new GameScreen(game));
        }
    }

    @Override public void resize (int width, int height){
        stage.getViewport().update(width, height, true);
    }

    @Override public void show () {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGTH));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }

    @Override public void hide () {
        stage.dispose();
        skinWastedWhale.dispose();
    }
    @Override public void pause () {}
}
