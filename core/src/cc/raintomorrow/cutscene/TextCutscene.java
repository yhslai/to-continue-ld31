package cc.raintomorrow.cutscene;

import cc.raintomorrow.Ecg;
import cc.raintomorrow.EcgStage;
import cc.raintomorrow.Sounds;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TextCutscene extends Cutscene {
    private boolean ended;
    private String mainText;
    private Color color;
    private String subText;
    private float charPerSec;
    private float stateTime;
    private int previousShowedLength;

    private BitmapFont mainFont;
    private BitmapFont subFont;

    public TextCutscene(String mainText, Color color) {
        this.mainText = mainText;
        this.charPerSec = Math.min(10, mainText.length()/1f);
        this.mainFont = Ecg.app.getAsset("font/believer.ttf.42");
        this.mainFont.setColor(color);
    }

    public TextCutscene(String mainText) {
        this(mainText, Color.WHITE);
    }

    public TextCutscene(String mainText, String subText) {
        this(mainText, Color.WHITE);
        this.subText = subText;
        this.subFont = Ecg.app.getAsset("font/believer.ttf.32");
    }

    @Override
    public boolean isEnded() {
        return ended;
    }

    @Override
    public void pressAction() {
        if(mainTextEnded())
            ended = true;
        else
            stateTime = mainTextDuration();
    }

    @Override
    public void update(float deltaTime) {
        stateTime += deltaTime;
    }

    @Override
    public void draw(Batch batch) {
        String mainTextToShow = mainTextToShow();
        String [] lines = mainTextToShow.split("\n");
        for(int i = 0; i < lines.length; i++) {
            BitmapFont.TextBounds bounds = mainFont.getBounds(lines[i]);
            mainFont.draw(batch, lines[i],
                    (stage.getWidth()-bounds.width)/2,
                    490 - i * 64);
        }
        if(mainTextToShow.length() > previousShowedLength)
            Sounds.type.play();
        previousShowedLength = mainTextToShow.length();

        if(mainTextEnded() && subText != null) {
            BitmapFont.TextBounds bounds = subFont.getBounds(subText);
            subFont.draw(batch, subText,
                    (stage.getWidth() - bounds.width) / 2, 115);
        }
    }

    private String mainTextToShow() {
        float duration = stateTime;
        float oneCharDuration = 1 / charPerSec;
        for(int i = 0; i < mainText.length(); i++) {
            if(mainText.charAt(i) == ".".charAt(0)) duration -= oneCharDuration * 3;
            else if(mainText.charAt(i) == "\"".charAt(0)) duration += 0;
            else if(mainText.charAt(i) == "\n".charAt(0)) duration -= oneCharDuration * 5;
            else duration -= oneCharDuration;
            if(duration <= 0)
                return mainText.substring(0, i+1);
        }
        return mainText;
    }

    private boolean mainTextEnded() {
        return stateTime >= mainTextDuration();
    }

    private float mainTextDuration() {
        float duration = 0;
        float oneCharDuration = 1 / charPerSec;
        for(int i = 0; i < mainText.length(); i++) {
            if(mainText.charAt(i) == ".".charAt(0)) duration += oneCharDuration * 3;
            else if(mainText.charAt(i) == "\"".charAt(0)) duration += 0;
            else if(mainText.charAt(i) == "\n".charAt(0)) duration += oneCharDuration * 5;
            else duration += oneCharDuration;
        }
        return duration;
    }
}
