import java.util.ArrayList;
import java.util.Arrays;

// TODO maybe include switch-case somewhere in the Puzzle methods

public class SudokuSolver {
    private ArrayList<Puzzle> solutions;
    private int maxRecursion;

    // hardcoded settings
    private final int recursionLimit;
    private final boolean recursionLimitActive;
    private final int puzzleLimit;
    private final boolean puzzleLimitActive;


    // Constructor method
    public SudokuSolver() {
        // initialize some hardcoded settings
        puzzleLimitActive = false;
        puzzleLimit = 1;
        recursionLimitActive = false;
        recursionLimit = -1; // recursionLimit could be based on puzzle size, but the limit is not rly necessary in practice
    }


    // Getter methods
    public ArrayList<Puzzle> getSolutions() {
        return solutions;
    }

    public int getMaxRecursion() {
        return maxRecursion;
    }


    // Misc helper methods
    private void addSolution(Puzzle puzzle) {
        boolean addNew = true;

        for (Puzzle solution : solutions) {
            if (puzzle.equals(solution)) { // if the solution is already found, don't add it
                addNew = false;
                break;
            }
        }
        if (addNew) solutions.add(puzzle);
    }


    // Recursive solving methods
    public void recurSolve(Puzzle puzzle) {
        // dedicated method before first recursion step

        // reset these things per solve
        solutions = new ArrayList<>();
        maxRecursion = 0;

        recurSolve(puzzle.copy(), 0); // create copy to avoid overriding
    }

    private void recurSolve(Puzzle puzzle, int depth) {
        // this is only triggered by the call in the other definition of recurSolve

        if (recursionLimitActive && depth > recursionLimit) return; // recursion limit reached
        if (puzzleLimitActive && solutions.size() >= puzzleLimit) return; // puzzle limit reached
        if (depth > maxRecursion) maxRecursion = depth; // update maxRecursion for stats

        // fully check and update the puzzle first
        do {
            if (!checkValid(puzzle)) return; // check if current puzzle arrangement is valid, and return if it is not
        } while (updateByElim(puzzle) || updateByNecessity(puzzle)); // update puzzle

        // now operate on the updated puzzle
        if (puzzle.isComplete()) {
            addSolution(puzzle); // if the puzzle is fully filled in at this stage, it must be a valid solution
        }

        else {
            int size = puzzle.getSize();
            Square[][] grid = puzzle.getGrid();

            for (int row = 0; row < size*size; row++) {
                for (int col = 0; col < size*size; col++) {
                    if (grid[row][col].getValue() == 0) { // TODO: COULD OPTIMIZE THIS FOR THE SQUARE WITH MINIMUM OPTIONS
                        for (int i : grid[row][col].getValid()) {
                            // this step only operates on valid numbers

                            Puzzle newPuzzle = puzzle.copy();
                            newPuzzle.getGrid()[row][col].setValue(i); // assume a square is filled in as one possible value

                            // recursive case
                            recurSolve(newPuzzle, depth + 1); // keep track of recursion depth and increment
                        }
                        return; // the magic line that makes things a billion times faster
                    }
                }
            }
        }
    }


    // Dedicated checker methods
    private boolean checkValid(Puzzle puzzle) {
        int size = puzzle.getSize();
        Square[][] grid = puzzle.getGrid();

        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                int square = grid[row][col] - 1;

                if (square != -1) {
                    for (int i = 0; i < size*size; i++) {
                        // make all other values invalid but leave square value unchanged
                        // this helps find invalid squares

                        // for just this square
                        if (i != square) grid[row][col].setInvalid(i, true);

                        // row cancel
                        if (i != col) invalid[row][i][square] = true;

                        // col cancel
                        if (i != row) invalid[i][col][square] = true;

                        // box cancel
                        int refy = (row / size) * size, refx = (col / size) * size;
                        if ((refy + i / size) != row && (refx + i % size) != col) {
                            invalid[refy + i / size][refx + i % size][square] = true;
                        }
                    }
                }
            }
        }

        // possibilities restricting other possibilities in BOXES

        // example where 1 must be in either spot, blocking off the row
        // 11->
        // 0000
        // 0000
        // 0000

        // TODO: THIS WHOLE STEP MIGHT ACTUALLY MAKE THINGS SLOWER

        // for (int boxRow = 0; boxRow < size; boxRow++) {
        //     for (int boxCol = 0; boxCol < size; boxCol++) {
        //         int[][] rows = new int[size*size][size];
        //         int[][] cols = new int[size*size][size];

        //         for (int miniRow = 0; miniRow < size; miniRow++) {
        //             for (int miniCol = 0; miniCol < size; miniCol++) {
        //                 for (int i = 0; i < size*size; i++) {
        //                     if (!invalid[boxRow*size+miniRow][boxCol*size+miniCol][i]) {
        //                         rows[i][miniRow]++;
        //                         cols[i][miniCol]++;
        //                     }
        //                 }
        //             }
        //         }

        //         for (int i = 0; i < size*size; i++) {
        //             int[] rowIndices = topTwo(rows[i]); // selection sort-ish operation
        //             int[] colIndices = topTwo(cols[i]); // returns [val1, val2, ind1]

        //             // row conditions
        //             if (rowIndices[0] > 1 && rowIndices[1] == 0) { // means one row has multiple of a number and others have none
        //                 int rowToUpdate = rowIndices[2] + boxRow*size;

        //                 for (int shift = 1; shift < size*(size-1); shift++) {
        //                     invalid[rowToUpdate][((boxCol+1)*size + shift) % size*size][i] = false; // update
        //                 }
        //             }

        //             // col conditions
        //             if (colIndices[0] > 1 && colIndices[1] == 0) { // means one col has multiple of a number and others have none
        //                 int colToUpdate = colIndices[2] + boxCol*size;

        //                 for (int shift = 1; shift < size*(size-1); shift++) {
        //                     invalid[((boxRow+1)*size + shift) % size*size][colToUpdate][i] = false; // update
        //                 }
        //             }
        //         }
        //     }
        // }

        for (int i = 0; i < size*size; i++) {
            for (int num = 0; num < size*size; num++) {
                ArrayList<Integer> rowCheck = new ArrayList<>();
                ArrayList<Integer> colCheck = new ArrayList<>();
                ArrayList<Integer> boxCheck = new ArrayList<>();

                for (int j = 0; j < size*size; j++) {
                    // row
                    if (!invalid[i][j][num]) rowCheck.add(j);

                    // col
                    if (!invalid[j][i][num]) colCheck.add(j);

                    // box
                    int refy = (i / size) * size, refx = (i % size) * size;
                }
            }
        } // TODO something here?

        // solvable check
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                // check if unsolvable
                boolean squareContradiction = true;

                // make ArrayList for possible values
                ArrayList<Integer> possible = new ArrayList<>();
                for (int i = 0; i < size*size; i++) {
                    if (!invalid[row][col][i]) {
                        possible.add(i+1);

                        if (i+1 == grid[row][col]) squareContradiction = false; // row can be what it already is (good!)
                    }
                }

                // obvious unsolvable case
                if (possible.size() == 0) return false;

                // whole arrangement is invalid because number disagrees with possible values
                if (grid[row][col] != 0 && squareContradiction) return false;
            }
        }
        return true; // default case meaning arrangement is valid
    }

    private boolean updateByElim(Puzzle puzzle) {
        int size = puzzle.getSize();
        Square[][] grid = puzzle.getGrid();

        boolean actionTaken = false;

        // iterate through all squares
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                if (grid[row][col].getValue() != 0) continue; // operate on a square if it is not yet filled in

                ArrayList<Integer> possible = grid[row][col].getValid(); // get possible values

                if (possible.size() == 1) {
                    grid[row][col].setValue(possible.getFirst()); // if only one possible value, it must be that value
                    actionTaken = true; // an action was taken, so the call to this method was not useless
                }
            }
        }
        return actionTaken; // return whether this method changed anything
    }

    private boolean updateByNecessity(Puzzle puzzle) {
        int size = puzzle.getSize();
        Square[][] grid = puzzle.getGrid();

        boolean actionTaken = false;

        // iterate through all rows, cols, and boxes
        for (int i = 0; i < size*size; i++) {
            for (int num = 0; num < size*size; num++) {
                ArrayList<Integer> rowCheck = new ArrayList<>();
                ArrayList<Integer> colCheck = new ArrayList<>();
                ArrayList<Integer> boxCheck = new ArrayList<>();

                // i is primary, j is secondary for iteration (not strictly rows and cols in this context)
                for (int j = 0; j < size*size; j++) {
                    // row
                    if (!invalid[i][j][num]) rowCheck.add(j);

                    // col
                    if (!invalid[j][i][num]) colCheck.add(j);

                    // box
                    int refy = (i / size) * size, refx = (i % size) * size;
                    if (!invalid[refy + j / size][refx + j % size][num]) boxCheck.add(j);
                }

                // UPDATES!
                if (rowCheck.size() == 1 && grid[i][rowCheck.get(0)] == 0) {
                    grid[i][rowCheck.get(0)] = num+1;
                    actionTaken = true;
                }
                if (colCheck.size() == 1 && grid[colCheck.get(0)][i] == 0) {
                    grid[colCheck.get(0)][i] = num+1;
                    actionTaken = true;
                }
                if (boxCheck.size() == 1) {
                    int j = boxCheck.get(0);
                    int refy = (i / size) * size, refx = (i % size) * size;

                    if (grid[refy + j / size][refx + j % size] == 0) {
                        grid[refy + j / size][refx + j % size] = num+1;
                        actionTaken = true;
                    }
                }
            }
        }
        return actionTaken; // return whether this method changed anything
    }
}