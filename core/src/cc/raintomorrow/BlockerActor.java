package cc.raintomorrow;

import cc.raintomorrow.math.WeightedRandomChooser;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class BlockerActor extends EcgActor {
    private NinePatch blockerPatch;
    private float hpChange;
    private boolean active = true;
    private Vector2 speed = new Vector2();

    public BlockerActor(float hpChange, float x, float y, float width, float height) {
        this.hpChange = hpChange;

        TextureAtlas atlas = Ecg.app.getAsset("img/pack.atlas");
        if(hpChange > 0)
            this.blockerPatch = atlas.createPatch("blocker-heal");
        else
            this.blockerPatch = atlas.createPatch("blocker-damage");

        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    public void setSpeed(Vector2 speed) {
        this.speed.set(speed);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float drawWidth = getWidth() + (blockerPatch.getLeftWidth() + blockerPatch.getRightWidth());
        float drawHeight = getHeight() + (blockerPatch.getTopHeight() + blockerPatch.getBottomHeight());
        blockerPatch.draw(batch, scrolledX(getX())-drawWidth/2, getY()-drawHeight/2,
                drawWidth, drawHeight);
        if(scrolledX(getX())+drawWidth < 0) {
            remove();
        }
        //batch.draw(Ecg.app.getAsset("img/cutscene-background.png"), )
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        WaveActor wave = getStage().wave;
        if(wave.isColliding(toRectangle()) && active) {
            gotHit();
        }
        setX(getX()+speed.x*delta);
        setY(getY() + speed.y * delta);
    }

    private void gotHit() {
        active = false;
        if(hpChange < 0) {
            getStage().blink(Color.RED, 0.25f);
            WeightedRandomChooser<Sound> chooser = new WeightedRandomChooser<Sound>();
            chooser.addItem(Sounds.hit1, 1);
            chooser.addItem(Sounds.hit2, 2);
            chooser.choose().play();

            getStage().addHpUpdater(new HpUpdater(hpChange/0.15f, 0.15f));
            remove();
        }
        else {
            getStage().addHpUpdater(new HpUpdater(hpChange/0.15f, 0.15f));
            remove();
        }
    }

    private Rectangle toRectangle() {
        return new Rectangle(getX()-getWidth()/2, getY()-getHeight()/2, getWidth(), getHeight());
    }
}
