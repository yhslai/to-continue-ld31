package cc.raintomorrow.cutscene;

import cc.raintomorrow.EcgStage;
import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class Cutscene {
    protected EcgStage stage;

    public void setStage(EcgStage stage) {
        this.stage = stage;
    }

    public abstract boolean isEnded();
    public abstract void pressAction();
    public abstract void update(float deltaTime);
    public abstract void draw(Batch batch);
}
