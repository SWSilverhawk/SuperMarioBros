package ium.mario.Entity;

import ium.mario.TileMap.*;

import java.awt.*;

public abstract class MapObject {

    private TileMap tileMap;
    private int tileSize;
    private double xmap;
    private double ymap;

    // Position
    private double x;
    private double y;
    protected double dx;
    protected double dy;

    // Dimensions
    protected int width;
    protected int height;

    // Collision
    protected int cwidth;
    protected int cheight;
    protected double xtemp;
    protected double ytemp;
    private boolean topLeft;
    private boolean topRight;
    private boolean bottomLeft;
    private boolean bottomRight;

    // Animation
    protected Animation animation;
    protected boolean facingRight;
    protected int currentAction;

    // Movement
    protected boolean left;
    protected boolean right;
    protected boolean falling;
    protected boolean jumping;

    protected double moveSpeed;
    protected double maxSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double stopSpeed;
    protected double jumpStart;
    protected double stopJumpSpeed;

    protected boolean isGoomba;


    protected MapObject(TileMap tileMap) {
        this.tileMap = tileMap;
        this.tileSize = tileMap.getTileSize();
        isGoomba = false;
    }

    protected boolean intersects(MapObject object) {
        Rectangle r1 = getRectangle();
        Rectangle r2 = object.getRectangle();
        return r1.intersects(r2);
    }

    public Rectangle getRectangle() { return new Rectangle((int)x - cwidth, (int)y - cheight, cwidth, cheight); }

    private void calculateCorners(double x, double y) {

        int leftTile = (int) (x - cwidth / 2) / tileSize;
        int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
        int topTile = (int) (y - cheight / 2) / tileSize;
        int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;

        topLeft = !(topTile >= 0 && leftTile > 0) || tileMap.isBlock(topTile, leftTile);
        topRight = !(topTile >= 0 && rightTile < tileMap.getNumCols()) || tileMap.isBlock(topTile, rightTile);
        bottomLeft = !(bottomTile < tileMap.getNumRows() && leftTile > 0) || tileMap.isBlock(bottomTile, leftTile);
        bottomRight = !(bottomTile < tileMap.getNumRows() && rightTile < tileMap.getNumCols()) || tileMap.isBlock(bottomTile, rightTile);

    }

    protected void checkTileMapCollision() {
        int currCol = (int)x / tileSize;
        int currRow = (int)y / tileSize;

        xtemp = x;
        ytemp = y;

        calculateCorners(x, y + dy);
        if (dy < 0) {
            if (topLeft || topRight) {
                dy = 0;
                ytemp = currRow * tileSize + cheight / 2;
            } else ytemp += dy;
        }
        if (dy > 0) {
            if (bottomLeft || bottomRight) {
                dy = 0;
                falling = false;
                ytemp = (currRow + 1) * tileSize - cheight / 2;
            } else ytemp += dy;
        }

        if (isGoomba && !tileMap.isBlock(currRow + 1, (int)(x + dx) / tileSize) && !falling) {
            dx = 0;
        }

        calculateCorners(x + dx, y);
        if (dx < 0) {
            if (topLeft || bottomLeft) {
                dx = 0;
                xtemp = currCol * tileSize + cwidth / 2;
            } else xtemp += dx;
        }
        if (dx > 0) {
            if (topRight || bottomRight) {
                dx = 0;
                xtemp = (currCol + 1) * tileSize - cwidth / 2;
            } else xtemp += dx;
        }

        if (!falling) {
            calculateCorners(x , y + dy + 1 );
            if (!bottomLeft && !bottomRight) falling = true;
        }
    }

    public void draw(Graphics2D graphics) {
        if (facingRight) graphics.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
        else graphics.drawImage(animation.getImage(), (int)(x + xmap + width / 2), (int)(y + ymap - height / 2), -width, height, null);
    }

    public int getx() { return (int)x; }
    public int gety() { return (int)y; }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    protected void setMapPosition() {
        xmap = tileMap.getPosX();
        ymap = tileMap.getPosY();
    }

    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
    public void setJumping(boolean jumping) { this.jumping = jumping; }
}
