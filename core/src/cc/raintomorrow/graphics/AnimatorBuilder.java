package cc.raintomorrow.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimatorBuilder {
    private Texture texture;
    private int row;
    private int column;

    public AnimatorBuilder(Texture texture, int row, int column) {
        this.texture = texture;
        this.row = row;
        this.column = column;
    }

    public Animator buildAnimator(float frameDuration, int startFrame, int endFrame,
                                  Animation.PlayMode playMode) {
        return new Animator(frameDuration, texture, row, column, startFrame, endFrame, playMode);
    }

    public Animator buildAnimator(float frameDuration, int startFrame, int endFrame) {
        return buildAnimator(frameDuration, startFrame, endFrame, Animation.PlayMode.NORMAL);
    }
}
