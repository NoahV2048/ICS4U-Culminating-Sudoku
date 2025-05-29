import java.util.ArrayList;
import java.util.Arrays;

public class SudokuSolver {
    public static ArrayList<Puzzle> solutions = new ArrayList<>();
    public static int recursionLimit;
    public static boolean recursionLimitActive = false;
    public static boolean findOnePuzzle = !true;

    public static void main(String[] args) {

        // create puzzle
        int[][] arg = {
                // {0, 1, 3, 0, 0, 0, 0, 0, 0},
                // {0, 0, 0, 5, 0, 0, 0, 0, 4},
                // {5, 0, 0, 0, 2, 7, 0, 0, 3},
                // {0, 5, 0, 0, 6, 0, 0, 0, 0},
                // {7, 3, 0, 0, 0, 5, 0, 2, 0},
                // {9, 0, 0, 0, 0, 0, 0, 0, 0},
                // {0, 7, 0, 0, 9, 0, 8, 0, 2},
                // {2, 0, 0, 0, 0, 0, 1, 9, 0},
                // {0, 0, 0, 0, 1, 0, 0, 0, 0}

                {8, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 3, 6, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 9, 0, 2, 0, 0}, // 2 0 0
                {0, 5, 0, 0, 0, 7, 0, 0, 0},
                {0, 0, 0, 0, 4, 5, 7, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 3, 0},
                {0, 0, 1, 0, 0, 0, 0, 6, 8},
                {0, 0, 8, 5, 0, 0, 0, 1, 0},
                {0, 9, 0, 0, 0, 0, 4, 0, 0}

                // {1, 2, 0, 0, 0, 0, 0, 0, 0},
                // {0, 0, 0, 0, 0, 0, 0, 0, 0},
                // {0, 0, 0, 0, 0, 0, 0, 0, 0},
                // {0, 0, 0, 0, 0, 0, 0, 0, 0},
                // {0, 0, 0, 0, 0, 0, 0, 0, 0},
                // {0, 0, 0, 0, 0, 0, 0, 0, 0},
                // {0, 0, 0, 0, 0, 0, 0, 0, 0},
                // {0, 0, 0, 0, 0, 0, 0, 0, 0},
                // {0, 0, 0, 0, 0, 0, 0, 0, 0}

                // {0, 0, 0, 0},
                // {0, 0, 0, 0},
                // {0, 0, 0, 0},
                // {0, 0, 0, 0}

                // TODO fix plugging in numbers being much faster than doing full recursion
                // maybe some memoization!
                // maybe include switch-case somewhere in the Puzzle methods
        };
        Puzzle sud = new Puzzle(arg);

        // solve puzzle
        System.out.println("PUZZLE:\n");
        System.out.println(sud);

        long timeStart = System.currentTimeMillis();
        recurSolve(sud.getGrid());
        long timeEnd = System.currentTimeMillis();

        System.out.println("Time elapsed: " + (timeEnd - (double) timeStart) / 1000 + " seconds.");
        System.out.println("Recursion limit: " + (recursionLimitActive ? recursionLimit : "no limit"));
        System.out.println("Allow multiple puzzles: " + !findOnePuzzle);
        System.out.println();

        System.out.printf("SOLUTIONS (%d):%n%n", solutions.size());

        if (solutions.size() < 10) {
            for (Puzzle solution : solutions) {
                System.out.println(solution);
            }
        }
    }

    public static void recurSolve(int[][] grid) {
        // TODO use O(n) to determine appropriate recursion depth (but this works pretty well)
        recursionLimit = (int) Math.sqrt(grid.length) - 1;
        recurSolve(grid, 0);
    }

    public static void recurSolve(int[][] grid, int depth) {
        if (recursionLimitActive && depth > recursionLimit) return; // depth limit
        if (findOnePuzzle && solutions.size() == 1) return; // puzzle limit

        Puzzle puzzle = new Puzzle(grid);

        do {
            // debug
            // System.out.println(puzzle.stringInvalid());
            // System.out.println(puzzle);

            // check for unsolvable
            if (!puzzle.checkValid()) return; // no changes to solution list
        } while (puzzle.updateByElim() || puzzle.updateByNecessity());

        if (puzzle.isComplete()) {
            addSolution(puzzle);
            return;
        } else {
            int size = puzzle.getSize();

            for (int row = 0; row < size*size; row++) {
                for (int col = 0; col < size*size; col++) {
                    if (puzzle.getGrid()[row][col] == 0) { // TODO OPTIMIZE THIS FOR THE SQUARE WITH MINIMUM OPTIONS THAT WILL BE SUPER DUPER FAST
                        for (int i : puzzle.getInvalid(row, col)) {
                            // optimized this step to only operate on valid numbers
                            // FIXED MAJOR BUG INVOLVING SHALLOW COPIES
                            // https://www.geeksforgeeks.org/arrays-copyof-in-java-with-examples/

                            // TODO maybe update invalid entries at each step?
                            int[][] newGrid = puzzle.gridCopy();
                            newGrid[row][col] = i;

                            // recursive case
                            recurSolve(newGrid, depth + 1); // keep track of depth and increment

                            // experimental TODO --> doing recursion one layer at a time until a solution is found
                            // recurSolve(newGrid, depth);
                        }
                        return; // HOLYYYYYYY SHITTTTTT
                    }
                }
            }
        }
    }

    public static void addSolution(Puzzle puzzle) {
        boolean addNew = true;

        for (Puzzle solution : solutions) {
            if (puzzle.equals(solution)) { // if solution is already found, don't add
                addNew = false;
                break;
            }
        }

        if (addNew) {
            // debug
            // System.out.println(puzzle);
            // System.out.println("Found a solution!");

            solutions.add(puzzle);
        }
    }
}