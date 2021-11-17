import java.io.IOException;
import java.util.Scanner;

public class Battleship {
    public enum Ships {
        AIRCRAFT_CARRIER("Aircraft Carrier", 5),
        BATTLESHIP("Battleship", 3),
        SUBMARINE("Submarine", 2);

        private final String name;
        private final int size;

        Ships(String name, int size) {
            this.name = name;
            this.size = size;
        }

        public int getSize() {
            return this.size;
        }
    }

    private static Scanner scanner;

    public static void information() {
        System.out.println("  ____        _   _   _           _     _       \n" +
                " |  _ \\      | | | | | |         | |   (_)      \n" +
                " | |_) | __ _| |_| |_| | ___  ___| |__  _ _ __  \n" +
                " |  _ < / _` | __| __| |/ _ \\/ __| '_ \\| | '_ \\ \n" +
                " | |_) | (_| | |_| |_| |  __/\\__ \\ | | | | |_) |\n" +
                " |____/ \\__,_|\\__|\\__|_|\\___||___/_| |_|_| .__/ \n" +
                "                                         | |    \n" +
                "                                         |_|    ");
    }

    public static void clearScreen() {
        try {
            String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String collectInput(String question) {
        System.out.print(question + ": ");

        return scanner.nextLine();
    }

    public static void placeShipInput(Board playerBoard, Ships shipType) {
        String aircraftPlacement = collectInput(shipType.name + " Placement (" + shipType.size + "x1)");
        Vec2 aircraftPlacementPosition = playerBoard.convertCellToPosition(aircraftPlacement);

        if (aircraftPlacementPosition != null) {
            boolean placed = playerBoard.placeShip(new Ship(shipType, aircraftPlacementPosition));

            if (placed) {
                System.out.println("Successfully placed ship.");
            } else {
                System.out.println("Failed to place ship.");

                placeShipInput(playerBoard, shipType);
            }
        } else {
            System.out.println("Invalid ship placement please try again.");

            placeShipInput(playerBoard, shipType);
        }
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        information();

        int currentTurn = 0; // 0 = player one, 1 = player two
        Board playerOneBoard = new Board(10);
        Board playerTwoBoard = new Board(10);

        /*
        Choose ship placements
         */

        System.out.println("Place your ships player one.");
        for (Ships ship : Ships.values()) {
            placeShipInput(playerOneBoard, ship);
        }

        clearScreen();

        System.out.println("Place your ships player two.");
        for (Ships ship : Ships.values()) {
            placeShipInput(playerTwoBoard, ship);
        }

        clearScreen();


         /*
         Main game round
         */

        while (!playerOneBoard.allShipsDestroyed() && !playerTwoBoard.allShipsDestroyed()) {
            if (currentTurn == 0) {
                System.out.println(playerTwoBoard.getBoardOutput(false));

                String cell = collectInput("Player one please choose a place to hit");
                Vec2 position = playerTwoBoard.convertCellToPosition(cell);
                if (position == null) {
                    System.out.println("Invalid position cell.");

                    continue;
                }

                boolean successful = playerTwoBoard.performAttack(position);
                if (successful) {
                    System.out.println("Successfully attacked.");
                    System.out.println(playerTwoBoard.getBoardOutput(false));

                    currentTurn++;
                } else {
                    System.out.println("Failed to attack.");
                }
            } else {
                System.out.println(playerOneBoard.getBoardOutput(false));

                String cell = collectInput("Player two please choose a place to hit");
                Vec2 position = playerOneBoard.convertCellToPosition(cell);
                if (position == null) {
                    System.out.println("Invalid position cell.");

                    continue;
                }

                boolean successful = playerOneBoard.performAttack(position);
                if (successful) {
                    System.out.println("Successfully attacked.");
                    System.out.println(playerOneBoard.getBoardOutput(false));

                    currentTurn--;
                } else {
                    System.out.println("Failed to attack.");
                }
            }

            clearScreen();
        }

        System.out.println(playerOneBoard.getBoardOutput(true));
        System.out.println(playerTwoBoard.getBoardOutput(true));

        if (playerOneBoard.allShipsDestroyed()) {
            System.out.println("Congratulations player two you have successfully destroyed the opponents ships.");
        } else if (playerTwoBoard.allShipsDestroyed()) {
            System.out.println("Congratulations player one you have successfully destroyed the opponents ships.");
        } else { // impossible
            System.out.println("Not sure how you got here but congrats.");
        }


    }
}
