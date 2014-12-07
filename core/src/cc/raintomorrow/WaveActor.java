package cc.raintomorrow;

import cc.raintomorrow.graphics.Direction;
import cc.raintomorrow.utils.ColorUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Segment;

public class WaveActor extends EcgActor {
    static private Vector2 DEFAULT_SPEED = new Vector2(200, 400);
    static private float DEFAULT_CENTER_WIDTH = 3;
    static private float DEFAULT_NEON_WIDTH = 30;
    static private Color DEFAULT_NEON_COLOR = ColorUtils.rgba256(105, 220, 239, 255);

    private Vector2 position;
    private Vector2 lastPosition;
    private Vector2 speed;
    private boolean horizontal;

    private float centerWidth = DEFAULT_CENTER_WIDTH;
    private float neonWidth = DEFAULT_NEON_WIDTH;

    private Color centerColor = Color.WHITE;
    private Color neonColor = DEFAULT_NEON_COLOR;

    private NeonTexturer neonTexturer;

    public WaveActor() {
        this.speed = new Vector2(DEFAULT_SPEED.x, 0);
    }

    @Override
    public void onAdded() {
        int stageWidth = (int)getStage().getWidth();
        int stageHeight = (int)getStage().getHeight();
        this.position = new Vector2(0, stageHeight / 2);
        this.lastPosition = position.cpy();
        this.neonTexturer = new NeonTexturer(stageWidth, stageHeight);
        this.neonTexturer.setParameters(centerWidth, centerColor, neonWidth, neonColor);
    }

    @Override
    public void onRemoved() {
        neonTexturer.dispose();
    }

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);

        if(position.y < 0)
            goUp();
        if(position.y > getStage().getHeight())
            goDown();

        lastPosition.set(position);
        Vector2 tempSpeed = speed.cpy();
        if(horizontal) tempSpeed.y = 0;
        position.add(tempSpeed.scl(deltaTime));
        neonTexturer.drawNewSegment(position, lastPosition);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Texture texture = neonTexturer.getTexture();
        Vector2 position = neonTexturer.getTexturePosition();
        batch.draw(texture, scrolledX(position.x), position.y);
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public void setSpeedQuantity(Vector2 speed) {
        float oldSpeedY = this.speed.y;
        float dir = oldSpeedY == 0 ? 0 : oldSpeedY / Math.abs(oldSpeedY);
        this.speed.set(speed);
        if(dir == 0) setHorizontal(true);
        else this.speed.y *= dir;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public void changeDirection() {
        speed.set(speed.x, -speed.y);
    }

    public void goUp() {
        setHorizontal(false);
        speed.set(speed.x, Math.abs(speed.y));
    }

    public void goDown() {
        setHorizontal(false);
        speed.set(speed.x, -Math.abs(speed.y));
    }

    public Direction getDirection() {
        if(horizontal) return Direction.RIGHT;
        return speed.y > 0 ? Direction.UP : Direction.DOWN;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public void clearTexture() {
        neonTexturer.clearPixmap();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public boolean isColliding(Rectangle rect) {
        Vector2 center = new Vector2();
        rect.getCenter(center);
        float w = rect.getWidth();
        float h = rect.getHeight();
        if(center.dst2(position) > w * w / 4 + h * h / 4)
            return false;

        Vector2 delta = position.cpy().sub(lastPosition).scl(0.75f);
        Vector2 normal = new Vector2(-delta.y, delta.x);
        Vector2 nextPosition = position.cpy().add(delta);

        return intersectSegmentRectangle(position.cpy().sub(delta), nextPosition.cpy().add(delta), rect) ||
                intersectSegmentRectangle(position.cpy().add(normal), nextPosition.cpy().add(normal), rect) ||
                intersectSegmentRectangle(position.cpy().sub(normal), nextPosition.cpy().sub(normal), rect);
    }

    private boolean intersectSegmentRectangle(Vector2 u, Vector2 v, Rectangle rectangle) {
        if(intersectPointRectangle(u, rectangle) || intersectPointRectangle(v, rectangle))
            return true;
        Vector2 p1 = new Vector2(rectangle.x, rectangle.y);
        Vector2 p2 = new Vector2(rectangle.x+rectangle.width, rectangle.y);
        Vector2 p3 = new Vector2(rectangle.x+rectangle.width, rectangle.y+rectangle.height);
        Vector2 p4 = new Vector2(rectangle.x, rectangle.y+rectangle.height);
        return Intersector.intersectSegments(u, v, p1, p2, null) ||
                Intersector.intersectSegments(u, v, p2, p3, null) ||
                Intersector.intersectSegments(u, v, p3, p4, null) ||
                Intersector.intersectSegments(u, v, p4, p1, null);
    }

    private boolean intersectPointRectangle(Vector2 p, Rectangle rectangle) {
        return p.x >= rectangle.x && p.x <= rectangle.x + rectangle.width &&
                p.y >= rectangle.y && p.y <= rectangle.y + rectangle.height;
    }

}
