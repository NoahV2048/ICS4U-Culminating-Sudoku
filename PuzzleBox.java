import javax.swing.*;
import java.awt.*;

public class PuzzleBox extends JFrame {
    // learned basics here
    // https://youtu.be/Kmgo00avvEw?si=ZAOYPNNYAb-rIGEX

    public PuzzleBox(int size) {

        // create puzzleBox window
        this.setTitle("Sudoku Solver");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setLayout(new GridLayout(size, size));
        // puzzleBox.setResizable(false);

        // create text input
        JTextField[][] textFields = new JTextField[size*size][size*size];

        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                JTextField newTextField = new JTextField();
                textFields[row][col] = newTextField;
                this.add(newTextField);
            }
        }

        // create label
        JLabel title = new JLabel("SUDOKU SOLVER");
        title.setVerticalAlignment(JLabel.TOP);
        title.setHorizontalAlignment(JLabel.CENTER);
        // puzzleBox.add(title);

        // create image
        // ImageIcon image = new ImageIcon("SudokuSolverLogo.png");
        // puzzleBox.setIconImage(image.getImage());
        this.setVisible(true);
    }
}