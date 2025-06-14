/**
 * Sudoku Solver Program
 * Uses Swing interface to solve a user-inputted Sudoku puzzle
 * This is my own work, with referenced sources clearly indicated.
 * Loosely based on this site: https://sudokuspoiler.com/sudoku/sudoku9
 *
 * @author  Noah Verdon
 * @version 1.0
 * @since   2025-06-13
 */

package src;

public class Main {
    static SwingPuzzle swingPuzzle;

    public static void main(String[] args) {
        swingPuzzle = new SwingPuzzle(3); // you can change SIZE to be any natural number! (but gets exponentially slower)
        SudokuSolver.setSolutionLimit(1000); // dangerous to put this very high
    }
}