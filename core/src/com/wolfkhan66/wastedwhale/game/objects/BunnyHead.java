package com.wolfkhan66.wastedwhale.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

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

    public void init(){};
    public void setJumping(boolean jumpKeyPressed){};
    public void setFeatherPowerup(boolean pickedUp){};
    public boolean hasFeatherPowerup(){};
}
