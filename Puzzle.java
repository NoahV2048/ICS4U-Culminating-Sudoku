import java.util.Arrays;

public class Puzzle {
    private final int size; // keep grid size final
    private final Square[][] grid;


    // Constructor methods
    public Puzzle(int size) {
        this.size = size;
        grid = new Square[size*size][size*size];

        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                grid[row][col] = new Square(size);
            }
        }
    }

    public Puzzle(Square[][] grid) {
        // option to create a puzzle by passing a grid
        this.size = (int) Math.sqrt(grid.length);

        // make a copy of the grid to avoid referencing issues
        Square[][] gridCopy = new Square[size*size][size*size];
        for (int row = 0; row < size*size; row++) {
            // loop to avoid shallow copies
            // https://www.geeksforgeeks.org/arrays-copyof-in-java-with-examples/
            gridCopy[row] = Arrays.copyOf(grid[row], size*size);
        }

        this.grid = gridCopy;

        // check if array is not a square
        for (Square[] row : grid) {
            if (row.length != grid.length) throw new Error("Bad grid");
        }
    }


    // Getter methods
    public int getSize() {
        return size;
    }

    public Square[][] getGrid() {
        // returns reference type but this is intended for mutability
        return grid;
    }


    // Helper methods
    public Puzzle copy() {
        return new Puzzle(grid);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                Square square = grid[row][col];

                if (square.getValue() > 9) output.append((char) (55 + square.getValue())); // output letter instead if number is more than one digit
                else output.append(square.getValue());

                output.append(" ");
                if (col % size == size-1) output.append(" ");
            }
            output.append("\n");
            if (row % size == size-1) output.append("\n");
        }
        return output.toString();
    }

    public String invalidToString() {
        // this is mainly for debugging, so it may have no usages
        StringBuilder output = new StringBuilder();

        for (int row = 0; row < size*size; row++) {
            for (int miniRow = 0; miniRow < size; miniRow++) {
                for (int col = 0; col < size*size; col++) {
                    for (int miniCol = 0; miniCol < size; miniCol++) {
                        int invIndex = miniRow * size + miniCol;

                        String miniNum = ((grid[row][col].getInvalid()[invIndex] ? "-" : (invIndex+1)) + " ");
                        output.append(miniNum);
                    }
                    output.append(" ");
                    if (col % size == size-1) output.append(" ");
                }
                output.append("\n");
                if (miniRow % size == size-1) output.append(" ");
            }
            output.append("\n");
            if (row % size == size-1) output.append("\n");
        }
        return output.toString();
    }

    public boolean isComplete() {
        // check for a full puzzle
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                if (grid[row][col].getValue() == 0) return false;
            }
        }
        return true;
    }

    public boolean equals(Puzzle otherPuzzle) {
        // check if two puzzles are the same
        if (size != otherPuzzle.size) return false;

        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                if (grid[row][col].getValue() != otherPuzzle.grid[row][col].getValue()) return false;
            }
        }
        return true;
    }
}