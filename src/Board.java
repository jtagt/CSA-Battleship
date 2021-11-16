import java.util.Arrays;

public class Board {
    private final String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private final Integer[] boardData;
    private Ship[] ships;


    public Board(int size) {
        int totalSize = (int) Math.pow(size, 2);
        this.boardData = new Integer[totalSize];

        Arrays.fill(this.boardData, 0);
    }

    public Vec2 getSize() {
        int size = (int) Math.sqrt(this.boardData.length);

        return new Vec2(size - 1, size - 1);
    }

    public Vec2 transformIndexToCoordinates(int index) {
        Vec2 size = this.getSize();

        int y = index / size.getX();
        int x = index - (y * size.getX());

        return new Vec2(x, y);
    }

    public int transformCoordinatesToIndex(Vec2 position) {
        return position.getX() + (position.getY() * (this.getSize().getX() + 1));
    }

    public int convertLetterToNumber(String letter) {
        int totalSize = this.getSize().getX() + 1;

        //if (totalSize)

        for (int i = 0; i < this.alphabet.length; i++) {
            String currentLetter = this.alphabet[i];

            if (letter.equalsIgnoreCase(currentLetter)) {
                return i;
            }
        }

        return -1;
    }

    public boolean isWithinBounds(Vec2 position, Ship ship) {
        return false;
    }

    public String getBoardOutput(Ship[] ships) {
        StringBuilder builder = new StringBuilder();

        int colCount = 0;
        for (int i = 0; i < this.boardData.length; i++) {
            if (i % (this.getSize().getX() + 1) == 0) {
                if (i == 0) {
                    for (int x = 0; x < this.getSize().getX() + 1; x++) {
                        if (x == 0) {
                            builder.append("   ");
                        }

                        builder.append(this.alphabet[x]).append("  ");
                    }
                }

                builder.append("\n").append(colCount).append(" ");
                colCount++; // couldn't use mod for some reason
            }

            boolean isShip = false;
            for (Ship ship : ships) {
                Vec2[] bounds = ship.calculateBoundingBox();

                Vec2 lowerBound = bounds[0];
                Vec2 upperBound = bounds[1];

                for (int upperX = upperBound.getX(); upperX >= lowerBound.getX(); upperX--) {
                    Vec2 position = new Vec2(upperX, ship.getPosition().getY());
                    int replaceIndex = this.transformCoordinatesToIndex(position);

                    if (i == replaceIndex) {
                        isShip = true;

                        builder.append(" = ");
                    }
                }
            }

            if (isShip) {
                continue;
            }

            if (this.boardData[i] == 0) {
                builder.append(" ~ ");
            } else if (this.boardData[i] == 1) {
                builder.append(" O ");
            } else if (this.boardData[i] == 2) {
                builder.append(" X ");
            }
        }

        return builder.toString();
    }
}
