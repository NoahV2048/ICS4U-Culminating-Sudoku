import javax.swing.*;
import java.awt.*;

public class SwingPuzzle extends JFrame {
    // learned basics here
    // https://youtu.be/Kmgo00avvEw?si=ZAOYPNNYAb-rIGEX

    int size;
    int pixelSize = 450;
    public SwingSquare[][] squareValues;


    // Constructor
    public SwingPuzzle(int size) {
        this.size = size;
        setTitle("Sudoku Solver");
        setSize(pixelSize, pixelSize * 4/3);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // can be toggled TODO

        // MASTER CONFIGURATION
        JPanel masterPanel = new JPanel();
        add(masterPanel);
        masterPanel.setLayout(new GridLayout(3, 1)); // grid layout
        masterPanel.setSize(pixelSize, pixelSize * 4/3);


        // TOP CONFIGURATION
        JPanel topPanel = new JPanel();
        masterPanel.add(topPanel);
        topPanel.setSize(pixelSize, pixelSize/6);

        // create title at top of program
        JLabel title = new JLabel("SUDOKU SOLVER");
        title.setVerticalAlignment(JLabel.TOP);
        title.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(title);


        // PUZZLE CONFIGURATION

        // make layouts
        GridLayout macroLayout = new GridLayout(size, size, pixelSize/100, pixelSize/100); // macro
        GridLayout microLayout = new GridLayout(size, size, 1, 1); // micro

        // create panel for the whole puzzle
        JPanel puzzlePanel = new JPanel();
        masterPanel.add(puzzlePanel);
        puzzlePanel.setLayout(macroLayout); // grid layout
        puzzlePanel.setSize(pixelSize/size, pixelSize/size);

        // create smaller panels inside the main puzzle panel
        JPanel[][] boxPanels = new JPanel[size][size];
        for (int macroRow = 0; macroRow < size; macroRow++) {
            for (int macroCol = 0; macroCol < size; macroCol++) {
                // create smaller panels
                JPanel boxPanel = new JPanel();
                boxPanel.setLayout(microLayout); // micro grid layout
                boxPanel.setSize(pixelSize/(size*size), pixelSize/(size*size));
                puzzlePanel.add(boxPanel); // add each smaller panel to the larger one
                boxPanels[macroRow][macroCol] = boxPanel;
            }
        }

        // create text input boxes
        squareValues = new SwingSquare[size*size][size*size];

        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                // create a new SwingSquare
                SwingSquare newSwingSquare = new SwingSquare(size);

                // add SwingSquare to array of squareValues and also to the appropriate boxPanel
                squareValues[row][col] = newSwingSquare;

                int macroRow = (row/size), macroCol = (col/size);
                boxPanels[macroRow][macroCol].add(newSwingSquare);
            }
        }


        // BOTTOM CONFIGURATION
        JPanel bottomPanel = new JPanel();
        masterPanel.add(bottomPanel); // TODO problems with master rn
        bottomPanel.setSize(pixelSize, pixelSize/6);


        // create image TODO
        // ImageIcon image = new ImageIcon("SudokuSolverLogo.png");
        // puzzleBox.setIconImage(image.getImage());

        // finally, set the frame visible
        setVisible(true);
    }


    // Getters
    public Square[][] toArray() {
        int[][] tempIntArray = new int[size*size][size*size];

        // iterate through squares
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                // error handling
                // if a square is empty, assume it is zero
                try {
                    tempIntArray[row][col] = squareValues[row][col].getInt();
                }
                catch (Exception e) {
                    tempIntArray[row][col] = 0;
                }
            }
        }

        // turn the temporary int array into a Square array
        return Square.intsToSquares(tempIntArray);
    }
}