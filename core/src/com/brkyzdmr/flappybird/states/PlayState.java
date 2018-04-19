package com.brkyzdmr.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brkyzdmr.flappybird.FlappyBird;
import com.brkyzdmr.flappybird.sprites.Bird;
import com.brkyzdmr.flappybird.sprites.Tube;

public class PlayState extends State {
    private static final int TUBE_SPACING = 125;        // Space beetwen two tube
    private static final int TUBE_COUNT = 4;            // Number of tubes that created
    private static final int GROUND_Y_OFFSET = -50;

    private Bird bird;
    private Texture background, ground, scoreboard;
    private Array<Tube> tubes;
    private Vector2 posGround1, posGround2;
    private Sound sfx_hit, sfx_point;
    private BitmapFont font;
    private static int point = 0;
    private boolean isPointed = true;
    private boolean isDetecting = false;
    private boolean isPaused = false;
    private AssetManager manager = new AssetManager();

    public PlayState(GameStateManager gsm) {
        super(gsm);
        point = 0;
        bird = new Bird(50,125);
        camera.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);

        manager.load("sprites/background-day.png", Texture.class);
        manager.load("sprites/base.png", Texture.class);
        manager.load("sprites/scoreboard.png", Texture.class);
        manager.load("sounds/sfx_hit.ogg", Sound.class);
        manager.load("sounds/sfx_point.ogg", Sound.class);

        manager.finishLoading();
        if(manager.update()) {
            background = manager.get("sprites/background-day.png", Texture.class);
            ground = manager.get("sprites/base.png", Texture.class);
            scoreboard = manager.get("sprites/scoreboard.png", Texture.class);
            sfx_hit = manager.get("sounds/sfx_hit.ogg", Sound.class);
            sfx_point = manager.get("sounds/sfx_point.ogg", Sound.class);
        }


        posGround1 = new Vector2(camera.position.x - camera.viewportWidth / 2, GROUND_Y_OFFSET);
        posGround2 = new Vector2((camera.position.x - camera.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);

        tubes = new Array<Tube>();
        for (int i=1; i<=TUBE_COUNT; i++) {
            tubes.add(new Tube(i*(TUBE_SPACING + Tube.TUBE_WIDTH)));
        }

        this.createFont();
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {
            bird.jump();
        }
    }

    @Override
    public void update(float deltaTime) {
        if(!isPaused) {
            handleInput();
            bird.update(deltaTime);
            updateGround();
            camera.position.x = bird.getPosition().x + 80;  // reposition the camera position

            // Reposition the tube that off from camera viewport
            for (int i=0; i<tubes.size; i++) {
                Tube tube = tubes.get(i);
                if(camera.position.x - (camera.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                    tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
                }

                if(tube.collides(bird.getBounds())) {
                    sfx_hit.play();
                    isPaused=true;
                }

                if(bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
                    sfx_hit.play();
                    isPaused=true;
                }

                if ((bird.getPosition().x >= tube.getPosTopTube().x + tube.getTopTube().getWidth() / 3) &&
                        (bird.getPosition().x <= tube.getPosTopTube().x + 2 * tube.getTopTube().getWidth() / 3) &&
                        (bird.getPosition().y <= tube.getPosTopTube().y) &&
                        (bird.getPosition().y >= tube.getPosBotTube().y) &&
                        isPointed) {
                    System.out.println("Point!");
                    point++;
                    sfx_point.play();
                    isPointed = false;
                } else {
                    isPointed = true;
                }
                System.out.println(isPointed);
            }
            camera.update();
        } else {
            if(Gdx.input.justTouched()) {
                gsm.set(new PlayState(gsm));
                FlappyBird.getInstance().resume();
            }
        }


    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background, camera.position.x - (camera.viewportWidth / 2), 0);
        sb.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y);
        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        sb.draw(ground, posGround1.x, posGround1.y);
        sb.draw(ground, posGround2.x, posGround2.y);

        font.draw(sb, "" + point/10, camera.position.x - 5, camera.position.y + 175);

        sb.end();
    }

    private void updateGround() {
        if(camera.position.x - (camera.viewportWidth / 2) > posGround1.x + ground.getWidth()) {
            posGround1.add(ground.getWidth() * 2, 0);
        }
        if(camera.position.x - (camera.viewportWidth / 2) > posGround2.x + ground.getWidth()) {
            posGround2.add(ground.getWidth() * 2, 0);
        }
    }

    private void createFont() {
        FreeTypeFontGenerator genarator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        font = genarator.generateFont(parameter);
        genarator.dispose();
    }

    @Override
    public void dispose() {
        background.dispose();
        bird.dispose();
        for(Tube tube : tubes) {
            tube.dispose();
        }
        ground.dispose();
        sfx_hit.dispose();
        sfx_point.dispose();
        scoreboard.dispose();
        manager.dispose();
        System.out.println("Play state disposed!");
    }
}
