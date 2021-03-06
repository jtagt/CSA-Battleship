package me.jtagt.battleship;

public class Vec2 {
    private final int x;
    private final int y;

    public Vec2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vec2 sub(Vec2 vec2) {
        return new Vec2(this.x - vec2.getX(), this.y - vec2.getY());
    }

    public Vec2 add(Vec2 vec2) {
        return new Vec2(this.x + vec2.getX(), this.y + vec2.getY());
    }

    @Override
    public String toString() {
        return "Vec2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
