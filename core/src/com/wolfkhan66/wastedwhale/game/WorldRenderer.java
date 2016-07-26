package com.wolfkhan66.wastedwhale.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.wolfkhan66.wastedwhale.util.Constants;

/**
 * Created by Cai Lehwald on 24/07/2016.
 */
public class WorldRenderer implements Disposable {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private com.wolfkhan66.wastedwhale.game.WorldController worldController;
    private OrthographicCamera cameraGUI;

    public WorldRenderer(com.wolfkhan66.wastedwhale.game.WorldController worldController) {
        this.worldController = worldController;
        init();
    }
    private void init() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(com.wolfkhan66.wastedwhale.util.Constants.VIEWPORT_WIDTH, com.wolfkhan66.wastedwhale.util.Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGTH);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
    }

    public void render() {
        renderWorld(batch);
    }

    private void renderWorld(SpriteBatch batch){
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.level.render(batch);
        batch.end();
    }

    public void resize(int width, int height) {
        camera.viewportWidth = (com.wolfkhan66.wastedwhale.util.Constants.VIEWPORT_WIDTH / height) * width;
        camera.update();
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGTH;
        cameraGUI.viewportWidth = Constants.VIEWPORT_GUI_WIDTH / (float)height * (float)width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }

    @Override public void dispose() {
        batch.dispose();
    }
}
