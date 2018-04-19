package com.brkyzdmr.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.brkyzdmr.flappybird.FlappyBird;

public class HomeState extends State {
    private static final int GROUND_Y_OFFSET = -50;
    private Texture background, playButton, logo, ground;
    private BitmapFont font;
    private Vector2 posGround;

    public HomeState(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);
        background = new Texture("sprites/background-day.png");
        logo = new Texture("sprites/flappy_logo.png");
        playButton = new Texture("sprites/play_button.png");
        ground = new Texture("sprites/base.png");
        posGround = new Vector2(camera.position.x - camera.viewportWidth / 2, GROUND_Y_OFFSET);
        this.createFont();

    }
    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.set(new TipState(gsm));
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
        sb.draw(background, 0, 0);
        sb.draw(ground, posGround.x, posGround.y);
        sb.draw(logo, camera.position.x - 95, camera.position.y + 80, 190, 63);
        sb.draw(playButton, camera.position.x - playButton.getWidth()/2, camera.position.y - 80);
        font.draw(sb, "(c) brkyzdmr - 2018", camera.position.x - 50, camera.position.y - 170);
        sb.end();
    }

    private void createFont() {
        FreeTypeFontGenerator genarator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        font = genarator.generateFont(parameter);
        genarator.dispose();
    }

    @Override
    public void dispose() {
        background.dispose();
        playButton.dispose();
        font.dispose();
        logo.dispose();
        ground.dispose();
    }
}
