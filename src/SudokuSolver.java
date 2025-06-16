package src;

import java.util.ArrayList;

public class SudokuSolver {
    private ArrayList<Puzzle> solutions;
    private int maxRecursion;

    // hardcoded static settings
    private static final int recursionLimit = 1;
    private static final boolean recursionLimitActive = false;
    private static int puzzleLimit = 10;
    private static boolean puzzleLimitActive = true;
    private static boolean randomSolutions = false;


    // Constructor method
    public SudokuSolver() {
        // hardcoded settings already initialized so nothing to do here
    }


    // Getter methods
    public ArrayList<Puzzle> getSolutions() {
        return solutions;
    }

    public int getMaxRecursion() {
        return maxRecursion;
    }


    // Getter methods just for settings
    public static boolean getPuzzleLimitActive() {
        return puzzleLimitActive;
    }

    public static int getPuzzleLimit() {
        return puzzleLimit;
    }

    public static boolean getRecursionLimitActive() {
        return recursionLimitActive;
    }

    public static int getRecursionLimit() {
        return recursionLimit;
    }

    public static boolean getRandomSolutionsActive() {
        return randomSolutions;
    }


    // Setter methods for easier manipulation in Main
    public static void setSolutionLimit(int lim) {
        puzzleLimit = lim;
        if (puzzleLimit < 1) puzzleLimit = 1; // min limit of 1
    }

    public static void setPuzzleLimitActive(boolean bool) {
        puzzleLimitActive = bool;
    }

    public static void setRandomSolutions(boolean bool) {
        randomSolutions = bool;
    }


    // Misc helper methods
    private void addSolution(Puzzle puzzle) {
        boolean alreadyFound = false;

        // not necessary to check previously found solutions if they are sorted
        // but must be done for random solutions
        if (randomSolutions) {
            for (Puzzle solution : solutions) {
                if (puzzle.equals(solution)) { // if the solution is already found, don't add it
                    alreadyFound = true;
                    break;
                }
            }

        }

        // add solution if not found already
        if (!alreadyFound) solutions.add(puzzle);
    }


    // Recursive solving methods
    public void recurSolve(Puzzle puzzle) {
        // dedicated method before first recursion step

        // reset these things per solve
        solutions = new ArrayList<>();
        maxRecursion = 0;

        // randomSolutions == true, recurSolve must be called multiple times
        if (randomSolutions) {
            if (!puzzleLimitActive) throw new Error("RANDOM SOLUTIONS AND PUZZLE LIMIT ARE INCOMPATIBLE");

            // since this step is random it is a bit wacky
            double iterationsMultiplier = Math.E; // larger doubles work more effectively, but this is random chance
            for (int iterations = 0; iterations < (int) (iterationsMultiplier * puzzleLimit); iterations++) {
                recurSolve(puzzle.copy(), 0);
                if (solutions.size() >= puzzleLimit) break;
            }
        }

        // for sorted solutions, one call will do
        else recurSolve(puzzle.copy(), 0); // create puzzle copy to avoid overriding
    }

    private void recurSolve(Puzzle puzzle, int depth) {
        // this is only triggered by the call in the other definition of recurSolve

        if (recursionLimitActive && depth > recursionLimit) return; // recursion limit reached
        if (puzzleLimitActive && solutions.size() >= puzzleLimit) return; // puzzle limit reached
        if (depth > maxRecursion) maxRecursion = depth; // update maxRecursion for stats

        // fully check and update the puzzle first
        do {
            updateInvalid(puzzle);
            if (!checkValid(puzzle)) return; // check if current puzzle arrangement is valid, and return if it is not
        } while (updateByElimination(puzzle) || updateByNecessity(puzzle)); // update puzzle

        // now operate on the updated puzzle
        if (puzzle.isComplete()) {
            addSolution(puzzle); // if the puzzle is fully filled in at this stage, it must be a valid solution
        }

        else {
            int size = puzzle.getSize();
            Square[][] grid = puzzle.getGrid();

            ArrayList<int[]> possibleSquares = new ArrayList<>(); // only used if randomSolutions == true

            // iterate through each square in the grid
            for (int row = 0; row < size*size; row++) {
                for (int col = 0; col < size*size; col++) {
                    Square square = grid[row][col];

                    // differentiate random solutions vs sorted solutions
                    if (square.getValue() == 0) {
                        // SORTED SOLUTIONS
                        if (!randomSolutions) {
                            // only operate on the first Square that has multiple possibilities
                            // try every valid value of the square
                            for (int i : square.getValid()) {
                                Puzzle newPuzzle = puzzle.copy();
                                newPuzzle.getGrid()[row][col].setValue(i); // assume a square is filled in as one possible value and continue from there

                                // recursive call
                                recurSolve(newPuzzle, depth + 1); // keep track of recursion depth and increment
                            }
                            return; // magic return statement
                        }

                        // RANDOM SOLUTIONS
                        else {
                            possibleSquares.add(new int[] {row, col});
                        }
                    }
                }
            }

            // RANDOM SOLUTIONS
            if (randomSolutions && !possibleSquares.isEmpty()) {
                // make a random choice for the square
                int randomIndex = (int) (Math.random() * possibleSquares.size());
                int row = possibleSquares.get(randomIndex)[0], col = possibleSquares.get(randomIndex)[1];

                // make a random choice for the square's value
                ArrayList<Integer> possibleValues = grid[row][col].getValid();
                int randomValue = possibleValues.get((int) (Math.random() * possibleValues.size()));

                // now assume that both random choices were made
                Puzzle newPuzzle = puzzle.copy();
                newPuzzle.getGrid()[row][col].setValue(randomValue);

                // recursive call
                recurSolve(newPuzzle, depth + 1); // keep track of recursion depth and increment
            }
        }
    }


    // Dedicated puzzle checker methods
    private boolean checkValid(Puzzle puzzle) {
        int size = puzzle.getSize();
        Square[][] grid = puzzle.getGrid();

        // iterate and check if each square is valid
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                Square square = grid[row][col];

                // make an ArrayList for possible values in the square
                ArrayList<Integer> possible = square.getValid();

                // if a square cannot be anything, the puzzle is wrong
                if (possible.isEmpty()) return false;

                // if a square already has an assigned value that is invalid, the puzzle is wrong
                if (square.getValue() != 0 && !possible.contains(square.getValue())) return false;
            }
        }
        return true; // default case meaning arrangement is valid
    }

    private void updateInvalid(Puzzle puzzle) {
        // there is another type of update that could weed out some more invalid options, but the algorithm is faster without it

        int size = puzzle.getSize();
        Square[][] grid = puzzle.getGrid();

        // iterate through each square
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                int squareValMinusOne = grid[row][col].getValue()-1;

                // if the square is not empty, let it affect the possibilities of other squares
                // but leave THIS square value unchanged
                if (squareValMinusOne != -1) {
                    // iterate from 1 to size^2 (minus one, for indexing)
                    for (int i = 0; i < size*size; i++) {
                        // for this square, make all values other than itself invalid
                        if (i != squareValMinusOne) grid[row][col].setInvalid(i, true);

                        // cancel other squares in the same row
                        if (i != col) grid[row][i].setInvalid(squareValMinusOne, true);

                        // cancel other squares in the same col
                        if (i != row) grid[i][col].setInvalid(squareValMinusOne, true);

                        // cancel other squares in the same box
                        int macroRow = (row / size) * size, macroCol = (col / size) * size;
                        int boxRow = macroRow + i / size, boxCol = macroCol + i % size;
                        if (boxRow != row && boxCol != col) grid[boxRow][boxCol].setInvalid(squareValMinusOne, true);
                    }
                }
            }
        }
    }

    private boolean updateByElimination(Puzzle puzzle) {
        int size = puzzle.getSize();
        Square[][] grid = puzzle.getGrid();

        boolean actionTaken = false;

        // iterate through all squares
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                Square square = grid[row][col];

                if (square.getValue() != 0) continue; // operate on a square if it is not yet filled in

                ArrayList<Integer> possible = square.getValid(); // get possible values

                // update the grid
                if (possible.size() == 1) {
                    square.setValue(possible.getFirst()); // if only one possible value, it must be that value
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
                // create ArrayLists for each subsection type
                ArrayList<Integer> rowCheck = new ArrayList<>();
                ArrayList<Integer> colCheck = new ArrayList<>();
                ArrayList<Integer> boxCheck = new ArrayList<>();

                // iteration using i and j (not strictly rows and cols in this context)
                // num is the number to check between 1 to size^2 (minus one for iteration)
                for (int j = 0; j < size*size; j++) {
                    // check how many squares in this row have "num" as valid
                    if (!grid[i][j].getInvalid()[num]) rowCheck.add(j);

                    // check how many squares in this col have "num" as valid
                    if (!grid[j][i].getInvalid()[num]) colCheck.add(j);

                    // check how many squares in this box have "num" as valid
                    int macroRow = (i / size) * size, macroCol = (i % size) * size; // box is more complex to iterate through than row and col, so temp values are used
                    int boxRow = macroRow + j / size, boxCol = macroCol + j % size;
                    if (!grid[boxRow][boxCol].getInvalid()[num]) boxCheck.add(j);
                }

                // update the grid

                // if only one square in the row can be "num" then "num" must go in that square
                if (rowCheck.size() == 1 && grid[i][rowCheck.getFirst()].getValue() == 0) {
                    grid[i][rowCheck.getFirst()].setValue(num+1);
                    actionTaken = true;
                }

                // if only one square in the col can be "num" then "num" must go in that square
                if (colCheck.size() == 1 && grid[colCheck.getFirst()][i].getValue() == 0) {
                    grid[colCheck.getFirst()][i].setValue(num+1);
                    actionTaken = true;
                }

                // if only one square in the box can be "num" then "num" must go in that square
                if (boxCheck.size() == 1) {
                    int j = boxCheck.getFirst();
                    int macroRow = (i / size) * size, macroCol = (i % size) * size;
                    int boxRow = macroRow + j / size, boxCol = macroCol + j % size;

                    // use nested if instead of && because the box iteration is more complex
                    if (grid[boxRow][boxCol].getValue() == 0) {
                        grid[boxRow][boxCol].setValue(num+1);
                        actionTaken = true;
                    }
                }

                // there are no spots in a row/col/box for a number, the puzzle is invalid
                if (rowCheck.isEmpty() || colCheck.isEmpty() || boxCheck.isEmpty()) {
                    grid[0][0].setValue(1);
                    grid[0][1].setValue(1);
                    actionTaken = true;
                }
            }
        }
        return actionTaken; // return whether this method changed anything
    }
}