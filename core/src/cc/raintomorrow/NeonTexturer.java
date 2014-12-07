package cc.raintomorrow;

import cc.raintomorrow.utils.ColorUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

// Remember to call dispose()!
public class NeonTexturer {
    private Buffer buffers[];
    private Texture cachedTexture;
    private Vector2 cachedTexturePosition;

    private float centerWidth;
    private float neonWidth;

    private Color centerColor;
    private Color neonColor;

    private int stageHeight;

    public NeonTexturer(int stageWidth, int stageHeight) {
        this.stageHeight = stageHeight;

        this.buffers = new Buffer[] {
                new Buffer(
                        new Pixmap(stageWidth, stageHeight, Pixmap.Format.RGBA8888),
                        new Vector2(0, 0)
                ),
                new Buffer(
                        new Pixmap(stageWidth, stageHeight, Pixmap.Format.RGBA8888),
                        new Vector2(stageWidth, 0)
                )
        };
        this.cachedTexture = new Texture(stageWidth * 2, stageHeight, Pixmap.Format.RGBA8888);
        this.cachedTexturePosition = new Vector2(0, 0);
    }

    public void dispose() {
        cachedTexture.dispose();
        for(Buffer buffer : buffers)
            buffer.pixmap.dispose();
        cachedTexture = null;
        buffers = null;
    }

    public void setParameters(float centerWidth, Color centerColor, float neonWidth, Color neonColor) {
        this.centerWidth = centerWidth;
        this.centerColor = centerColor;
        this.neonWidth = neonWidth;
        this.neonColor = neonColor;
        for(Buffer buffer : buffers)
            buffer.clearPixmap();
    }

    public void drawNewSegment(Vector2 u, Vector2 v) {
        drawSegmentOnBuffer(u, v);
        combineBuffers();
    }

    private void drawSegmentOnBuffer(Vector2 u, Vector2 v) {
        Pixmap.setBlending(Pixmap.Blending.None);

        float length = v.dst2(u);
        int sampleCount = Math.min(20, (int)Math.ceil(length) / 2);
        Vector2 pos = new Vector2();
        for(int i = 0; i < sampleCount; i++) {
            pos.set(u);
            pos.lerp(v, (float) i / sampleCount);

            Color color = new Color();
            Color transparent = neonColor.cpy();
            transparent.a = 0;
            for(float r = 0.5f; r * 2 <= neonWidth; r++) {
                color.set(neonColor);
                color.lerp(transparent, Interpolation.pow2Out.apply(r * 2 / neonWidth));
                for(float x = pos.x - r; x <= pos.x + r; x++)
                    for(float y = pos.y - r; y <= pos.y + r; y++) {
                        if(y < 0)
                            break;
                        float dst2 = pos.dst2(x, y);
                        if (dst2 <= r * r && (r < 2 || dst2 >= (r-1) * (r-1))) {
                            drawPixelOnBuffer((int)x, (int)y, color);
                        }
                    }
            }
            for(float r = 0.5f; r * 2 <= centerWidth; r++) {
                for(float x = pos.x - r; x <= pos.x + r; x++)
                    for(float y = pos.y - r; y <= pos.y + r; y++) {
                        if(y < 0)
                            break;
                        float dst2 = pos.dst2(x, y);
                        if (dst2 <= r * r && (r < 2 || dst2 >= (r-1) * (r-1))) {
                            drawPixelOnBuffer((int)x, (int)y, centerColor);
                        }
                    }
            }
        }

        Pixmap.setBlending(Pixmap.Blending.SourceOver);
    }

    private void drawPixelOnBuffer(int x, int y, Color color) {
        if(x <= 0 || y <= 0 || y >= stageHeight)
            return;

        Buffer toDraw = null;
        for(Buffer buffer : buffers) {
            if(buffer.contains(x, y)) {
                toDraw = buffer;
                break;
            }
        }
        if(toDraw == null) {
            Buffer leftMost = buffers[0];
            Buffer rightMost = buffers[0];
            for(Buffer buffer : buffers) {
                if(buffer.position.x < leftMost.position.x) leftMost = buffer;
                if(buffer.position.x > rightMost.position.x) rightMost = buffer;
            }
            leftMost.position.x = rightMost.position.x + rightMost.pixmap.getWidth();
            leftMost.clearPixmap();
            toDraw = leftMost;
        }
        toDraw.drawPixelIfBrighter(x, y, color);
    }


    private void combineBuffers() {
        cachedTexturePosition.set(Float.MAX_VALUE, Float.MAX_VALUE);
        for(Buffer buffer : buffers) {
            cachedTexturePosition.x = Math.min(cachedTexturePosition.x, buffer.position.x);
            cachedTexturePosition.y = Math.min(cachedTexturePosition.y, buffer.position.y);
        }
        for(Buffer buffer : buffers) {
            cachedTexture.draw(buffer.pixmap,
                    (int) (buffer.position.x - cachedTexturePosition.x),
                    (int) (buffer.position.y - cachedTexturePosition.y));
        }
    }

    public Texture getTexture() {
        return cachedTexture;
    }

    public Vector2 getTexturePosition() {
        return cachedTexturePosition;
    }

    public void clearPixmap() {
        for(Buffer buffer : buffers)
            buffer.clearPixmap();
        combineBuffers();
    }

    class Buffer {
        public Pixmap pixmap;
        public Vector2 position;

        Buffer(Pixmap pixmap, Vector2 position) {
            this.pixmap = pixmap;
            this.position = position;
        }

        public boolean contains(int x, int y) {
            Vector2 position = positionOnPixmap(x, y);
            return position.x >= 0 && position.x < pixmap.getWidth() &&
                    position.y >= 0 && position.y < pixmap.getHeight();
        }

        public void drawPixelIfBrighter(int x, int y, Color color) {
            Vector2 position = positionOnPixmap(x, y);
            Color oldColor = new Color(pixmap.getPixel((int)position.x, (int)position.y));
            if(color.a >= oldColor.a || ColorUtils.intensity(color) > ColorUtils.intensity(oldColor)) {
                pixmap.drawPixel((int) position.x, (int) position.y, Color.rgba8888(color));
            }
        }

        public Vector2 positionOnPixmap(int x, int y) {
            return new Vector2(x - position.x, pixmap.getHeight() - y - position.y);
        }

        public void clearPixmap() {
            pixmap.setColor(ColorUtils.TRANSPARENT);
            pixmap.fill();
        }
    }
}
