import java.io.*;
import java.util.Scanner;

public class Sudoku {

    static class SudokuSolver {

        private int[][] board;
        private int size = -1;
        private int log10 = 0;
        private String numberFormat;
        private static final int SIZE = 9;
        private static final int EMPTY = 0;

        private SudokuSolver(String filename) {
            try {
                readFile(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Reads the text file
        private void readFile(String filename) throws IOException {

            BufferedReader buffer = new BufferedReader(new FileReader(filename));
            String line;
            int row = 0;

            while ((line = buffer.readLine()) != null & row < 9) {
                String[] vals = line.trim().split("\\s+");

                if (board == null) {
                    size = vals.length;
                    board = new int[size][size];
                    log10 = (int) Math.floor(Math.log10(size * size)) + 1;
                    numberFormat = String.format("%%%dd", log10);
                }

                for (int col = 0; col < size; col++) {
                    board[row][col] = Integer.parseInt(vals[col]);
                }

                row++;
            }
        }

        //Displays Sudoku puzzle as 3x3 squares
        @Override
        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();

            if (board != null) {
                for (int row = 0; row < size; row++) {
                    stringBuffer.append(" ");
                    for (int col = 0; col < size; col++) {
                        stringBuffer.append(String.format(numberFormat, board[row][col]));
                        if ((col + 1) % 3 == 0 && col < size - 1) {
                            stringBuffer.append(" | ");
                        }
                    }
                    if ((row + 1) % 3 == 0 && row < size - 1) {
                        stringBuffer.append("\n");
                        for (int col = 0; col < size; col++) {
                            for (int i = 0; i <= log10; i++) {
                                stringBuffer.append("-");
                            }
                        }
                        stringBuffer.append("\n");
                    } else {
                        stringBuffer.append("\n");
                    }
                }
            }

            return stringBuffer.toString();
        }

        //Checks to see if all the numbers in the row are unique
        private boolean uniqueRow(int row, int number) {
            for (int i = 0; i < SIZE; i++)
                if (board[row][i] == number) {
                    return true;
                }
            return false;
        }

        //Check to see if all the numbers in the columns are unique
        private boolean uniqueColumn(int col, int number) {
            for (int i = 0; i < SIZE; i++)
                if (board[i][col] == number) {
                    return true;
                }
            return false;
        }

        //Checks to see if all the numbers in the 3x3 box are unique
        private boolean uniqueBox(int row, int col, int number) {
            int r = row - row % 3;
            int c = col - col % 3;

            for (int i = r; i < r + 3; i++) {
                for (int j = c; j < c + 3; j++) {
                    if (board[i][j] == number) {
                        return true;
                    }
                }
            }

            return false;
        }

        //Making sure all three constraints check out
        private boolean isOk(int row, int col, int number) {
            return !uniqueRow(row, number) && !uniqueColumn(col, number) && !uniqueBox(row, col, number);
        }

        //Solving the puzzle using recursive backtracking
        private boolean solvePuzzle() {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (board[row][col] == EMPTY) {
                        for (int number = 1; number <= SIZE; number++) {
                            if (isOk(row, col, number)) {
                                board[row][col] = number;
                                if (solvePuzzle()) {
                                    return true;
                                } else {
                                    board[row][col] = EMPTY;
                                }
                            }
                        }

                        return false;
                    }
                }
            }

            return true;
        }
    }

    public static void main(String[] args) {

        //User provides file path of the sudoku puzzles
        //Solves the puzzle and tells you how long it takes to solvePuzzle in ms
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter file path for the sudoku puzzle you would like to solvePuzzle: ");
        String usersFilePath = keyboard.nextLine();
        SudokuSolver puzzle = new SudokuSolver(usersFilePath);
        System.out.println();
        System.out.println("Sudoku puzzle to be solved");
        System.out.println();
        System.out.println(puzzle.toString());

        long startTime = System.currentTimeMillis();

        if (puzzle.solvePuzzle()) {
            System.out.println();
            System.out.println("Sudoku puzzle solved");
            System.out.println();
            System.out.println(puzzle.toString());
        } else {
            //testPuzzle.txt is unsolvable
            System.out.println("Sudoku puzzle unsolvable");
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.print("Time it took to solve the puzzle: " + totalTime + "ms");

    }
}
