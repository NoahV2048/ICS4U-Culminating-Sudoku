import java.util.ArrayList;
import java.util.Arrays;

// TODO maybe some memoization!
// TODO maybe include switch-case somewhere in the Puzzle methods

public class SudokuSolver {
    // all must be static for recursive copies to work
    public static ArrayList<Puzzle> solutions;
    public static int recursionLimit;
    public static int maxRecursion;
    public static boolean recursionLimitActive;
    public static boolean findOnePuzzle;

    public SudokuSolver() {

    }

    public void recurSolve(int[][] grid) {
        // reset all this stuff per solve
        solutions = new ArrayList<>();
        maxRecursion = 0;
        recursionLimitActive = false;
        findOnePuzzle = false;

        // TODO use O(n) to determine appropriate recursion depth (but this works pretty well)
        recursionLimit = (int) Math.sqrt(grid.length) - 1;
        recurSolve(grid, 0);
    }

    public void recurSolve(int[][] grid, int depth) {
        if (recursionLimitActive && depth > recursionLimit) return; // depth limit
        if (findOnePuzzle && solutions.size() == 1) return; // puzzle limit
        if (depth > maxRecursion) maxRecursion = depth; // update maxRecursion

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
                    if (puzzle.getGrid()[row][col] == 0) { // TODO OPTIMIZE THIS FOR THE SQUARE WITH MINIMUM OPTIONS THAT WILL BE SUPER DUPER FAST // OR IT MIGHT BE UNNECESSARY
                        for (int i : puzzle.getInvalid(row, col)) {
                            // optimized this step to only operate on valid numbers

                            // FIXED MAJOR BUG INVOLVING SHALLOW COPIES
                            // https://www.geeksforgeeks.org/arrays-copyof-in-java-with-examples/
                            int[][] newGrid = puzzle.gridCopy();
                            newGrid[row][col] = i;

                            // recursive case
                            recurSolve(newGrid, depth + 1); // keep track of depth and increment
                        }
                        return; // THIS LINE sped up the program insanely, insanely effectively
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

        if (addNew) solutions.add(puzzle);
    }
}