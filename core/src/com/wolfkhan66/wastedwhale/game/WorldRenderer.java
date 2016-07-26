package com.wolfkhan66.wastedwhale.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Cai Lehwald on 24/07/2016.
 */
public class WorldRenderer implements Disposable {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private com.wolfkhan66.wastedwhale.game.WorldController worldController;

    public WorldRenderer(com.wolfkhan66.wastedwhale.game.WorldController worldController) {
        this.worldController = worldController;
        init();
    }
    private void init() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(com.wolfkhan66.wastedwhale.util.Constants.VIEWPORT_WIDTH, com.wolfkhan66.wastedwhale.util.Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
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
    }

    @Override public void dispose() {
        batch.dispose();
    }
}
