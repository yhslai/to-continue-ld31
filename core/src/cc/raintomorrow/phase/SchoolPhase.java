package cc.raintomorrow.phase;

import cc.raintomorrow.BlockerActor;
import cc.raintomorrow.Ecg;
import cc.raintomorrow.EcgStage;
import cc.raintomorrow.Phase;
import com.badlogic.gdx.math.MathUtils;

public class SchoolPhase extends Phase {
    GeneratingLine [] generatingLines;

    public SchoolPhase(EcgStage stage) {
        super(stage);

        GeneratingLine topOut = new GeneratingLine(stage);
        topOut.setInterval(1.5f, 4f);
        topOut.setY(480, 500);
        topOut.setBlockerSize(20, 30, 5, 10);
        topOut.setBaseDamage(50);
        topOut.setBaseHeal(0);
        topOut.setHealProb(0);

        GeneratingLine bottomOut = new GeneratingLine(stage);
        bottomOut.setInterval(1.5f,4f);
        bottomOut.setY(220, 240);
        bottomOut.setBlockerSize(20, 30, 5, 10);
        bottomOut.setBaseDamage(50);
        bottomOut.setBaseHeal(0);
        bottomOut.setHealProb(0);

        GeneratingLine topIn = new GeneratingLine(stage);
        topIn.setInterval(2.5f, 4.5f);
        topIn.setY(400, 440);
        topIn.setBlockerSize(40, 60, 10, 15);
        topIn.setBaseDamage(100);
        topIn.setBaseHeal(125);
        topIn.setHealProb(0.2f);

        GeneratingLine bottomIn = new GeneratingLine(stage);
        bottomIn.setInterval(2.5f, 4.5f);
        bottomIn.setY(280, 320);
        bottomIn.setBlockerSize(40, 60, 10, 15);
        bottomIn.setBaseDamage(100);
        bottomIn.setBaseHeal(125);
        bottomIn.setHealProb(0.2f);

        generatingLines = new GeneratingLine[] {
                topOut, bottomOut, topIn, bottomIn
        };
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
        for(GeneratingLine line : generatingLines) {
            line.update(deltaTime);
            BlockerActor blocker = line.tryToGenerate();
            if(blocker != null)
                stage.addActor(blocker);

            line.setIntensityFactor(Generating.intensityFactor(Ecg.state.hp, Ecg.state.MAX_HP));
        }
    }

}
