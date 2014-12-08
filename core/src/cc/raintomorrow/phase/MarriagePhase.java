package cc.raintomorrow.phase;

import cc.raintomorrow.BlockerActor;
import cc.raintomorrow.BullyActor;
import cc.raintomorrow.Ecg;
import cc.raintomorrow.EcgStage;
import cc.raintomorrow.cutscene.Cutscene;
import cc.raintomorrow.cutscene.TextCutscene;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MarriagePhase extends Phase {
    private int subPhase = 0;
    private int subPhaseCount = 4;
    private GeneratingLine [] lines0;

    private float fromLastTime1;

    private BullyActor bully21;
    private BullyActor bully22;
    private BullyActor bully23;
    private GeneratingLine line2;

    private GeneratingLine line3;

    public MarriagePhase(EcgStage stage) {
        super(stage);

        cutscenes = new Cutscene[] {
                new TextCutscene("\"But I know.\""),
                new TextCutscene("\"As long as we're together...\"")
        };

        this.hpRegen = 60;
        this.hpConsumption = 25;
        this.waveSpeed = new Vector2(150, 300);
        this.initHp = 150;

        GeneratingLine topOut0 = new GeneratingLine(stage);
        topOut0.setInterval(2f, 4f);
        topOut0.setY(480, 500);
        topOut0.setBlockerSize(20, 30, 5, 10);
        topOut0.setBaseDamage(10);
        topOut0.setBaseHeal(0);
        topOut0.setHealProb(0.2f);

        GeneratingLine bottomOut0 = new GeneratingLine(stage);
        bottomOut0.setInterval(2f, 4f);
        bottomOut0.setY(220, 240);
        bottomOut0.setBlockerSize(20, 30, 5, 10);
        bottomOut0.setBaseDamage(10);
        bottomOut0.setBaseHeal(0);
        bottomOut0.setHealProb(0.2f);

        GeneratingLine topIn0 = new GeneratingLine(stage);
        topIn0.setInterval(3f, 5f);
        topIn0.setY(400, 440);
        topIn0.setBlockerSize(40, 60, 10, 15);
        topIn0.setBaseDamage(20);
        topIn0.setBaseHeal(30);
        topIn0.setHealProb(0.5f);

        GeneratingLine bottomIn0 = new GeneratingLine(stage);
        bottomIn0.setInterval(3f, 5f);
        bottomIn0.setY(280, 320);
        bottomIn0.setBlockerSize(40, 60, 10, 15);
        bottomIn0.setBaseDamage(20);
        bottomIn0.setBaseHeal(30);
        bottomIn0.setHealProb(0.5f);

        lines0 = new GeneratingLine[] { topOut0, topIn0, bottomIn0, bottomOut0 };

        bully21 = new BullyActor(10, 0, 0.5f);
        bully22 = new BullyActor(10, 1, 0.5f);
        bully23 = new BullyActor(10, 2, 1);
        line2 = new GeneratingLine(stage);
        line2.setInterval(0.5f, 1f);
        line2.setY(180, 540);
        line2.setBlockerSize(20, 30, 5, 10);
        line2.setBaseDamage(0);
        line2.setBaseHeal(10);
        line2.setHealProb(1f);

        line3 = new GeneratingLine(stage);
        line3.setInterval(0.05f, 0.5f);
        line3.setY(0, 720);
        line3.setBlockerSize(10, 40, 2, 15);
        line3.setBaseDamage(10);
        line3.setBaseHeal(10);
        line3.setHealProb(0.5f);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        super.start();
        stage.getWave().setHasPartner(true);
        stage.addActor(bully21);
        stage.addActor(bully22);
        stage.addActor(bully23);
    }

    @Override
    public void end() {
        super.end();
        stage.getWave().setHasPartner(false);
        bully21.remove();
        bully22.remove();
        bully23.remove();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        subPhase = Math.max(subPhase,
                (int)(Generating.unlerp(Ecg.state.hp, initHp+50, Ecg.state.MAX_HP)*subPhaseCount));

        if(subPhase == 0) {
            for (GeneratingLine line : lines0) {
                doLine(line, deltaTime);
            }
        }
        if(subPhase == 1) {
            fromLastTime1 += deltaTime;
            float intervalMin = 0.5f;
            float intervalMax = 2f;
            float prob = Generating.unlerp(fromLastTime1, intervalMin, intervalMax);
            if(MathUtils.random() < prob) {
                fromLastTime1 = 0;
                float blockerX = stage.getScrollX()+stage.getWidth();
                float blockerY = MathUtils.random(0, 100);
                float hpChange;
                float blockerWidth;
                float blockerHeight;
                if(MathUtils.random() < 0.25) {
                    hpChange = 20;
                    blockerWidth = MathUtils.random(20, 30);
                    blockerHeight = blockerWidth;
                }
                else {
                    hpChange = -20;
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
        if(subPhase == 2) {
            bully21.showUp();
            bully22.showUp();
            bully23.showUp();
            doLine(line2, deltaTime);
        }
        else {
            if(bully21.hasParent()) {
                bully21.leave();
                bully22.leave();
                bully23.leave();
            }
        }
        if(subPhase == 3) {
            this.bumperRange = 0;
            this.waveSpeed = new Vector2(400, 800);
            this.hpRegen = 12.5f;
            doLine(line3, deltaTime);
        }
    }

    private void doLine(GeneratingLine line, float deltaTime) {
        line.update(deltaTime);
        BlockerActor blocker = line.tryToGenerate();
        if (blocker != null)
            stage.addActor(blocker);
    }
}
