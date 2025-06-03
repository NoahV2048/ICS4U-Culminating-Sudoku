import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class SwingTest {
    public static void main(String[] args) {
        // create window
        JFrame puzzleBox = new JFrame();
        puzzleBox.setTitle("Sudoku");
        puzzleBox.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        puzzleBox.setResizable(false);
        puzzleBox.setSize(500, 500);
        puzzleBox.setVisible(true);

       // ImageIcon image = new ImageIcon("SudokuSolverLogo");
       // puzzleBox.setIconImage(image.getImage());
    }
}