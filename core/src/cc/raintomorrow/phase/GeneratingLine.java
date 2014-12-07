package cc.raintomorrow.phase;

import cc.raintomorrow.BlockerActor;
import cc.raintomorrow.EcgStage;
import com.badlogic.gdx.math.MathUtils;

public class GeneratingLine {
    private EcgStage stage;
    private float intervalMin;
    private float intervalMax;
    private float top;
    private float bottom;
    private float blockerWidthMin;
    private float blockerWidthMax;
    private float blockerHeightMin;
    private float blockerHeightMax;
    private float baseHeal;
    private float baseDamage;
    private float healProb;

    private float intensityFactor = 1;

    private float fromLastTime;

    GeneratingLine(EcgStage stage) {
        this.stage = stage;
    }

    public void setInterval(float intervalMin, float intervalMax) {
        this.intervalMin = intervalMin;
        this.intervalMax = intervalMax;
    }

    public void setY(float bottom, float top) {
        this.top = top;
        this.bottom = bottom;
    }

    public void setBlockerSize(float blockerWidthMin, float blockerWidthMax,
                               float blockerHeightMin, float blockerHeightMax) {
        this.blockerWidthMin = blockerWidthMin;
        this.blockerWidthMax = blockerWidthMax;
        this.blockerHeightMin = blockerHeightMin;
        this.blockerHeightMax = blockerHeightMax;
    }

    public void setBaseHeal(float baseHeal) {
        this.baseHeal = baseHeal;
    }

    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    public void setHealProb(float healProb) {
        this.healProb = healProb;
    }

    public void setIntensityFactor(float intensityFactor) {
        this.intensityFactor = intensityFactor;
    }

    public void update(float deltaTime) {
        fromLastTime += deltaTime;
    }

    public BlockerActor tryToGenerate() {
        float prob = Generating.unlerp(fromLastTime * intensityFactor, intervalMin, intervalMax);
        if(MathUtils.random() < prob) {
            fromLastTime = 0;
            float blockerX = stage.getScrollX()+stage.getWidth();
            float blockerY = MathUtils.random(bottom, top);
            float blockerWidth = MathUtils.random(blockerWidthMin, blockerWidthMax);
            float blockerHeight = MathUtils.random(blockerHeightMin, blockerHeightMax);
            float hpChange = MathUtils.random() < healProb ? baseHeal : -baseDamage;
            hpChange *= blockerWidth / (blockerWidthMin + blockerWidthMax) * 2 *
                    blockerHeight / (blockerHeightMin + blockerHeightMax) * 2;

            return new BlockerActor(hpChange,
                    blockerX, blockerY, blockerWidth, blockerHeight);
        }
        return null;
    }
}
