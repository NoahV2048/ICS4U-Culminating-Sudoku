public class Main {
    public static void main(String[] args) {
        create();
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

        Puzzle sud = new Puzzle(arg);

        // solve puzzle
        System.out.println("PUZZLE:\n");
        System.out.println(sud);

        long timeStart = System.currentTimeMillis();
        solver.recurSolve(sud.getGrid());
        long timeEnd = System.currentTimeMillis();

        System.out.println("Recursion limit: " + (SudokuSolver.recursionLimitActive ? SudokuSolver.recursionLimit : "no limit"));
        System.out.println("Allow multiple puzzles: " + !SudokuSolver.findOnePuzzle);
        System.out.println();
        System.out.printf("Time elapsed: %.3f seconds%n", (timeEnd - (double) timeStart) / 1000);
        System.out.println("Max recursive depth: " + SudokuSolver.maxRecursion);
        System.out.println();
        System.out.printf("SOLUTIONS (%d):%n%n", SudokuSolver.solutions.size());

        if (SudokuSolver.solutions.size() <= 10) {
            for (Puzzle solution : SudokuSolver.solutions) {
                System.out.println(solution);
            }
        }
    }

    public static void create() {
        // initialize Sudoku generator
        SudokuGenerator generator = new SudokuGenerator(3);

        generator.generateNew(25, "TBD");

        System.out.println(generator.getNewPuzzle());
    }
}