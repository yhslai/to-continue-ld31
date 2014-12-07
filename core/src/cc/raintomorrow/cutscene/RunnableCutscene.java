package cc.raintomorrow.cutscene;

import cc.raintomorrow.EcgStage;
import com.badlogic.gdx.graphics.g2d.Batch;

public class RunnableCutscene extends Cutscene {
    private boolean ended;
    private Runnable runnable;

    public RunnableCutscene(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public boolean isEnded() {
        return ended;
    }

    @Override
    public void pressAction() {

    }

    @Override
    public void update(float deltaTime) {
        runnable.run();
        ended = true;
    }

    @Override
    public void draw(Batch batch) {

    }
}
