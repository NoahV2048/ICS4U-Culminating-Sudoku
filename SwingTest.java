import javax.swing.*;

public class SwingTest {
    public static void main(String[] args) {
        // learned basics here
        // https://www.youtube.com/playlist?list=PLNb_-KeTZieW3DNI2aLOAYBnjx29CN8dT

        // create text input
        JTextField yo = new JTextField();

        // create label
        JLabel title = new JLabel("SUDOKU SOLVER");
        title.setVerticalAlignment(JLabel.TOP);
        title.setHorizontalAlignment(JLabel.CENTER);

        // create image
        // ImageIcon image = new ImageIcon("SudokuSolverLogo.png");

        // create window
        JFrame puzzleBox = new JFrame();
        puzzleBox.setTitle("Sudoku");
        puzzleBox.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // puzzleBox.setResizable(false);
        puzzleBox.setSize(500, 500);
        puzzleBox.setVisible(true);

        puzzleBox.add(title);
        puzzleBox.add(yo);
        // puzzleBox.setIconImage(image.getImage());
    }
}