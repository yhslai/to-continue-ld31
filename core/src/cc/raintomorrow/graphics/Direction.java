package cc.raintomorrow.graphics;

public enum Direction {
    UP, DOWN, RIGHT, LEFT;

    public Direction opposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case RIGHT: return LEFT;
            case LEFT: return RIGHT;
        }
        return null;
    }
}
