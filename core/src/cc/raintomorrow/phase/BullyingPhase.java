package cc.raintomorrow.phase;

import cc.raintomorrow.BlockerActor;
import cc.raintomorrow.BullyActor;
import cc.raintomorrow.Ecg;
import cc.raintomorrow.EcgStage;
import cc.raintomorrow.cutscene.Cutscene;
import cc.raintomorrow.cutscene.TextCutscene;
import cc.raintomorrow.graphics.Direction;

public class BullyingPhase extends Phase {
    private BullyActor bully1;
    private BullyActor bully2;
    private BullyActor bully3;
    GeneratingLine [] generatingLines;

    public BullyingPhase(EcgStage stage) {
        super(stage);
        bully1 = new BullyActor(50, 0, 1);
        bully2 = new BullyActor(50, 0.5f, 1.5f);
        bully3 = new BullyActor(50, 1, 2);

        cutscenes = new Cutscene[] {
                new TextCutscene("\"And lots of people...\""),
                new TextCutscene("\"lying to and slandering me.\"")
        };

        GeneratingLine topOut = new GeneratingLine(stage);
        topOut.setInterval(1f, 24);
        topOut.setY(560, 700);
        topOut.setBlockerSize(45, 45, 25, 25);
        topOut.setBaseDamage(0);
        topOut.setBaseHeal(100);
        topOut.setHealProb(1);

        GeneratingLine bottomOut = new GeneratingLine(stage);
        bottomOut.setInterval(1f, 24f);
        bottomOut.setY(20, 160);
        bottomOut.setBlockerSize(45, 45, 25, 25);
        bottomOut.setBaseDamage(0);
        bottomOut.setBaseHeal(100);
        bottomOut.setHealProb(1);

        generatingLines = new GeneratingLine[] {
                topOut, bottomOut
        };
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        super.start();
        stage.addActor(bully1);
        stage.addActor(bully2);
        stage.addActor(bully3);
    }

    @Override
    public void end() {
        super.end();
        bully1.remove();
        bully2.remove();
        bully3.remove();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        float intensityFactor = Generating.intensityFactor(Ecg.state.hp, Ecg.state.MAX_HP);
        bully1.showUp();
        if(intensityFactor > 1) bully2.showUp();
        else bully2.leave();
        if(intensityFactor > 1.5) bully3.showUp();
        else bully3.leave();

        for(GeneratingLine line : generatingLines) {
            line.update(deltaTime);
            BlockerActor blocker = line.tryToGenerate();
            if(blocker != null)
                stage.addActor(blocker);

            line.setIntensityFactor(Generating.intensityFactor(Ecg.state.hp, Ecg.state.MAX_HP));
        }
    }

}
