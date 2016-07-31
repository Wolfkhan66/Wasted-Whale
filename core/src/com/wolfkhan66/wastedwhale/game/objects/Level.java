package com.wolfkhan66.wastedwhale.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Cai Lehwald on 26/07/2016.
 */
public class Level {
    public static final String TAG  = Level.class.getName();
    public BunnyHead bunnyHead;
    public Array<GoldCoin> goldCoins;
    public Array<Feather> feathers;

    public enum BLOCK_TYPE{
        EMPTY(0, 0, 0), // Black
        ROCK(0, 255, 0), // Green
        PLAYER_SPAWNPOINT(255, 255, 255), // White
        ITEM_FEATHER(255, 0, 255), // Purple
        ITEM_GOLD_COIN(255, 255, 0); // Yellow

        private int color;

        private  BLOCK_TYPE (int r, int g, int b){
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColor (int color){
            return this.color == color;
        }

        public int getColor(){
            return color;
        }
    }

    // Objects
    public Array<Rock> rocks;

    // Decoration
    public Clouds clouds;
    public Mountains mountains;
    public WaterOverlay waterOverlay;

    public Level (String filename){
        init(filename);
    }

    private void init (String filename){
        // Objects
        rocks = new Array<Rock>();
        goldCoins = new Array<GoldCoin>();
        feathers = new Array<Feather>();

        // Load image file that represents the level data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));

        // Scan pixels from top-left to bottom-right
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++){
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++){
                AbstractGameObject obj = null;
                float offsetHeight = 0;

                // Height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;

                // Get color of current pixel as 32-bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);

                // Find matching color value to identify block type at (x,y)
                // point and create the corresponding game object if there is
                // a match

                // Empty Space
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)){
                    // Do Nothing
                }
                else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)){
                    if (lastPixel != currentPixel){
                        obj = new Rock();
                        float heightIncreaseFactor = 0.25f;
                        offsetHeight = -2.5f;
                        obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
                        rocks.add((Rock)obj);
                    }
                    else {
                        rocks.get(rocks.size - 1).increaseLength(1);
                    }
                }
                // Player spawn point
                else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)){
                    obj = new BunnyHead();
                    offsetHeight = -3.0f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    bunnyHead = (BunnyHead)obj;
                }

                // Feather
                else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)){
                    obj = new Feather();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    feathers.add((Feather)obj);
                }

                // Gold Coin
                else if(BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)){
                    obj = new GoldCoin();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    goldCoins.add((GoldCoin)obj);
                }

                // Unknown object/pixel color
                else {
                    int r = 0xff & (currentPixel >>> 24); // Red
                    int g = 0xff & (currentPixel >>> 16); // Green
                    int b = 0xff & (currentPixel >>> 8);  // Blue
                    int a = 0xff & currentPixel;          // Alpha
                    Gdx.app.error(TAG, "Unknown object at X<" + pixelX + "> Y<" + pixelY + ">: r<" + r+ "> g <" + g + "> b<" + b + "> a <" + a +">");
                }
                lastPixel = currentPixel;
            }
        }

        // Decoration
        clouds = new Clouds(pixmap.getWidth());
        clouds.position.set(0, 2);
        mountains = new Mountains(pixmap.getWidth());
        mountains.position.set(-1, -1);
        waterOverlay = new WaterOverlay(pixmap.getWidth());
        waterOverlay.position.set(0, -3.75f);

        // Free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "level '" + filename + "' loaded");
    }

    public void render (SpriteBatch batch){
        // Draw Mountains
        mountains.render((batch));

        // Draw Rocks
        for (Rock rock : rocks){
            rock.render(batch);
        }

        // Draw Water Overlay
        waterOverlay.render(batch);

        // Draw Clouds
        clouds.render(batch);
    }
}