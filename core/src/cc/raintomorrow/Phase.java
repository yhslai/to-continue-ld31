package cc.raintomorrow;

import cc.raintomorrow.Ecg;
import cc.raintomorrow.EcgStage;
import com.badlogic.gdx.math.Vector2;

public abstract class Phase {
    private boolean running;
    protected EcgStage stage;
    protected Vector2 waveSpeed = new Vector2(200, 400);
    protected float initHp = 300;

    protected Phase(EcgStage stage) {
        this.stage = stage;
    }

    public void init() {
        stage.wave.setSpeedQuantity(waveSpeed);
        Ecg.state.hp = initHp;
        running = false;
    }
    public void start() {
        running = true;
    }
    public boolean isRunning() {
        return running;
    }
    public void end() {
        running = false;
    }
    public abstract void update(float deltaTime);
}
