package cc.raintomorrow.phase;

import cc.raintomorrow.BlockerActor;
import cc.raintomorrow.EcgStage;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import javax.swing.text.html.HTMLDocument;

public class GeneratingGate {
    private EcgStage stage;

    private float intensityFactor = 1;
    private float interval;
    private float damage;
    private float blockerWidth;

    private float fromLastTime;
    private float previousY;

    public GeneratingGate(EcgStage stage, float interval, float damage, float blockerWidth) {
        this.stage = stage;
        this.damage = damage;
        this.interval = interval;
        this.blockerWidth = blockerWidth;
    }

    public void update(float deltaTime) {
        fromLastTime += deltaTime;
    }

    public void setIntensityFactor(float intensityFactor) {
        this.intensityFactor = intensityFactor;
    }

    public BlockerActor[] tryToGenerate() {
        if(fromLastTime > interval) {
            fromLastTime = 0;
            float y;
            do {
                y = MathUtils.random(180, 540);
            } while(Math.abs(y-previousY) < 150 || Math.abs(y-previousY) > 250);
            previousY = y;
            float h = stage.getHeight();
            BlockerActor top = new BlockerActor(-damage*intensityFactor,
                    stage.getScrollX()+stage.getWidth(), y + h / 2 + h,
                    blockerWidth*intensityFactor, h);
            BlockerActor bottom = new BlockerActor(-damage*intensityFactor,
                    stage.getScrollX()+stage.getWidth(), y - h / 2 - h,
                    blockerWidth*intensityFactor, h);
            top.addAction(Actions.sequence(
                    Actions.moveBy(0, -h-2, 1, Interpolation.pow2),
                    Actions.delay(1.25f),
                    Actions.moveBy(0, h, 6)
            ));
            bottom.addAction(Actions.sequence(
                    Actions.moveBy(0, h+2, 1, Interpolation.pow2),
                    Actions.delay(1.25f),
                    Actions.moveBy(0, -h, 6)
            ));
            return new BlockerActor[] { top, bottom };
        }
        return null;
    }
}
