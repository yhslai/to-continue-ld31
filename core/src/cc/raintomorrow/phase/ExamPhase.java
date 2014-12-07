package cc.raintomorrow.phase;

import cc.raintomorrow.BlockerActor;
import cc.raintomorrow.Ecg;
import cc.raintomorrow.EcgStage;
import cc.raintomorrow.Phase;
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

    @Override
    public void update(float deltaTime) {
        fromLastTime += deltaTime;
        intensityFactor = Generating.intensityFactor(Ecg.state.hp, Ecg.state.MAX_HP);
        float intervalMin = 1f;
        float intervalMax = 2f;
        float prob = MathUtils.clamp((fromLastTime * intensityFactor - intervalMin) / (intervalMax - intervalMin), 0, 1);
        if(MathUtils.random() < prob) {
            fromLastTime = 0;
            float blockerX = stage.getScrollX()+stage.getWidth();
            float blockerY = MathUtils.random(500, 640);
            float hpChange;
            float blockerWidth;
            float blockerHeight;
            if(MathUtils.random() < 0.25) {
                hpChange = 50;
                blockerWidth = 15;
                blockerHeight = 15;
            }
            else {
                hpChange = -100;
                blockerWidth = 25;
                blockerHeight = 25;
            }
            BlockerActor blocker = new BlockerActor(hpChange,
                    blockerX, blockerY, blockerWidth, blockerHeight);
            blocker.setSpeed(new Vector2(
                    20, -100));
            stage.addActor(blocker);
        }
    }
}
