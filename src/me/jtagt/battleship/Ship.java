package me.jtagt.battleship;

public class Ship {
    private final JTIS_Battleship.Ships type;
    private final Vec2 size;
    private final Vec2 position;
    private final boolean isVertical;

    public Ship(JTIS_Battleship.Ships type, Vec2 position, boolean isVertical) {
        this.type = type;
        this.isVertical = isVertical;

        if (isVertical) {
            this.size = new Vec2(1, type.getSize());
        } else {
            this.size = new Vec2(type.getSize(), 1);
        }

        this.position = position;
    }

    public JTIS_Battleship.Ships getType() {
        return type;
    }

    public Vec2 getSize() {
        return this.size;
    }

    public Vec2 getPosition() {
        return position;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public Vec2[] calculateBoundingBox() {
        Vec2 size;

        if (this.isVertical) {
            size = new Vec2(0, this.getSize().getY() - 1);
        } else {
            size = new Vec2(this.getSize().getX() - 1, 0);
        }

        return new Vec2[]{this.getPosition(), this.position.add(size)};
    }
}
