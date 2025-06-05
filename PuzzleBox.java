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
        for (int i = 0; i < size*size; i++) {
            this.add(new JTextField());
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
