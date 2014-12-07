package cc.raintomorrow;

import com.badlogic.gdx.audio.Sound;

public class Sounds {
    static public Sound beat;
    static public Sound bonus;
    static public Sound die;
    static public Sound hit1;
    static public Sound hit2;
    static public Sound shoot;
    static public Sound warn;
    static public Sound type;

    static public void loadSounds() {
        beat = Ecg.app.getAsset("sound/beat.wav");
        die = Ecg.app.getAsset("sound/die.wav");
        hit1 = Ecg.app.getAsset("sound/hit-1.wav");
        hit2 = Ecg.app.getAsset("sound/hit-2.wav");
        shoot = Ecg.app.getAsset("sound/shoot.wav");
        warn = Ecg.app.getAsset("sound/warn.wav");
        bonus = Ecg.app.getAsset("sound/bonus.wav");
        type = Ecg.app.getAsset("sound/type.wav");
    }
}
