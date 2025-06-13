public class Main {
    public static void main(String[] args) {
        solve();
    }

    public static void solve() {
        // initialize Sudoku solver
        SudokuSolver solver = new SudokuSolver();

        // create puzzle
        int[][] intGrid = {
            {1, 2, 3, 4, 5, 6, 7, 0, 9},
            {4, 0, 0, 7, 0, 9, 1, 0, 3},
            {7, 0, 0, 1, 0, 0, 4, 0, 0},
            {2, 0, 0, 0, 0, 7, 0, 0, 0},
            {5, 6, 0, 8, 0, 0, 0, 0, 4},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {3, 0, 0, 2, 0, 0, 6, 0, 0},
            {0, 0, 5, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0}
        };

        // initialize puzzle object
        Square[][] newSquareGrid = Square.intsToSquares(intGrid); // first make 2D Square array
        Puzzle sud = new Puzzle(newSquareGrid);

        // display unsolved puzzle
        System.out.println("\n\nPUZZLE:\n");
        System.out.println(sud);

        // keep track of time to solve
        long timeStart = System.currentTimeMillis();
        solver.recurSolve(sud);
        long timeEnd = System.currentTimeMillis();

        // print stats
        System.out.println("Recursion limit: " + (solver.getRecursionLimitActive() ? solver.getRecursionLimit() : "no limit"));
        System.out.println("Solution limit: " + (solver.getPuzzleLimitActive() ? solver.getPuzzleLimit() : "no limit"));
        System.out.printf("Time elapsed: %.3f seconds%n", (timeEnd - (double) timeStart) / 1000);
        System.out.println("Max recursive depth: " + solver.getMaxRecursion());

        // print up to the first ten solutions
        System.out.printf("%nSOLUTIONS (%d):%n%n", solver.getSolutions().size());

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