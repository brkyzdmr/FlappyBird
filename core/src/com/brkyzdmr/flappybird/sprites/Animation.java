package com.brkyzdmr.flappybird.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animation {
    private Array<TextureRegion> frames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int frameCount;
    private int frame;

    public Animation(TextureRegion region, int frameCount, float cycleTime) {
        frames = new Array<TextureRegion>();
        int frameHeight = region.getRegionHeight() / frameCount;

        for(int i=0; i<frameCount; i++) {
            frames.add(new TextureRegion(region, 0, i*frameHeight, region.getRegionWidth(), frameHeight));
        }
        this.frameCount = frameCount;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;
    }

    public void update(float deltaTime) {
        currentFrameTime += deltaTime;
        if(currentFrameTime > maxFrameTime) {
            frame++;
            currentFrameTime = 0;
        }
        if(frame >= frameCount) {
            frame = 0;
        }
    }

    public TextureRegion getFrame() {
        return frames.get(frame);
    }
}
