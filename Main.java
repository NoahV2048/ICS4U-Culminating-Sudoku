public class Main {
    public static void main(String[] args) {
        solve();
    }

    public static void solve() {
        // initialize Sudoku solver
        SudokuSolver solver = new SudokuSolver();

        // create puzzle
        int[][] arg = {
            {1, 2, 3, 4, 5, 6, 7, 8, 9},
            {4, 5, 6, 7, 8, 9, 1, 2, 3},
            {7, 8, 9, 1, 2, 3, 4, 5, 6},
            {2, 3, 4, 5, 6, 7, 8, 9, 1},
            {5, 6, 7, 8, 9, 1, 2, 3, 4},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {3, 0, 0, 2, 0, 0, 6, 0, 0},
            {0, 0, 5, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0}
        };

        Square[][] newSquareGrid = new Square[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                newSquareGrid[row][col] = new Square(3);
                newSquareGrid[row][col].setValue(arg[row][col]);
            }
        }

        Puzzle sud = new Puzzle(newSquareGrid);

        // solve puzzle
        System.out.println("\n\nPUZZLE:\n");
        System.out.println(sud);

        long timeStart = System.currentTimeMillis();
        solver.recurSolve(sud);
        long timeEnd = System.currentTimeMillis();

        System.out.println("Recursion limit: " + (solver.getRecursionLimitActive() ? solver.getRecursionLimit() : "no limit"));
        System.out.println("Solutions limit: " + (solver.getPuzzleLimitActive() ? solver.getPuzzleLimit() : "no limit"));
        System.out.println();
        System.out.printf("Time elapsed: %.3f seconds%n", (timeEnd - (double) timeStart) / 1000);
        System.out.println("Max recursive depth: " + solver.getMaxRecursion());
        System.out.println();
        System.out.printf("SOLUTIONS (%d):%n%n", solver.getSolutions().size());

        if (solver.getSolutions().size() <= 10) {
            for (Puzzle solution : solver.getSolutions()) {
                System.out.println(solution);
            }
        }
    }



    // // TODO: does not work rn
    // public static void create() {
    //     // initialize Sudoku generator
    //     SudokuGenerator generator = new SudokuGenerator(3);
    //
    //     generator.generateNew(25, "TBD");
    //
    //     System.out.println(generator.getNewPuzzle());
    // }
}