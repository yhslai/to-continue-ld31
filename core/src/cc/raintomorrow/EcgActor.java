package cc.raintomorrow;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class EcgActor extends Actor {
    @Override
    public EcgStage getStage() {
        return (EcgStage)super.getStage();
    }

    public void onAdded() {

    }

    public void onRemoved() {

    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if(stage == null)
            onRemoved();
    }

    protected float scrolledX(float x) {
        return x - getStage().getScrollX();
    }
}
