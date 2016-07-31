package com.wolfkhan66.wastedwhale.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.wolfkhan66.wastedwhale.game.objects.Feather;
import com.wolfkhan66.wastedwhale.game.objects.GoldCoin;
import com.wolfkhan66.wastedwhale.game.objects.Level;
import com.wolfkhan66.wastedwhale.game.objects.Rock;
import com.wolfkhan66.wastedwhale.util.CameraHelper;
import com.wolfkhan66.wastedwhale.util.Constants;


/**
 * Created by Cai Lehwald on 24/07/2016.
 */
public class WorldController extends InputAdapter {
    private static final String TAG = WorldController.class.getName();
    public CameraHelper cameraHelper;
    public Level level;
    public int lives;
    public int score;

    // Rectangles for collision detection
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();

    public WorldController() {
        init();
    }

    public void init() {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START;
        initLevel();
    }

    public void initLevel(){
        score = 0;
        level = new Level(Constants.LEVEL_01);
    }

    public void update(float deltaTime) {
        handleDebugInput(deltaTime);
        level.update(deltaTime);
        cameraHelper.update(deltaTime);
    }

    @Override
    public boolean keyUp (int keycode){
        // Reset game world
        if (keycode == Input.Keys.R){
            init();
            Gdx.app.debug(TAG, "Game World Reset");
        }
        return false;
    }


    private Pixmap createProceduralPixmap(int width, int height){
        Pixmap pixmap = new Pixmap(width,height, Pixmap.Format.RGBA8888);

        // Fill square with red color at 50% opacity
        pixmap.setColor(1, 0, 0, 0.5f);
        pixmap.fill();

        // Draw a yellow-colored X shape on square
        pixmap.setColor(1, 1, 0, 1);
        pixmap.drawLine(0, 0, width, height);
        pixmap.drawLine(width, 0, 0, height);

        // Draw a cyan-colored border around the square
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);

        return pixmap;
    }

    private void handleDebugInput(float deltaTime){
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

        // Camera Controls (movement)
        float camMoveSpeed = 5 * deltaTime;
        float camMoveSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(-camMoveSpeed,0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(camMoveSpeed,0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0, camMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0, -camMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)){
            cameraHelper.setPosition(0, 0);
        }

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);
    }

    private void moveCamera(float x, float y){
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    private void onCollisionBunnyHeadWithRock(Rock rock){

    }

    private void onCollisionBunnyWithGoldCoin(GoldCoin goldCoin){

    }

    private void onCollisionBunnyWithFeather(Feather feather){

    }

    private void testCollisions(){
        r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);

        // Test collision : BunnyHead <--> Rocks
        for (Rock rock : level.rocks){
            r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyHeadWithRock(rock);
            // IMPORTANT: must do all collisions for valid edge testing on rocks
        }

        // Test collision: BunnyHead <--> Gold Coins
        for (GoldCoin goldCoin : level.goldCoins){
            if (goldCoin.collected) continue;
            r2.set(goldCoin.position.x, goldCoin.position.y, goldCoin.bounds.width, goldCoin.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyWithGoldCoin(goldCoin);
            break;
        }

        // Test collision: BunnyHead <--> Feathers
        for (Feather feather : level.feathers){
            if (feather.collected) continue;
            r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyWithFeather(feather);
            break;
        }
    }
}
