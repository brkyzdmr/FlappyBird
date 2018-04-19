package com.brkyzdmr.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.brkyzdmr.flappybird.FlappyBird;

public class TipState extends State {
    private static final int GROUND_Y_OFFSET = -50;
    private Texture background, splash, ground;
    private Vector2 posGround;

    public TipState(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);
        background = new Texture("sprites/background-day.png");
        splash = new Texture("sprites/splash.png");
        ground = new Texture("sprites/base.png");
        posGround = new Vector2(camera.position.x - camera.viewportWidth / 2, GROUND_Y_OFFSET);
    }
    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float deltaTime) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background, 0,0);
        sb.draw(splash, camera.position.x - splash.getWidth() / 2, camera.position.y);
        sb.draw(ground, posGround.x, posGround.y);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        splash.dispose();
        ground.dispose();
        System.out.println("Menu satate disposed!");

    }
}
