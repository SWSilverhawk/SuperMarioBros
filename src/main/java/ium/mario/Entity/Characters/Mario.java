package ium.mario.Entity.Characters;

import ium.mario.Entity.*;
import ium.mario.GameState.SpritesLoader;
import ium.mario.GamePanel;
import ium.mario.TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Mario extends MapObject {

    private int health;
    private boolean dead;

    private ArrayList<BufferedImage[]> sprites;

    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int DYING = 3;
    private static final int FALLING = 4;

    public Mario(TileMap tileMap, SpritesLoader spritesLoader) {
        super(tileMap);

        width = 16;
        height = 16;
        cwidth = 15;
        cheight = 15;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = 1;

        sprites = new ArrayList<>();
        sprites.add(spritesLoader.getMarioIdle());
        sprites.add(spritesLoader.getMarioRun());
        sprites.add(spritesLoader.getMarioJump());
        sprites.add(spritesLoader.getMarioDeath());

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

    }

    public boolean isDead() { return dead; }

    // CheckAttack
    public void checkAttack(ArrayList<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (intersects(enemy)) {
                if (enemy.gety() - gety() < 8 && enemy.getDamage() != 0) health = 0;
                else enemy.hit();
            }
        }
    }

    // GetNextPosition
    private void getNextPosition() {

       // Movement
       if (left) {
           dx -= moveSpeed;
           if (dx < -maxSpeed) dx = -maxSpeed;
       } else if (right) {
           dx += moveSpeed;
           if (dx > maxSpeed) dx = maxSpeed;
       } else if (dx > 0) {
           dx -= stopSpeed;
           if (dx < 0) dx = 0;
       } else if (dx < 0) {
           dx += stopSpeed;
           if (dx > 0) dx = 0;
       }

       // Jumping
       if (jumping && !falling) {
           dy = jumpStart;
           falling = true;
       }

       // Falling
       if (falling) {
           dy += fallSpeed;
           if (dy > 0) jumping = false;
           if (dy < 0 && !jumping) dy += stopJumpSpeed;
           if (dy > maxFallSpeed) dy = maxFallSpeed;
       }

    }

    // Update
    public void update() {

        // Update Position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        if (GamePanel.HEIGHT - ytemp - height < 0) health = 0;

        // Set Animation
        if (health == 0) {
            if (currentAction != DYING) {
                currentAction = DYING;
                animation.setFrames(sprites.get(DYING));
                animation.setDelay(0);
            }
        } else if (dy > 0) {
            if (currentAction != FALLING) {
                currentAction = FALLING;
            }
        } else if (dy < 0) {
            if (currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
            }
        } else if (left || right) {
            if (currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
            }
        } else if (currentAction != IDLE) {
            currentAction = IDLE;
            animation.setFrames(sprites.get(IDLE));
            animation.setDelay(-1);
        }

        animation.update();

        // Set Direction
        if (currentAction == WALKING) facingRight = right;

        // Check Death
        if (currentAction == DYING && animation.hasPlayedOnce()) dead = true;

    }

    // Draw
    public void draw(Graphics2D graphics) {
        setMapPosition();
        super.draw(graphics);
    }

}
