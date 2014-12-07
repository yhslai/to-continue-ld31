package cc.raintomorrow.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animator {
    private Animation animation;
    private float stateTime;
    private Runnable callback;

    public Animator(float frameDuration, Texture sheet,
                    int row, int column,
                    int startFrame, int endFrame,
                    Animation.PlayMode playMode) {
        TextureRegion[][] tmp = TextureRegion.split(sheet,
                sheet.getWidth()/column, sheet.getHeight()/row);
        Array<TextureRegion> frames = new Array<TextureRegion>(row*column);
        if(startFrame < endFrame) {
            for (int i = startFrame; i < endFrame; i++) {
                frames.add(tmp[i / column][i % column]);
            }
        }
        else {
            for (int i = startFrame; i > endFrame; i--) {
                frames.add(tmp[i / column][i % column]);
            }
        }
        animation = new Animation(frameDuration, frames, playMode);
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    public TextureRegion getKeyFrame(float stateTime) {
        return animation.getKeyFrame(stateTime);
    }

    public TextureRegion getKeyFrame() {
        return getKeyFrame(stateTime);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if(isEnded() && callback != null) {
            callback.run();
            callback = null;
        }
    }

    public void restart() {
        stateTime = 0;
    }

    public boolean isEnded() {
        return stateTime >= animation.getFrameDuration() * animation.getKeyFrames().length;
    }
}
