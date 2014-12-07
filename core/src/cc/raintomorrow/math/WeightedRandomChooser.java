package cc.raintomorrow.math;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class WeightedRandomChooser<T> {
    private ArrayList<T> items = new ArrayList<T>();
    private ArrayList<Float> weights = new ArrayList<Float>();
    private float totalWeight = 0;

    public void addItem(T item, float weight) {
        items.add(item);
        weights.add(weight);
        totalWeight += weight;
    }

    public T choose() {
        float random = MathUtils.random(totalWeight);
        for(int i = 0; i < weights.size(); i++) {
            random -= weights.get(i);
            if(random <= 0)
                return items.get(i);
        }
        return items.get(items.size()-1);
    }
}
