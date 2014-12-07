package cc.raintomorrow;

import cc.raintomorrow.graphics.Animator;
import cc.raintomorrow.graphics.AnimatorBuilder;
import cc.raintomorrow.graphics.Direction;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Iterator;

public class BumperActor extends EcgActor {
    private float hpRegen = 100;

    private boolean active;
    private boolean bumping;
    private boolean justBumped;
    private Direction previousBumpDirection = Direction.LEFT;

    private Animator inactiveAnimator;
    private Animator activeAnimator;
    private Animator bumpingAnimator;
    private Texture bonusTexture;

    private WaveActor wave;
    private BumperActor another;

    private ArrayList<Vector2> bonusStars = new ArrayList<Vector2>();

    public BumperActor(float y, WaveActor wave) {
        this.wave = wave;
        setY(y);

        Texture texture = Ecg.app.getAsset("img/bumper.png");
        AnimatorBuilder animBuilder = new AnimatorBuilder(texture, 6, 1);
        this.inactiveAnimator = animBuilder.buildAnimator(0.1f, 0, 1, Animation.PlayMode.LOOP);
        this.activeAnimator = animBuilder.buildAnimator(0.1f, 1, 2, Animation.PlayMode.LOOP);
        this.bumpingAnimator = animBuilder.buildAnimator(0.05f, 2, 6, Animation.PlayMode.NORMAL);
        this.bonusTexture = Ecg.app.getAsset("img/bonus.png");
    }

    public void setAnother(BumperActor another) {
        this.another = another;
    }

    public void activate() {
        active = true;
    }

    public void inactivate() {
        active = false;
    }

    public float getHpRegen() {
        return hpRegen;
    }

    public void setHpRegen(float hpRegen) {
        this.hpRegen = hpRegen;
    }

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);

        if(wave.isColliding(toRectangle()) && active) {
            bump();
        }

        if(justBumped && wave.getDirection().opposite() == previousBumpDirection) {
            bonus();
        }

        if(bumping) {
            bumpingAnimator.update(deltaTime);
            if(bumpingAnimator.isEnded()) {
                bumping = false;
            }
        }
    }

    private Rectangle toRectangle() {
        //noinspection SuspiciousNameCombination
        return new Rectangle(0, getY(), 100000000, 1);
    }

    private void bump() {
        active = false;
        another.active = true;
        bumping = true;
        justBumped = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if(justBumped) {
                    Sounds.beat.play();
                    justBumped = false;
                }
            }
        }, 0.05f);
        previousBumpDirection = wave.getDirection();
        bumpingAnimator.restart();

        getStage().addHpUpdater(new HpUpdater(getHpRegen() / 0.2f, 0.2f));
    }

    private void bonus() {
        justBumped = false;
        Sounds.bonus.play();
        Sounds.beat.stop();
        bonusStars.add(wave.getPosition().cpy());

        getStage().addHpUpdater(new HpUpdater(getHpRegen() / 0.4f, 0.4f));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float drawY = getY() - 20 / 2;

        if(bumping) {
            batch.draw(bumpingAnimator.getKeyFrame(), 0, drawY);
        }
        else if(active) {
            batch.draw(activeAnimator.getKeyFrame(), 0, drawY);
        }
        else {
            batch.draw(inactiveAnimator.getKeyFrame(), 0, drawY);
        }

        Iterator<Vector2> bonusIt = bonusStars.iterator();
        while(bonusIt.hasNext()) {
            Vector2 bonusPos = bonusIt.next();
            float x = scrolledX(bonusPos.x);
            float w = bonusTexture.getWidth();
            float h = bonusTexture.getHeight();
            batch.draw(bonusTexture, x-w/2-4, getY()-w/2);

            if(x+w < 0)
                bonusIt.remove();
        }
    }

}
