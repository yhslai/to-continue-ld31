package cc.raintomorrow;

import com.badlogic.gdx.math.MathUtils;

public class EcgState {
    public final float MAX_HP = 1000;
    public float hp;

    public void changeHpBy(float amount) {
        hp += amount;
        hp = MathUtils.clamp(hp, 0, MAX_HP);
    }
}
