package cc.raintomorrow;

import cc.raintomorrow.graphics.Direction;
import cc.raintomorrow.phase.Generating;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class BullyActor extends EcgActor {
    static private float INIT_X = 840;
    static private float FAR_X = 1080;

    private float delayFactor;
    private float fireFactor;
    private float damage;
    private boolean showed;

    private Texture bullyTexture;

    private Vector2 speed = new Vector2();

    private float fromLastTime;
    private float firingTime;
    private int leftBullets;

    private Action currentAction;

    public BullyActor(float damage, float delayFactor, float fireFactor) {
        this.delayFactor = delayFactor;
        this.fireFactor = fireFactor;
        this.damage = damage;
        this.bullyTexture = Ecg.app.getAsset("img/bully.png");
        setPosition(FAR_X, 40);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(bullyTexture,
                getX()-bullyTexture.getWidth()/2, getY()-bullyTexture.getHeight()/2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(!showed) return;

        float deltaY = getStage().getWave().getPosition().y - getY();
        float deltaX = INIT_X - getX();
        if(MathUtils.random() < Generating.unlerp(Math.abs(deltaX), 20, 60))
            speed.x = 60 * deltaX / Math.abs(deltaX);
        if(Math.abs(deltaY) >= 75 * delayFactor)
            speed.y = 150 * deltaY / Math.abs(deltaY);

        setPosition(getX()+speed.x*delta,getY()+speed.y*delta);

        firingTime -= delta;
        if(firingTime <= 0 && leftBullets > 0) {
            BlockerActor bullet = new BlockerActor(-damage,
                    getX()+getStage().getScrollX()-15, getY(),
                    30, 10);
            bullet.setSpeed(new Vector2(-100, 0));
            getStage().addActor(bullet);

            leftBullets--;
            firingTime = 0.1f;
        }
        else {
            fromLastTime += delta;
            float prob = Generating.unlerp(fromLastTime * fireFactor, 1.5f, 3);
            if (MathUtils.random() < prob) {
                fromLastTime = 0;
                if(leftBullets <= 0) {
                    firingTime = 0.1f;
                    leftBullets = 3;
                }
            }
        }
    }

    public void showUp() {
        if(!showed) {
            showed = true;
            removeAction(currentAction);
            currentAction = Actions.moveTo(INIT_X, MathUtils.random(180, 540), 1f, Interpolation.pow3Out);
            addAction(currentAction);
        }
    }

    public void leave() {
        if(showed) {
            showed = false;
            removeAction(currentAction);
            currentAction = Actions.moveTo(FAR_X, MathUtils.random(180, 540), 1f, Interpolation.pow3Out);
            addAction(currentAction);
        }
    }
}
