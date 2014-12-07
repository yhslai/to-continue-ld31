package cc.raintomorrow.phase;

import cc.raintomorrow.BlockerActor;
import cc.raintomorrow.BullyActor;
import cc.raintomorrow.Ecg;
import cc.raintomorrow.EcgStage;

public class RejectionPhase extends Phase {
    GeneratingGate gate;

    public RejectionPhase(EcgStage stage) {
        super(stage);

        this.gate = new GeneratingGate(stage, 1.25f, 50, 20);
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
        super.update(deltaTime);

        float intensityFactor = Generating.intensityFactor(Ecg.state.hp, Ecg.state.MAX_HP);

        gate.update(deltaTime);
        BlockerActor [] blockers = gate.tryToGenerate();
        if(blockers != null) {
            for(BlockerActor blocker : blockers)
                stage.addActor(blocker);
        }
        gate.setIntensityFactor(intensityFactor*1.5f);
    }

}
