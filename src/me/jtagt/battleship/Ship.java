package me.jtagt.battleship;

public class Ship {
    private final Battleship.Ships type;
    private final Vec2 size;
    private Vec2 position;

    public Ship(Battleship.Ships type, Vec2 position) {
        this.type = type;
        this.size = new Vec2(type.getSize(), 1);
        this.position = position;
    }

    public Battleship.Ships getType() {
        return type;
    }

    public Vec2 getSize() {
        return this.size;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public Vec2 getPosition() {
        return position;
    }

    public Vec2[] calculateBoundingBox(Vec2 position) {
        Vec2 sizeHalved = new Vec2(this.size.getX() / 2, 0);

        return new Vec2[]{position.sub(sizeHalved), position.add(sizeHalved)};
    }

    public Vec2[] calculateBoundingBox() {
        Vec2 size = new Vec2(this.getSize().getX() - 1, 0);

        return new Vec2[]{this.getPosition(), this.position.add(size)};
    }
}
