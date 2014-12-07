package cc.raintomorrow.cutscene;

import cc.raintomorrow.Ecg;
import cc.raintomorrow.EcgStage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class CutsceneManager {
    private EcgStage stage;
    private Cutscene [] cutscenes;
    private int currentCutsceneIndex;
    private Texture background;

    public CutsceneManager(EcgStage stage, Cutscene... cutscenes) {
        this.stage = stage;
        this.cutscenes = cutscenes;
        for(Cutscene cutscene : cutscenes)
            cutscene.setStage(stage);
        this.background = Ecg.app.getAsset("img/cutscene-background.png");
    }

    public void pressAction() {
        Cutscene currentCutscene = cutscenes[currentCutsceneIndex];
        currentCutscene.pressAction();
    }

    public void update(float deltaTime) {
        if(!isEnded()) {
            Cutscene currentCutscene = cutscenes[currentCutsceneIndex];
            currentCutscene.update(deltaTime);
            if(currentCutscene.isEnded())
                currentCutsceneIndex++;
        }
    }

    public void draw(Batch batch) {
        if(!isEnded()) {
            batch.draw(background, 0, 0, stage.getWidth(), stage.getHeight());
            Cutscene currentCutscene = cutscenes[currentCutsceneIndex];
            currentCutscene.draw(batch);
        }
    }

    public boolean isEnded() {
        return currentCutsceneIndex >= cutscenes.length;
    }
}
