import java.util.ArrayList;

public class Square {
    private final boolean[] invalid;
    private int value;


    // Constructor
    public Square(int size) {
        invalid = new boolean[size*size];
        value = 0;
    }


    // Getter Methods
    public int getValue() {
        return value;
    }

    public boolean[] getInvalid() {
        return invalid;
    }

    public ArrayList<Integer> getValid() {
        ArrayList<Integer> valid = new ArrayList<>();

        // iterate and add valid values to an ArrayList
        for (int i = 0; i < invalid.length; i++) {
            if (!invalid[i]) valid.add(i+1);
        }

        return valid;
    }


    // Setter Methods
    public void setValue(int num) {
        value = num;
    }

    public void setInvalid(int index, boolean isInvalid) {
        invalid[index] = isInvalid;
    }


    // Helper methods
    public static Square[][] intsToSquares(int[][] intGrid) {
        int size = (int) Math.sqrt(intGrid.length);
        Square[][] newSquareGrid = new Square[size*size][size*size];

        // change each cell from int to Square object
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                newSquareGrid[row][col] = new Square(size);
                newSquareGrid[row][col].setValue(intGrid[row][col]);
            }
        }
        return newSquareGrid;
    }
}