package cc.raintomorrow;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class BonusActor extends EcgActor {
    private Texture bonusTexture;

    public BonusActor() {
        this.bonusTexture = Ecg.app.getAsset("img/bonus.png");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float w = bonusTexture.getWidth();
        float h = bonusTexture.getHeight();
        batch.draw(bonusTexture, getX()-w/2-4, getY()-w/2);

        if(getX()+w < 0)
            this.remove();
    }
}
