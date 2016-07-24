package com.wolfkhan66.wastedwhale;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by caile_000 on 24/07/2016.
 */
public class WorldRenderer implements Disposable {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private WorldController worldController;

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }
    private void init() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
    }

    public void render() {
        renderTestObjects();
    }

    public void renderTestObjects(){
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Sprite sprite : worldController.testSprites){
            sprite.draw(batch);
        }
        batch.end();
    }

    public void resize(int width, int height) {
        camera.viewportWidth = (Constants.VIEWPORT_WIDTH / height) * width;
        camera.update();
    }

    @Override public void dispose() {
        batch.dispose();
    }
}
