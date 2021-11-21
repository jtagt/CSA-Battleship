package me.jtagt.battleship;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private int[] row0;
    private int[] row1;
    private int[] row2;
    private int[] row3;
    private int[] row4;
    private int[] row5;
    private int[] row6;
    private int[] row7;
    private int[] row8;
    private int[] row9;

    private final ArrayList<Ship> ships = new ArrayList<>();

    private final String name; // required for some reason????
    private boolean showShips; // required for some reason????

    public Board(String name) {
        this.name = name; // required for some reason????
    }

    public Board(String name, boolean showShips) {
        this.name = name; // required for some reason????
        this.showShips = showShips; // required for some reason????
    }

    public void initializeSpaces() {
        this.row0 = new int[10];
        this.row1 = new int[10];
        this.row2 = new int[10];
        this.row3 = new int[10];
        this.row4 = new int[10];
        this.row5 = new int[10];
        this.row6 = new int[10];
        this.row7 = new int[10];
        this.row8 = new int[10];
        this.row9 = new int[10];

        Arrays.fill(this.row0, 0);
        Arrays.fill(this.row1, 0);
        Arrays.fill(this.row2, 0);
        Arrays.fill(this.row3, 0);
        Arrays.fill(this.row4, 0);
        Arrays.fill(this.row5, 0);
        Arrays.fill(this.row6, 0);
        Arrays.fill(this.row7, 0);
        Arrays.fill(this.row8, 0);
        Arrays.fill(this.row9, 0);
    }

    public Vec2 getSize() {
        int size = this.row0.length;

        return new Vec2(size - 1, size - 1);
    }

    public int[] getRowArray(Vec2 position) {
        return switch (position.getX()) {
            case 0 -> row0;
            case 1 -> row1;
            case 2 -> row2;
            case 3 -> row3;
            case 4 -> row4;
            case 5 -> row5;
            case 6 -> row6;
            case 7 -> row7;
            case 8 -> row8;
            case 9 -> row9;
            default -> null;
        };
    }

    public Vec2 transformIndexToCoordinates(int index) {
        Vec2 size = this.getSize();

        int x = index % (size.getX() + 1);
        int y = index / (size.getX() + 1);

        return new Vec2(x, y);
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

    public boolean placeShip(Ship ship) {
        this.ships.add(ship);

        return true;
    }

    public boolean performAttack(Vec2 position) {
        int boardSize = this.getSize().getX();

        if (position.getX() > boardSize || position.getY() > boardSize) return false;
        if (position.getX() < 0 || position.getY() < 0) return false;

        int[] row = this.getRowArray(position);

        if (row[position.getY()] != 0) return false;
        row[position.getY()] = 1;

        return true;
    }

    public boolean allShipsDestroyed() {
        for (Ship ship : this.ships) {
            Vec2[] bounds = ship.calculateBoundingBox();

            Vec2 lowerBound = bounds[0];
            Vec2 upperBound = bounds[1];

            if (ship.isVertical()) {
                for (int upperY = upperBound.getX(); upperY >= lowerBound.getX(); upperY--) {
                    Vec2 position = new Vec2(ship.getPosition().getX(), upperY);
                    int[] row = this.getRowArray(position);

                    if (row[position.getX()] != 1) {
                        return false;
                    }
                }
            } else {
                for (int upperX = upperBound.getX(); upperX >= lowerBound.getX(); upperX--) {
                    Vec2 position = new Vec2(upperX, ship.getPosition().getY());
                    int[] row = this.getRowArray(position);

                    if (row[position.getY()] != 1) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public String printBoard(boolean showShips) {
        StringBuilder builder = new StringBuilder();

        int colCount = 0;
        for (int i = 0; i < this.row0.length * this.row0.length; i++) { // too lazy to raise to a power
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

                Vec2 indexToCoordinates = this.transformIndexToCoordinates(i);
                if (ship.isVertical()) {
                    for (int upperY = upperBound.getY(); upperY >= lowerBound.getY(); upperY--) {
                        Vec2 position = new Vec2(ship.getPosition().getX(), upperY);

                        if (position.equals(indexToCoordinates)) {
                            int[] row = this.getRowArray(position);

                            if (row[position.getY()] == 1) {
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
                } else {
                    for (int upperX = upperBound.getX(); upperX >= lowerBound.getX(); upperX--) {
                        Vec2 position = new Vec2(upperX, ship.getPosition().getY());

                        if (position.equals(indexToCoordinates)) {
                            int[] row = this.getRowArray(position);

                            if (row[position.getY()] == 1) {
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
            }
            if (isShip) continue;

            Vec2 position = this.transformIndexToCoordinates(i);
            int[] row = this.getRowArray(position);

            if (row[position.getY()] == 0) {
                builder.append(" _ ");
            } else if (row[position.getY()] == 1) {
                builder.append(" O ");
            }
        }

        return builder.toString();
    }
}
