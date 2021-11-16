public class Ship {
    private final Vec2 size;
    private Vec2 position;

    public Ship(Vec2 size, Vec2 position) {
        this.size = size;
        this.position = position;
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
