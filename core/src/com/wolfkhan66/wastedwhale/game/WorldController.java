package com.wolfkhan66.wastedwhale.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.wolfkhan66.wastedwhale.game.objects.BunnyHead;
import com.wolfkhan66.wastedwhale.game.objects.Feather;
import com.wolfkhan66.wastedwhale.game.objects.GoldCoin;
import com.wolfkhan66.wastedwhale.game.objects.Level;
import com.wolfkhan66.wastedwhale.game.objects.Rock;
import com.wolfkhan66.wastedwhale.game.screens.MenuScreen;
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
    private float timeLeftGameOverDelay;
    private Game game;
    // Rectangles for collision detection
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();

    public WorldController(Game game) {
        this.game = game;
        init();
    }

    public void init() {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START;
        timeLeftGameOverDelay = 0;
        initLevel();
    }

    public void initLevel(){
        score = 0;
        level = new Level(Constants.LEVEL_01);
        cameraHelper.setTarget(level.bunnyHead);
    }

    public void update(float deltaTime) {
        handleDebugInput(deltaTime);
        if ( isGameOver()){
            timeLeftGameOverDelay -= deltaTime;
            if(timeLeftGameOverDelay < 0) backToMenu();
        }
        else {
            handleInputGame(deltaTime);
        }
        level.update(deltaTime);
        testCollisions();
        cameraHelper.update(deltaTime);
        if (!isGameOver() && isPlayerInWater()){
            lives--;
            if (isGameOver()){
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            }
            else {
                initLevel();
            }
        }
    }

    private void backToMenu (){
        // Switch to menu screen
        game.setScreen(new MenuScreen(game));
    }

    @Override
    public boolean keyUp (int keycode){
        // Reset game world
        if (keycode == Input.Keys.R){
            init();
            Gdx.app.debug(TAG, "Game World Reset");
        }
        // Toggle camera follow
        else if (keycode == Input.Keys.ENTER){
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null: level.bunnyHead);
            Gdx.app.debug(TAG, "Camera Follow Enabled:" + cameraHelper.hasTarget());
        }
        // Back to menu
        else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK){
            backToMenu();
        }
        return false;
    }

    private void  handleInputGame(float deltaTime){
        if (cameraHelper.hasTarget(level.bunnyHead)){
            // Player movement
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
            }
            else {
                // Execute auto-forward movement on non desktop platforms
                if (Gdx.app.getType() != Application.ApplicationType.Desktop){
                    level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
                }
            }

            // Jump
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                level.bunnyHead.setJumping(true);
            }
            else {
                level.bunnyHead.setJumping(false);
            }
        }
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

        if (!cameraHelper.hasTarget(level.bunnyHead)) {
            // Camera Controls (movement)
            float camMoveSpeed = 5 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
                camMoveSpeed *= camMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0, camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0, -camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
                cameraHelper.setPosition(0, 0);
            }
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
        BunnyHead bunnyHead = level.bunnyHead;
        float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));
        if (heightDifference > 0.25f){
            boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
            if ( hitRightEdge){
                bunnyHead.position.x = rock.position.x + rock.bounds.width;
            }
            else {
                bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
            }
            return;
        }

        switch (bunnyHead.jump_state){
            case GROUNDED:
                break;
            case FALLING:
            case JUMP_FALLING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                bunnyHead.jump_state = BunnyHead.JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                break;
        }
    }

    private void onCollisionBunnyWithGoldCoin(GoldCoin goldCoin){
        goldCoin.collected = true;
        score += goldCoin.getScore();
        Gdx.app.log(TAG, "Gold coin collected");
    }

    private void onCollisionBunnyWithFeather(Feather feather){
        feather.collected = true;
        score += feather.getScore();
        level.bunnyHead.setFeatherPowerup(true);
        Gdx.app.log(TAG, "Feather Collected");
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

    public boolean isGameOver(){
        return lives < 0;
    }

    public boolean isPlayerInWater(){
        return level.bunnyHead.position.y < -5;
    }
}
