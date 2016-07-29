package com.wolfkhan66.wastedwhale.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.wolfkhan66.wastedwhale.game.Assets;
import com.wolfkhan66.wastedwhale.util.Constants;

/**
 * Created by Cai Lehwald on 29/07/2016.
 */
public class BunnyHead extends AbstractGameObject {
    public static final String TAG = BunnyHead.class.getName();
    private final float JUMP_TIME_MAX = 0.3f;
    private final float JUMP_TIME_MIN = 0.1f;
    private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

    public enum VIEW_DIRECTION {LEFT, RIGHT}

    public enum JUMP_STATE {GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING}

    private TextureRegion regHead;

    public VIEW_DIRECTION view_direction;
    public float timeJumping;
    public JUMP_STATE jump_state;
    public boolean hasFeatherPowerup;
    public float timeLeftFeatherPowerup;

    public BunnyHead(){
        init();
    }

    public void init(){
        dimension.set(1, 1);
        regHead = Assets.instance.bunny.bunny;
        // center image on game object
        origin.set(dimension.x / 2, dimension.y / 2);
        // Bounding box for collision detection;
        bounds.set(0, 0, dimension.x, dimension.y);
        // Set physics values
        terminalVelocity.set(3.0f, 4.0f);
        friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, -25.0f);
        // View direction
        view_direction = VIEW_DIRECTION.RIGHT;
        // Jump state
        jump_state = JUMP_STATE.FALLING;
        timeJumping = 0;
        // Power-ups
        hasFeatherPowerup = false;
        timeLeftFeatherPowerup = 0;
    };

    public void setJumping(boolean jumpKeyPressed){
        switch (jump_state){
            case GROUNDED: // Character is standing on a platform
                if (jumpKeyPressed){
                    // Start counting jump time from the beginning
                    timeJumping = 0;
                    jump_state = JUMP_STATE.JUMP_RISING;
                }
                break;
            case JUMP_RISING: // Rising in the air
                if ( !jumpKeyPressed) {
                    jump_state = JUMP_STATE.JUMP_FALLING;
                }
                    break;
            case FALLING: // Falling down
            case JUMP_FALLING: // Falling Down after jump
                if (jumpKeyPressed && hasFeatherPowerup){
                    timeJumping = JUMP_TIME_OFFSET_FLYING;
                    jump_state = JUMP_STATE.JUMP_RISING;
                }
                break;
        }
    }

    public void setFeatherPowerup(boolean pickedUp){
        hasFeatherPowerup = pickedUp;
        if (pickedUp){
            timeLeftFeatherPowerup = Constants.ITEM_FEATHER_POWERUP_DURATION;
        }
    };

    public boolean hasFeatherPowerup(){
        return hasFeatherPowerup && timeLeftFeatherPowerup > 0;
    };
}
