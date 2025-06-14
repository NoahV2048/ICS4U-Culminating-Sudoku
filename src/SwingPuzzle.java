package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SwingPuzzle extends JFrame {
    // learned basics here
    // https://youtu.be/Kmgo00avvEw?si=ZAOYPNNYAb-rIGEX

    int size;
    int displayedSolutionIndex = -1;
    boolean outputAsNewFile = false; // hardcoded so toggle here (changes file output)
    JLabel solutionNumber;
    ArrayList<Puzzle> solutions;

    // display settings
    int windowSize = 500;
    int displayRatio = 10;
    public SwingSquare[][] squareValues;


    // Constructor
    public SwingPuzzle(int size) {
        this.size = size;
        int realPuzzleSize = windowSize + (size-2) * (windowSize /100) + (size*size-2);

        // FRAME CONFIGURATION
        setTitle("Sudoku Solver");
        setSize(realPuzzleSize, realPuzzleSize * (displayRatio+2)/displayRatio);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // can be toggled


        // TOP CONFIGURATION
        JPanel topPanel = new JPanel();
        topPanel.setBounds(0, 0, realPuzzleSize, realPuzzleSize/displayRatio);
        add(topPanel);


        // PUZZLE CONFIGURATION

        // make layouts
        GridLayout macroLayout = new GridLayout(size, size, windowSize /100, windowSize /100); // macro
        GridLayout microLayout = new GridLayout(size, size, 1, 1); // micro

        // create panel for the whole puzzle
        JPanel puzzlePanel = new JPanel();
        add(puzzlePanel);
        puzzlePanel.setLayout(macroLayout); // grid layout
        puzzlePanel.setBounds(0, realPuzzleSize/displayRatio, windowSize, windowSize);

        // create smaller panels inside the main puzzle panel
        JPanel[][] boxPanels = new JPanel[size][size];
        for (int macroRow = 0; macroRow < size; macroRow++) {
            for (int macroCol = 0; macroCol < size; macroCol++) {
                // create smaller panels
                JPanel boxPanel = new JPanel();
                boxPanel.setLayout(microLayout); // micro grid layout
                boxPanel.setSize(windowSize /(size*size), windowSize /(size*size));
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
        bottomPanel.setBounds(0, windowSize * (displayRatio+1)/displayRatio, windowSize, windowSize /(2*displayRatio));
        add(bottomPanel);

        // add SOLVE button
        JButton solveButton = new JButton("SOLVE");
        topPanel.add(solveButton); // add to top

        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // solve
                solve();
            }
        } );

        // add RESET button
        JButton resetButton = new JButton("RESET");
        topPanel.add(resetButton); // add to top
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // reset
                reset();
            }
        } );


        // add other nav buttons
        JButton prevButton = new JButton("PREV");
        topPanel.add(prevButton); // add to top
        JButton nextButton = new JButton("NEXT");
        topPanel.add(nextButton); // add to top

        prevButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // try decrementing
                displayedSolutionIndex--;

                try {
                    updateSolutionDisplay();
                } catch (Exception e) {
                    displayedSolutionIndex++;
                }
            }
        } );

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // try incrementing
                displayedSolutionIndex++;

                try {
                    updateSolutionDisplay();
                } catch (Exception e) {
                    displayedSolutionIndex--;
                }
            }
        } );

        // add counter for solution number
        solutionNumber = new JLabel();
        topPanel.add(solutionNumber);

        // finally, set the frame visible
        setVisible(true);
    }


    // Getters
    private Square[][] toArray() {
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


    // Setters
    private void updateArray(Puzzle puzzle) {
        Square[][] grid = puzzle.getGrid();

        // iterate through squares
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                Square square = grid[row][col];
                squareValues[row][col].setText("" + ((square.getValue() == 0) ? "" : square.getValue())); // rewrite the value of the square
            }
        }
    }


    // Helpers
    private void updateSolutionDisplay() {
        updateArray(solutions.get(displayedSolutionIndex));
        solutionNumber.setText("Solution " + (displayedSolutionIndex+1) + "/" + solutions.size());
    }

    private void reset() {
        updateArray(new Puzzle(size));
        solutionNumber.setText("");
    }

    private void solve() {
        // initialize Sudoku solver
        SudokuSolver solver = new SudokuSolver();

        // initialize puzzle object
        Puzzle sud = new Puzzle(toArray()); // first make 2D Square array

        // display unsolved puzzle
        System.out.println("\n\nPUZZLE:\n");
        System.out.println(sud);

        // keep track of time to solve
        long timeStart = System.currentTimeMillis();
        solver.recurSolve(sud);
        long timeEnd = System.currentTimeMillis();

        // print stats
        System.out.println("Recursion limit: " + (solver.getRecursionLimitActive() ? solver.getRecursionLimit() : "no limit"));
        System.out.println("Solution limit: " + (solver.getPuzzleLimitActive() ? solver.getPuzzleLimit() : "no limit"));
        System.out.printf("Time elapsed: %.3f seconds%n", (timeEnd - (double) timeStart) / 1000);
        System.out.println("Max recursive depth: " + solver.getMaxRecursion());

        // see if any solutions were found
        solutions = solver.getSolutions();
        System.out.printf("%n%d SOLUTIONS WERE FOUND%n%n", solutions.size());

        // return if no solutions were found
        if (solutions.isEmpty()) {
            displayedSolutionIndex = -1;
            return;
        }

        // update the SwingPuzzle if there are solutions
        displayedSolutionIndex = 0;
        updateSolutionDisplay();

        // lastly, output solutions to a file
        String fileName;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = now.format(formatter);

        if (outputAsNewFile) fileName = "output/solutions_" + timestamp + ".txt";
        else fileName = "output/mostRecentSolutions.txt";

        // use PrintWriter
        try (PrintWriter writer = new PrintWriter(fileName))  {
            writer.println("=== Sudoku Solutions ===");
            writer.println("Number of solutions: " + solutions.size());
            writer.println("Generated on: " + now + "\n\n");

            for (Puzzle solution : solutions) {
                writer.println(solution);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}