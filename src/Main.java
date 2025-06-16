/**
 * Sudoku Solver Program
 * Uses Swing interface to solve a user-inputted Sudoku puzzle
 * This is my own work, with referenced sources clearly indicated.
 * Loosely based on this site: https://sudokuspoiler.com/sudoku/sudoku9
 *
 * @author  Noah Verdon
 * @version 1.2
 * @since   2025-06-13
 */

package src;

public class Main {
    static SwingPuzzle swingPuzzle;

    public static void main(String[] args) {
        swingPuzzle = new SwingPuzzle(3); // you can change size to be any natural number (but gets exponentially slower, so 5+ is not recommended)
        SudokuSolver.setSolutionLimit(20); // dangerous to put this very high
        SudokuSolver.setRandomSolutions(false); // works better for larger puzzle limits, does not work well above 9x9 grids
        SudokuSolver.setPuzzleLimitActive(true); // dangerous to set false
    }
}