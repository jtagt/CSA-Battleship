package me.jtagt.battleship;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private final Integer[] boardData;
    private final ArrayList<Ship> ships = new ArrayList<>();


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

    public Vec2 convertCellToPosition(String cell) {
        if (cell.length() != 2) return null;

        String column = cell.substring(0, 1);
        String row = cell.substring(1, 2);

        int rowParsed;
        try {
            rowParsed = Integer.parseInt(row);
        } catch (NumberFormatException e) {
            return null;
        }
        if (rowParsed > this.getSize().getX()) return null;

        int columnConverted = this.convertLetterToNumber(column);
        if (columnConverted == -1) return null;

        return new Vec2(columnConverted, rowParsed);
    }

    private int convertLetterToNumber(String letter) {
        int totalSize = this.getSize().getX();

        for (int i = 0; i < this.alphabet.length; i++) {
            String currentLetter = this.alphabet[i];

            if (letter.equalsIgnoreCase(currentLetter)) {
                if (i > totalSize) { // out of bounds of width
                    break;
                }

                return i;
            }
        }

        return -1;
    }

    public boolean isWithinBounds(Vec2 position, Ship ship) {
        return false;
    }

    public boolean placeShip(Ship ship) {
        this.ships.add(ship);

        return true;
    }

    public boolean performAttack(Vec2 position) {
        int boardSize = this.getSize().getX();

        if (position.getX() > boardSize || position.getY() > boardSize) return false;
        if (position.getX() < 0 || position.getY() < 0) return false;

        int boardIndex = this.transformCoordinatesToIndex(position);
        if (this.boardData[boardIndex] != 0) return false;

        this.boardData[boardIndex] = 1;
        return true;
    }

    public boolean allShipsDestroyed() {
        for (Ship ship : this.ships) {
            Vec2[] bounds = ship.calculateBoundingBox();

            Vec2 lowerBound = bounds[0];
            Vec2 upperBound = bounds[1];

            for (int upperX = upperBound.getX(); upperX >= lowerBound.getX(); upperX--) {
                Vec2 position = new Vec2(upperX, ship.getPosition().getY());
                int boardIndex = this.transformCoordinatesToIndex(position);

               if (this.boardData[boardIndex] != 1) {
                   return false;
               }
            }
        }

        return true;
    }

    public String getBoardOutput(boolean showShips) {
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
            for (Ship ship : this.ships) {
                Vec2[] bounds = ship.calculateBoundingBox();

                Vec2 lowerBound = bounds[0];
                Vec2 upperBound = bounds[1];

                for (int upperX = upperBound.getX(); upperX >= lowerBound.getX(); upperX--) {
                    Vec2 position = new Vec2(upperX, ship.getPosition().getY());
                    int replaceIndex = this.transformCoordinatesToIndex(position);

                    if (i == replaceIndex) {
                        if (this.boardData[i] == 1) {
                            builder.append(" X ");
                        } else if (showShips) {
                            switch (ship.getType()) {
                                case AIRCRAFT_CARRIER -> builder.append(" A ");
                                case BATTLESHIP -> builder.append(" B ");
                                case SUBMARINE -> builder.append(" S ");
                            }
                        } else {
                            builder.append(" _ ");
                        }

                        isShip = true;
                    }
                }
            }
            if (isShip) continue;


            if (this.boardData[i] == 0) {
                builder.append(" _ ");
            } else if (this.boardData[i] == 1) {
                builder.append(" O ");
            }
        }

        return builder.toString();
    }
}
