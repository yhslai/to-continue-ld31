package cc.raintomorrow.phase;

import cc.raintomorrow.Ecg;
import cc.raintomorrow.EcgStage;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public abstract class Phase {
    private boolean running;
    protected EcgStage stage;
    protected Vector2 waveSpeed = new Vector2(200, 400);
    protected float initHp = 300;
    protected float hpRegen = 100;
    protected float hpConsumption = EcgStage.DEFAULT_HP_CONSUMPTION;
    protected float bumperRange = EcgStage.DEFAULT_BUMPER_RANGE;

    protected Phase(EcgStage stage) {
        this.stage = stage;
    }

    public void init() {
        Ecg.state.hp = initHp;
        stage.setHpConsumption(hpConsumption);
        running = false;
    }

    public void start() {
        running = true;
    }

    public void update(float deltaTime) {
        updateHpRegen();
        updateWaveSpeed();
        updateBumperRange();
    }

    protected void updateWaveSpeed() {
        stage.getWave().setSpeedQuantity(waveSpeed);
    }

    protected void updateBumperRange() {
        float topBumperY = stage.getHeight()/2+bumperRange/2;
        float bottomBumperY = stage.getHeight()/2-bumperRange/2;
        stage.getTopBumper().clearActions();
        stage.getBottomBumper().clearActions();
        stage.getTopBumper().addAction(Actions.moveTo(0, topBumperY, 0.5f));
        stage.getBottomBumper().addAction(Actions.moveTo(0, bottomBumperY, 0.5f));
    }

    protected void updateHpRegen() {
        stage.getTopBumper().setHpRegen(hpRegen);
        stage.getBottomBumper().setHpRegen(hpRegen);
    }

    public boolean isRunning() {
        return running;
    }

    public void end() {
        running = false;
    }
}
