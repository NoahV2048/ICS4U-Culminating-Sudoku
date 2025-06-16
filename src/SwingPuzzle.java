package src;

import javax.swing.*;
import java.awt.*;
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
    JLabel adaptiveText;
    JLabel adaptiveTextLower;
    ArrayList<Puzzle> solutions;

    // display settings
    int puzzleSize = 500;
    int displayRatio = 10;
    public SwingSquare[][] squareValues;


    // Constructor
    public SwingPuzzle(int size) {
        if (size <= 1) size = 3; // prevent error, and 1x1 grid makes no sense

        this.size = size;
        int realPuzzleSize = puzzleSize;


        // FRAME CONFIGURATION
        setTitle("Sudoku Solver");
        setSize(realPuzzleSize, 5 + realPuzzleSize * (displayRatio+2)/displayRatio);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(0, 5));
        setResizable(false); // can be toggled


        // BUTTON PANEL CONFIGURATION
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 2));
        buttonPanel.setPreferredSize(new Dimension(puzzleSize/displayRatio, puzzleSize/displayRatio));

        JPanel buttonPanel1 = new JPanel(), buttonPanel2 = new JPanel();

        for (JPanel panel : new JPanel[] {buttonPanel1, buttonPanel2}) {
            buttonPanel.add(panel);
            panel.setLayout(new GridLayout(1, 2, 2, 0));
        }


        // TEXT PANEL CONFIGURATION
        JPanel textPanel = new JPanel();
        add(textPanel, BorderLayout.NORTH);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.PAGE_AXIS)); // set alignment to page axis
        textPanel.setPreferredSize(new Dimension(puzzleSize/displayRatio, puzzleSize/displayRatio));


        // PUZZLE PANEL CONFIGURATION

        // make grid layouts
        GridLayout macroLayout = new GridLayout(size, size, puzzleSize/50, puzzleSize/50); // macro
        GridLayout microLayout = new GridLayout(size, size, 1, 1); // micro

        // create panel for the whole puzzle
        JPanel puzzlePanel = new JPanel();
        add(puzzlePanel, BorderLayout.CENTER);
        puzzlePanel.setLayout(macroLayout); // grid layout

        // create smaller panels inside the main puzzle panel
        JPanel[][] boxPanels = new JPanel[size][size];
        for (int macroRow = 0; macroRow < size; macroRow++) {
            for (int macroCol = 0; macroCol < size; macroCol++) {
                // create smaller panels
                JPanel boxPanel = new JPanel();
                boxPanel.setLayout(microLayout); // micro grid layout
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


        // TEXT CONFIGURATION
        adaptiveText = new JLabel("Please Enter a Sudoku Puzzle to Solve");
        adaptiveText.setFont(new Font("Dialog", Font.BOLD, 18));
        textPanel.add(adaptiveText); // add to text panel
        adaptiveText.setAlignmentX(CENTER_ALIGNMENT); // center align

        adaptiveTextLower = new JLabel();
        adaptiveTextLower.setFont(new Font("Dialog", Font.PLAIN, 12));
        textPanel.add(adaptiveTextLower); // add to text panel
        adaptiveTextLower.setAlignmentX(CENTER_ALIGNMENT); // center align


        // BUTTON CONFIGURATIONS
        Dimension buttonSize = new Dimension(50, puzzleSize/(3*displayRatio));

        // add SOLVE and UNSOLVE button
        JButton solveButton = new JButton("SOLVE");
        solveButton.setPreferredSize(buttonSize);
        buttonPanel1.add(solveButton); // add to button panel

        solveButton.addActionListener(_ -> {
            // SOLVE
            if (solveButton.getText().equals("SOLVE")) {
                adaptiveText.setText("Solving...");

                // new anonymous ActionListener for a timer with no delay
                Timer timer = new Timer(0 , _ -> {
                    solve();
                    solveButton.setText("UNSOLVE");
                });
                timer.setRepeats(false);
                timer.start();
            }

            // UNSOLVE
            else {
                unsolve();
                solveButton.setText("SOLVE");
            }
        });


        // add RESET button
        JButton resetButton = new JButton("RESET");
        resetButton.setPreferredSize(buttonSize);
        buttonPanel1.add(resetButton); // add to button panel

        resetButton.addActionListener(_ -> {
            reset();
            solveButton.setText("SOLVE");
        });


        // add PREV and NEXT buttons for navigation
        JButton prevButton = new JButton("PREV");
        prevButton.setPreferredSize(buttonSize);
        buttonPanel2.add(prevButton); // add to button panel

        JButton nextButton = new JButton("NEXT");
        nextButton.setPreferredSize(buttonSize);
        buttonPanel2.add(nextButton); // add to button panel

        prevButton.addActionListener(_ -> {
            // try decrementing
            displayedSolutionIndex--;

            try {
                updateSolutionDisplay();
            } catch (Exception e) {
                displayedSolutionIndex++;
            }
        });

        nextButton.addActionListener(_ -> {
            // try incrementing
            displayedSolutionIndex++;

            try {
                updateSolutionDisplay();
            } catch (Exception e) {
                displayedSolutionIndex--;
            }
        });


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
                SwingSquare swingSquare = squareValues[row][col];

                // if a bold square is empty, set it plain
                if (swingSquare.isBold()) {
                    try {
                        swingSquare.getInt();
                    }
                    catch (Exception e) {
                        swingSquare.reset(); // if an int can't be retrieved, the bold cell is empty
                    }
                }

                // if the value is bold and filled in, do not update it
                if (!swingSquare.isBold()) {
                    Square square = grid[row][col];
                    swingSquare.setText("" + ((square.getValue() == 0) ? "" : square.getValue())); // rewrite the value of the square
                }
            }
        }
    }

    private void resetArray() {
        // iterate through squares
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                squareValues[row][col].reset(); // reset the value of the swingSquare
            }
        }
    }


    // Helpers
    private void setMutable(boolean bool) {
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                SwingSquare swingSquare = squareValues[row][col];
                Font font = swingSquare.getFont();
                swingSquare.setEditable(bool); // make all Squares mutable or not
                swingSquare.setFont(font); // this prevents fonts being reset in the method call above
            }
        }
    }

    private void updateSolutionDisplay() {
        updateArray(solutions.get(displayedSolutionIndex));
        adaptiveText.setText("Displaying Solution " + (displayedSolutionIndex+1) + "/" + solutions.size());
        adaptiveTextLower.setText(String.format("Random Puzzles: %s    Puzzle Limit: %s",
                SudokuSolver.getRandomSolutionsActive() ? "Active" : "Inactive",
                SudokuSolver.getPuzzleLimitActive() ? SudokuSolver.getPuzzleLimit() : "Inactive"));
    }

    private void reset() {
        resetArray();
        adaptiveText.setText("Please Enter a Sudoku Puzzle to Solve");
        adaptiveTextLower.setText("");
        displayedSolutionIndex = -2;
        setMutable(true);
    }

    private void unsolve() {
        updateArray(new Puzzle(size));
        adaptiveText.setText("Please Enter a Sudoku Puzzle to Solve");
        adaptiveTextLower.setText("");
        setMutable(true);
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

        // print stats (only in terminal)
        System.out.println("Recursion limit: " + (SudokuSolver.getRecursionLimitActive() ? SudokuSolver.getRecursionLimit() : "no limit"));
        System.out.println("Solution limit: " + (SudokuSolver.getPuzzleLimitActive() ? SudokuSolver.getPuzzleLimit() : "no limit"));
        System.out.printf("Time elapsed: %.3f seconds%n", (timeEnd - (double) timeStart) / 1000);
        System.out.println("Max recursive depth: " + solver.getMaxRecursion());

        // make SwingSquares immutable
        setMutable(false);

        // see if any solutions were found
        solutions = solver.getSolutions();
        System.out.printf("%n%d SOLUTIONS WERE FOUND%n%n", solutions.size());

        // return if no solutions were found
        if (solutions.isEmpty()) {
            displayedSolutionIndex = -1;
            adaptiveText.setText("NO SOLUTIONS FOUND");
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