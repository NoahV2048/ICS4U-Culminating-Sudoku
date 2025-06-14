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
    ArrayList<Puzzle> solutions;

    // display settings
    int windowWidth = 500;
    int displayRatio = 10;
    public SwingSquare[][] squareValues;


    // Constructor
    public SwingPuzzle(int size) {
        this.size = size;
        int realPuzzleSize = windowWidth + (size-2) * (windowWidth /100) + (size*size-2);

        // FRAME CONFIGURATION
        setTitle("Sudoku Solver");
        setSize(realPuzzleSize, realPuzzleSize * (displayRatio+2)/displayRatio);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // can be toggled


        // TOP CONFIGURATION
        JPanel topPanel = new JPanel();
        topPanel.setBounds(0, 0, realPuzzleSize, realPuzzleSize/displayRatio);
        add(topPanel);

        // create title at top of program
        JLabel title = new JLabel("SUDOKU SOLVER");
        title.setVerticalAlignment(JLabel.TOP);
        title.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(title);


        // PUZZLE CONFIGURATION

        // make layouts
        GridLayout macroLayout = new GridLayout(size, size, windowWidth/100, windowWidth/100); // macro
        GridLayout microLayout = new GridLayout(size, size, 1, 1); // micro

        // create panel for the whole puzzle
        JPanel puzzlePanel = new JPanel();
        add(puzzlePanel);
        puzzlePanel.setLayout(macroLayout); // grid layout
        puzzlePanel.setBounds(0, realPuzzleSize/displayRatio, windowWidth, windowWidth);

        // create smaller panels inside the main puzzle panel
        JPanel[][] boxPanels = new JPanel[size][size];
        for (int macroRow = 0; macroRow < size; macroRow++) {
            for (int macroCol = 0; macroCol < size; macroCol++) {
                // create smaller panels
                JPanel boxPanel = new JPanel();
                boxPanel.setLayout(microLayout); // micro grid layout
                boxPanel.setSize(windowWidth /(size*size), windowWidth /(size*size));
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
        bottomPanel.setBounds(0, windowWidth * (displayRatio+1)/displayRatio, windowWidth, windowWidth /(2*displayRatio));
        add(bottomPanel);

        // add SOLVE button
        JButton solveButton = new JButton("SOLVE");
        topPanel.add(solveButton); // add to top

        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // solve
                solve();

                // // output solutions to a file
                // LocalDateTime now = LocalDateTime.now();
                // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                // String timestamp = now.format(formatter);
                // String reportFileName = "output/solutions_" + timestamp + ".txt";
                //
                // PrintWriter writer = null;
                // try {
                //     writer = new PrintWriter(reportFileName);
                //     writer.println("=== Sudoku Solutions ===");
                //     writer.println("Generated on: " + now);
                //
                //     for (Puzzle solution : solutions) {
                //         writer.println(solution);
                //     }
                // } catch (FileNotFoundException e) {
                //     throw new RuntimeException(e);
                // }
                // finally {
                //     if (writer == null) {
                //         writer.close();
                //     }
                // }
            }
        } );

        // add RESET button
        JButton resetButton = new JButton("RESET");
        topPanel.add(resetButton); // add to top
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // solve
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
                displayedSolutionIndex--;

                try {
                    updateArray(solutions.get(displayedSolutionIndex));
                }
                catch (Exception e) {
                    displayedSolutionIndex++;
                }
            }
        } );

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                displayedSolutionIndex++;

                try {
                    updateArray(solutions.get(displayedSolutionIndex));
                }
                catch (Exception e) {
                    displayedSolutionIndex--;
                }
            }
        } );

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


    // Setters
    public void updateArray(Puzzle puzzle) {
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
    public void reset() {
        updateArray(new Puzzle(size));
    }

    public void solve() {
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

        // print up to the first ten solutions
        solutions = solver.getSolutions();
        System.out.printf("%nSOLUTIONS (%d):%n%n", solutions.size());

        if (solutions.size() <= 10) {
            for (Puzzle solution : solutions) {
                System.out.println(solution);
            }
        }

        // finally, update the SwingPuzzle if there are solutions
        if (solutions.isEmpty()) {
            displayedSolutionIndex = -1;
            return;
        }

        displayedSolutionIndex = 0;
        updateArray(solutions.getFirst());
    }
}