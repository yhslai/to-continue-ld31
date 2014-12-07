package cc.raintomorrow.utils;

import com.badlogic.gdx.graphics.Color;

public class ColorUtils {
    static public Color TRANSPARENT = new Color(0, 0, 0, 0);

    static public Color rgb256(int r, int g, int b) {
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, 1);
    }

    static public Color rgba256(int r, int g, int b, int a) {
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    static public float intensity(Color color) {
        return color.r * 0.2126f + color.g * 0.7152f + color.b * 0.0722f;
    }
}
