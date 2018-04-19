package com.brkyzdmr.flappybird.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.util.Random;

public class Tube {
    public static final int TUBE_WIDTH = 52;
    private static final int FLUCTUATION = 130;
    private static final int TUBE_GAP = 80;         // Space beetwen top and bottom tube
    private static final int LOWEST_OPENING = 100;

    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBotTube;
    private Random rand;
    private Rectangle boundsTop, boundsBot;
    private Vector3 rayPos, rayDir;
    private Ray pointRay;

    public Tube(float x) {
        topTube = new Texture("sprites/top-tube.png");
        bottomTube = new Texture("sprites/bottom-tube.png");
        rand = new Random();

        posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube = new Vector2(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());

        boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth(), topTube.getHeight());
        boundsBot = new Rectangle(posBotTube.x, posBotTube.y, bottomTube.getWidth(), bottomTube.getHeight());
        rayPos = new Vector3(posBotTube.x + topTube.getWidth()/2,posTopTube.y, 0);
        rayDir = new Vector3(0, -1, 0);
        pointRay = new Ray(rayPos, rayDir);
    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }

    public Ray getPointRay() {
        return pointRay;
    }

    public void reposition(float x) {
        posTopTube.set(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube.set(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
        boundsTop.setPosition(posTopTube.x, posTopTube.y);
        boundsBot.setPosition(posBotTube.x, posBotTube.y);
        setRayPosition(posBotTube.x + topTube.getWidth()/2,posTopTube.y);
    }

    private void setRayPosition(float x, float y) {
        rayPos = new Vector3(x, y, 0);
        pointRay = new Ray(rayPos, rayDir);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
    }

    public void dispose() {
        topTube.dispose();
        bottomTube.dispose();
    }
}
