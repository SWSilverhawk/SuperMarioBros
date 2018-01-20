package ium.mario.Entity.Characters;

import ium.mario.Entity.Animation;
import ium.mario.Entity.Enemy;
import ium.mario.GameState.SpritesLoader;
import ium.mario.GamePanel;
import ium.mario.TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Goomba extends Enemy {

    private ArrayList<BufferedImage[]> sprites;
    private static final int MOVE = 0;
    private static final int DEATH = 1;

    public Goomba(TileMap tileMap, SpritesLoader spritesLoader) {

        super(tileMap);

        moveSpeed = 0.3;
        maxSpeed = 0.3;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = 16;
        height = 16;
        cwidth = 15;
        cheight = 15;

        health = 1;
        damage = 1;

        sprites = new ArrayList<>();
        sprites.add(spritesLoader.getGoombaMove());
        sprites.add(spritesLoader.getGoombaDeath());
        animation = new Animation();
        animation.setFrames(sprites.get(MOVE));
        animation.setDelay(300);

        right = true;
        facingRight = true;
        isGoomba = true;

    }

    private void getNextPosition() {

        if (left) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) dx = -maxSpeed;
        } else if (right) {
            dx += moveSpeed;
            if (dx > maxSpeed) dx = maxSpeed;
        }

        if (falling) {
            dy += fallSpeed;
        }

    }


    public void update() {

        // Update Position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        if (dx == 0) {
            right = !right;
            left = !left;
            facingRight = right;
        }

        animation.update();

        if (animation.hasPlayedOnce() && health == 0) dead = true;

        if (GamePanel.HEIGHT - ytemp - height < 0) hit();

    }

    public void hit() {
        animation.setFrames(sprites.get(DEATH));
        animation.setDelay(0);
        health = 0;
        damage = 0;
    }

    public void draw(Graphics2D graphics) {
        setMapPosition();
        super.draw(graphics);
    }

}
