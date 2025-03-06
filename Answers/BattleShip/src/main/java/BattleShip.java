
import java.util.Scanner;
import java.util.Vector;
public class BattleShip {

    static final int GRID_SIZE = 10;

    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        placeShips(player1Grid);
        placeShips(player2Grid);

        boolean player1Turn = true;

        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
    }

    static void initializeGrid(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = '~';
            }
        }
    }

    static void placeShips(char[][] grid) {
        int[] shipSizes = {5, 4, 3, 3, 2};

        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int row = (int) (Math.random() * GRID_SIZE);
                int col = (int) (Math.random() * GRID_SIZE);
                boolean horizontal = Math.random() < 0.5;

                if (canPlaceShip(grid, row, col, size, horizontal)) {
                    for (int i = 0; i < size; i++) {
                        if (horizontal) {
                            grid[row][col + i] = 'S';
                        } else {
                            grid[row + i][col] = 'S';
                        }
                    }
                    placed = true;
                }
            }
        }
    }

    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > GRID_SIZE) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row][col + i] != '~') return false;
            }
        } else {
            if (row + size > GRID_SIZE) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row + i][col] != '~') return false;
            }
        }
        return true;
    }

    static boolean isValidInput(String input) {
        if (input.length() < 2 || input.length() > 3) {
            return false;
        }

        char column = input.charAt(0);
        int row;

        try {
            row = Integer.parseInt(input.substring(1));
        } catch (NumberFormatException e) {
            return false;
        }

        return (column >= 'A' && column <= 'J') && (row >= 1 && row <= 10);
    }

    static void printGrid(char[][] grid) {
        System.out.println("  A B C D E F G H I J");
        System.out.println("  -------------------");

        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        System.out.print("Enter your attack coordinates (e.g., A5): ");
        String input = scanner.nextLine().toUpperCase();

        if (!isValidInput(input)) {
            System.out.println("Invalid input. You lost your turn.");
            return;
        }

        int row = Integer.parseInt(input.substring(1)) - 1;
        int col = input.charAt(0) - 'A';

        if (trackingGrid[row][col] != '~') {
            System.out.println("You already attacked this location. You lost your turn.");
            return;
        }

        if (opponentGrid[row][col] == 'S') {
            System.out.println("Hit!");
            trackingGrid[row][col] = 'X';
            opponentGrid[row][col] = 'X';
        } else {
            System.out.println("Miss!");
            trackingGrid[row][col] = 'O';
        }
    }

    static boolean isGameOver() {
        return allShipsSunk(player1Grid) || allShipsSunk(player2Grid);
    }

    static boolean allShipsSunk(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == 'S') {
                    return false;
                }
            }
        }
        return true;
    }
}