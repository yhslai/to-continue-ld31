package cc.raintomorrow;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HpBarActor extends EcgActor {
    private NinePatch superHpPatch;
    private NinePatch highHpPatch;
    private NinePatch normalHpPatch;
    private NinePatch lowHpPatch;
    private TextureRegion superLowHpRegion;

    public HpBarActor() {
        TextureAtlas atlas = Ecg.app.getAsset("img/pack.atlas");
        superHpPatch = atlas.createPatch("hp-bar-super");
        highHpPatch = atlas.createPatch("hp-bar");
        normalHpPatch = atlas.createPatch("hp-bar-normal");
        lowHpPatch = atlas.createPatch("hp-bar-low");
        superLowHpRegion = atlas.findRegion("hp-bar-super-low");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float originalBarWidth = highHpPatch.getTotalWidth();
        float stageWidth = getStage().getWidth();
        float barX = (stageWidth-originalBarWidth)/2;
        float barY = 671;
        float barWidth = Ecg.state.hp / Ecg.state.MAX_HP * (originalBarWidth + barX + 35);
        float barHeight = highHpPatch.getTotalHeight() - 10;
        if(barWidth > stageWidth * 4 / 5) {
            superHpPatch.draw(batch, barX, barY, barWidth, barHeight);
        }
        else if(barWidth > stageWidth * 2 / 3) {
            highHpPatch.draw(batch, barX, barY, barWidth, barHeight);
        }
        else if(barWidth > 200) {
            normalHpPatch.draw(batch, barX, barY, barWidth, barHeight);
        }
        else if(barWidth > lowHpPatch.getLeftWidth() + lowHpPatch.getRightWidth()) {
            lowHpPatch.draw(batch, barX, barY, barWidth, barHeight);
        }
        else {
            barWidth -= 10;
            TextureRegion region = new TextureRegion(superLowHpRegion, 0, 0, (int)barWidth, (int)lowHpPatch.getTotalHeight());
            batch.draw(region, barX, barY, barWidth, barHeight);
        }
    }
}
