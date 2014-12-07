package cc.raintomorrow.phase;

import cc.raintomorrow.BlockerActor;
import cc.raintomorrow.Ecg;
import cc.raintomorrow.EcgStage;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ExamPhase extends Phase {
    private float fromLastTime;
    private float intensityFactor;

    public ExamPhase(EcgStage stage) {
        super(stage);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void end() {
        super.end();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        fromLastTime += deltaTime;
        intensityFactor = Generating.intensityFactor(Ecg.state.hp, Ecg.state.MAX_HP);
        float intervalMin = 0.5f;
        float intervalMax = 2f;
        float prob = Generating.unlerp(fromLastTime * intensityFactor, intervalMin, intervalMax);
        if(MathUtils.random() < prob) {
            fromLastTime = 0;
            float blockerX = stage.getScrollX()+stage.getWidth();
            float blockerY = MathUtils.random(0, 100);
            float hpChange;
            float blockerWidth;
            float blockerHeight;
            if(MathUtils.random() < 0.25) {
                hpChange = 50;
                blockerWidth = MathUtils.random(20, 30);
                blockerHeight = blockerWidth;
            }
            else {
                hpChange = -100;
                blockerWidth = MathUtils.random(30, 45);
                blockerHeight = blockerWidth;
            }
            BlockerActor blocker = new BlockerActor(hpChange,
                    blockerX, blockerY, blockerWidth, blockerHeight);
            if(MathUtils.random() < 0.25) {
                blocker.setSpeed(new Vector2(
                        MathUtils.random(-50, 50), MathUtils.random(50, 75)));
            }
            else {
                blocker.setSpeed(new Vector2(
                        MathUtils.random(-100, 100), MathUtils.random(100, 150)));
            }
            stage.addActor(blocker);
        }
    }
}
