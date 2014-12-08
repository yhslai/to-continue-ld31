package cc.raintomorrow.phase;

import cc.raintomorrow.*;
import cc.raintomorrow.cutscene.Cutscene;
import cc.raintomorrow.cutscene.TextCutscene;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RejectionPhase extends Phase {
    private GeneratingGate gate;
    private ArrayList<EcgActor> actors = new ArrayList<EcgActor>();

    public RejectionPhase(EcgStage stage) {
        super(stage);

        cutscenes = new Cutscene[] {
                new TextCutscene("\"We were very different.\""),
                new TextCutscene("\"I could see a solid barrier\nstanding between us.\"")
        };

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
        for(EcgActor actor : actors)
            actor.remove();
        actors.clear();
        stage.getWave().setPosition(new Vector2(stage.getWave().getPosition().x, 360));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        float intensityFactor = Generating.intensityFactor(Ecg.state.hp, Ecg.state.MAX_HP);

        gate.update(deltaTime);
        BlockerActor [] blockers = gate.tryToGenerate();
        if(blockers != null) {
            for(BlockerActor blocker : blockers) {
                stage.addActor(blocker);
                actors.add(blocker);
            }
        }
        gate.setIntensityFactor(intensityFactor*1.5f);
    }

}
