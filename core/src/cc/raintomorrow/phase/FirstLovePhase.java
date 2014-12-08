package cc.raintomorrow.phase;

import cc.raintomorrow.BlockerActor;
import cc.raintomorrow.BullyActor;
import cc.raintomorrow.Ecg;
import cc.raintomorrow.EcgStage;
import cc.raintomorrow.cutscene.Cutscene;
import cc.raintomorrow.cutscene.TextCutscene;
import com.badlogic.gdx.math.Vector2;

public class FirstLovePhase extends Phase {
    GeneratingLine line;

    public FirstLovePhase(EcgStage stage) {
        super(stage);

        cutscenes = new Cutscene[] {
                new TextCutscene("\"Then I met her.\""),
                new TextCutscene("\"She always trusted me.\nNo matter what others said.\"")
        };
        this.hpConsumption = 60;

        this.line = new GeneratingLine(stage);
        line.setInterval(0.5f, 1.5f);
        line.setY(180, 540);
        line.setBlockerSize(40, 40, 10, 10);
        line.setBaseDamage(80);
        line.setBaseHeal(100);
        line.setHealProb(0.25f);
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

        line.update(deltaTime);
        BlockerActor blocker = line.tryToGenerate();
        if(blocker != null)
            stage.addActor(blocker);

        line.setIntensityFactor(intensityFactor*1.75f);

        if(intensityFactor > 1.5) {
            this.waveSpeed = new Vector2(270, 650);
            this.bumperRange = 240;
            this.hpRegen = 50;
            line.setY(240, 480);
            line.setBlockerSize(20, 20, 5, 5);
        }
        else if(intensityFactor > 1) {
            this.waveSpeed = new Vector2(250, 500);
            this.bumperRange = 280;
            this.hpRegen = 65;
            line.setY(220, 500);
            line.setBlockerSize(30, 30, 7.5f, 7.5f);
        }
        else {
            this.waveSpeed = new Vector2(225, 450);
            this.bumperRange = 320;
            this.hpRegen = 80;
            line.setY(200, 520);
            line.setBlockerSize(40, 40, 10, 10);
        }
    }

}
