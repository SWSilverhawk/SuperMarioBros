package ium.mario.Entity;

import ium.mario.TileMap.TileMap;

public class Enemy extends MapObject {

    protected int health;
    protected boolean dead;
    protected int damage;

    protected Enemy (TileMap tileMap) { super(tileMap); }

    public boolean isDead() { return dead; }
    public int getDamage() { return damage; }

    public void update() {}

    public void hit() {}

}
