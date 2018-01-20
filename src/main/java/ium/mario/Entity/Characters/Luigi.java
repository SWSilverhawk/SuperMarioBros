package ium.mario.Entity.Characters;

import ium.mario.Entity.Animation;
import ium.mario.Entity.MapObject;
import ium.mario.GameState.SpritesLoader;
import ium.mario.TileMap.TileMap;

import java.awt.*;

public class Luigi extends MapObject {

    public Luigi(TileMap tileMap, SpritesLoader spritesLoader) {

        super(tileMap);

        width = 16;
        height = 16;
        cwidth = 15;
        cheight = 15;

        fallSpeed = 0.15;
        maxFallSpeed = 4.0;

        facingRight = false;

        animation = new Animation();
        animation.setFrames(spritesLoader.getLuigiIdle());
        animation.setDelay(-1);

    }

    // GetNextPosition
    private void getNextPosition() {
        if (falling) {
            dy += fallSpeed;
            if (dy > 0) jumping = false;
            if (dy < 0 && !jumping) dy += stopJumpSpeed;
            if (dy > maxFallSpeed) dy = maxFallSpeed;
        }
    }

    // Update
    public void update() {
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
    }

    // Draw
    public void draw(Graphics2D graphics) {
        setMapPosition();
        super.draw(graphics);
    }

}
