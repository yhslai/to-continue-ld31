package cc.raintomorrow;

public class HpUpdater {
    public float changePerSecond;
    public float leftDuration;
    public float leftChange;
    public boolean infinite;

    public HpUpdater(float changePerSecond) {
        this.infinite = true;
        this.changePerSecond = changePerSecond;
        this.leftChange = Float.MAX_VALUE;
    }

    public HpUpdater(float changePerSecond, float duration) {
        this.leftDuration = duration;
        this.changePerSecond = changePerSecond;
        this.leftChange = duration * changePerSecond;
    }

    public float update(float delatTime) {
        float change = Math.min(leftChange, changePerSecond*delatTime);
        leftDuration -= delatTime;
        leftChange -= change;
        return change;
    }

    public boolean isEnded() {
        return !infinite && leftDuration <= 0;
    }
}

