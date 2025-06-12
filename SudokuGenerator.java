// import java.util.Random;
//
// public class SudokuGenerator {
//     private final int size;
//     private Puzzle puzzle;
//
//     public SudokuGenerator(int size) {
//         this.size = size;
//     }
//
//     public Puzzle getNewPuzzle() {
//         return puzzle;
//     }
//
//     public void generateNew(int clues, String difficulty) { // TODO could be enum for puzzle difficulties
//         // https://en.wikipedia.org/wiki/Mathematics_of_Sudoku
//         // stats for 9x9 puzzles:
//         // smallest minimal puzzle: 17 clues
//         // largest minimal puzzle: 40 clues
//         // every solved puzzle can be reduced to 21 clues or fewer
//         // smallest ambiguous puzzle: 0 clues
//         // largest ambiguous puzzle: 77 clues
//
//         puzzle = new Puzzle(size);
//         SudokuSolver solver = new SudokuSolver();
//
//         do {
//             for (int clueNum = 0; clueNum < clues; clueNum++) {
//                 // add random clue by iterating
//                 puzzle.checkValid();
//                 addClue(clueNum);
//             }
//             solver.recurSolve(puzzle.getGrid());
//
//             while (SudokuSolver.solutions.size() > 1) {
//                 addClue(0); // TODO TODO TODO make sure it's more!
//                 solver.recurSolve(puzzle.getGrid());
//             }
//         } while (SudokuSolver.solutions.size() != 1);
//     }
//
//     private void addClue(int clueNum) {
//         Random random = new Random();
//
//         int index = random.nextInt(size*size*size*size - clueNum);
//
//         for (int row = 0; row < size*size; row++) {
//             for (int col = 0; col < size*size; col++) {
//                 if (puzzle.getGrid()[row][col].getValue() == 0) {
//                     if (index == 0) {
//                         // find possible values
//
//                         puzzle.getGrid()[row][col].setValue((random.nextInt(size*size) + 1));
//                         return;
//                     }
//                     else index--;
//                 }
//             }
//         }
//     }
// }