import java.util.ArrayList;
import java.util.Arrays;

public class Puzzle {
    private int[][] grid;
    private int size;
    private boolean[][][] invalid;

    public Puzzle(int size) {
        this.size = size;
        grid = new int[size*size][size*size];
        invalid = new boolean[size*size][size*size][size*size];
    }

    public Puzzle(int[][] grid) {
        this.size = (int) Math.sqrt(grid.length);
        this.grid = grid; // reference!
        invalid = new boolean[size*size][size*size][size*size];

        // check for valid array
        for (int[] row : grid) {
            if (row.length != grid.length) throw new Error("Bad grid");
        }
    }

    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                if (grid[row][col] > 9) output.append((char) (55 + grid[row][col])); // output letter instead of number
                else output.append(grid[row][col]);

                output.append(" ");
                if (col % size == size-1) output.append(" ");
            }
            output.append("\n");
            if (row % size == size-1) output.append("\n");
        }
        return output.toString();
    }

    public int getSize() {
        return size;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int[][] gridCopy() {
        // doesn't copy the "invalid" array, but not necessary anyway

        // all this because Arrays.copyOf still uses references for 2D
        int[][] newGrid = new int[size*size][size*size];
        for (int row = 0; row < size*size; row++) {
            newGrid[row] = Arrays.copyOf(grid[row], size*size);
        }
        return newGrid;
    }

    public ArrayList<Integer> getInvalid(int row, int col) {
        ArrayList<Integer> invalidNums = new ArrayList<>();

        for (int i = 0; i < size*size; i++) {
            if (!invalid[row][col][i]) invalidNums.add(i+1);
        }
        return invalidNums;
    }

    public String stringInvalid() {
        StringBuilder output = new StringBuilder();

        for (int row = 0; row < size*size; row++) {
            for (int miniRow = 0; miniRow < size; miniRow++) {
                for (int col = 0; col < size*size; col++) {
                    for (int miniCol = 0; miniCol < size; miniCol++) {
                        int invIndex = miniRow * size + miniCol;

                        String print = ((invalid[row][col][invIndex] ? "-" : (invIndex+1)) + " ");
                        output.append(print);
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

    public boolean checkValid() {
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                int square = grid[row][col] - 1;

                if (square != -1) {
                    for (int i = 0; i < size*size; i++) {
                        // make all other values invalid but leave square value unchanged
                        // this helps find invalid squares

                        // for just this square
                        if (i != square) invalid[row][col][i] = true;

                        // row cancel
                        if (i != col) invalid[row][i][square] = true;

                        // col cancel
                        if (i != row) invalid[i][col][square] = true;

                        // box cancel
                        int refy = (row / size) * size, refx = (col / size) * size;
                        if ((refy + i / size) != row && (refx + i % size) != col) {
                            invalid[refy + i / size][refx + i % size][square] = true;
                        }
                    }
                }
            }
        }

        // possibilities restricting other possibilities in BOXES

        // example where 1 must be in either spot, blocking off the row
        // 11->
        // 0000
        // 0000
        // 0000

        // TODO: THIS WHOLE STEP MIGHT ACTUALLY MAKE THINGS SLOWER

        // for (int boxRow = 0; boxRow < size; boxRow++) {
        //     for (int boxCol = 0; boxCol < size; boxCol++) {
        //         int[][] rows = new int[size*size][size];
        //         int[][] cols = new int[size*size][size];

        //         for (int miniRow = 0; miniRow < size; miniRow++) {
        //             for (int miniCol = 0; miniCol < size; miniCol++) {
        //                 for (int i = 0; i < size*size; i++) {
        //                     if (!invalid[boxRow*size+miniRow][boxCol*size+miniCol][i]) {
        //                         rows[i][miniRow]++;
        //                         cols[i][miniCol]++;
        //                     }
        //                 }
        //             }
        //         }

        //         for (int i = 0; i < size*size; i++) {
        //             int[] rowIndices = topTwo(rows[i]); // selection sort-ish operation
        //             int[] colIndices = topTwo(cols[i]); // returns [val1, val2, ind1]

        //             // row conditions
        //             if (rowIndices[0] > 1 && rowIndices[1] == 0) { // means one row has multiple of a number and others have none
        //                 int rowToUpdate = rowIndices[2] + boxRow*size;

        //                 for (int shift = 1; shift < size*(size-1); shift++) {
        //                     invalid[rowToUpdate][((boxCol+1)*size + shift) % size*size][i] = false; // update
        //                 }
        //             }

        //             // col conditions
        //             if (colIndices[0] > 1 && colIndices[1] == 0) { // means one col has multiple of a number and others have none
        //                 int colToUpdate = colIndices[2] + boxCol*size;

        //                 for (int shift = 1; shift < size*(size-1); shift++) {
        //                     invalid[((boxRow+1)*size + shift) % size*size][colToUpdate][i] = false; // update
        //                 }
        //             }
        //         }
        //     }
        // }

        for (int i = 0; i < size*size; i++) {
            for (int num = 0; num < size*size; num++) {
                ArrayList<Integer> rowCheck = new ArrayList<>();
                ArrayList<Integer> colCheck = new ArrayList<>();
                ArrayList<Integer> boxCheck = new ArrayList<>();

                for (int j = 0; j < size*size; j++) {
                    // row
                    if (!invalid[i][j][num]) rowCheck.add(j);

                    // col
                    if (!invalid[j][i][num]) colCheck.add(j);

                    // box
                    int refy = (i / size) * size, refx = (i % size) * size;
                }
            }
        } // TODO something here?

        // solvable check
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                // check if unsolvable
                boolean squareContradiction = true;

                // make ArrayList for possible values
                ArrayList<Integer> possible = new ArrayList<>();
                for (int i = 0; i < size*size; i++) {
                    if (!invalid[row][col][i]) {
                        possible.add(i+1);

                        if (i+1 == grid[row][col]) squareContradiction = false; // row can be what it already is (good!)
                    }
                }

                // obvious unsolvable case
                if (possible.size() == 0) return false;

                // whole arrangement is invalid because number disagrees with possible values
                if (grid[row][col] != 0 && squareContradiction) return false;
            }
        }

        return true; // default case meaning arrangement is valid
    }

    public boolean isComplete() {
        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                if (grid[row][col] == 0) return false;
            }
        }
        return true;
    }

    public boolean updateByElim() {
        boolean actionTaken = false;

        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                if (grid[row][col] != 0) continue;

                ArrayList<Integer> possible = new ArrayList<>();
                for (int i = 0; i < size*size; i++) {
                    if (!invalid[row][col][i]) possible.add(i+1);
                }

                // UPDATE!
                if (possible.size() == 1) {
                    grid[row][col] = possible.get(0);
                    actionTaken = true;
                }
            }
        }
        return actionTaken;
    }

    public boolean updateByNecessity() {
        boolean actionTaken = false;

        for (int i = 0; i < size*size; i++) {
            for (int num = 0; num < size*size; num++) {
                ArrayList<Integer> rowCheck = new ArrayList<>();
                ArrayList<Integer> colCheck = new ArrayList<>();
                ArrayList<Integer> boxCheck = new ArrayList<>();

                for (int j = 0; j < size*size; j++) {
                    // row
                    if (!invalid[i][j][num]) rowCheck.add(j);

                    // col
                    if (!invalid[j][i][num]) colCheck.add(j);

                    // box
                    int refy = (i / size) * size, refx = (i % size) * size;
                    if (!invalid[refy + j / size][refx + j % size][num]) boxCheck.add(j);
                }

                // UPDATES!
                if (rowCheck.size() == 1 && grid[i][rowCheck.get(0)] == 0) {
                    grid[i][rowCheck.get(0)] = num+1;
                    actionTaken = true;
                }
                if (colCheck.size() == 1 && grid[colCheck.get(0)][i] == 0) {
                    grid[colCheck.get(0)][i] = num+1;
                    actionTaken = true;
                }
                if (boxCheck.size() == 1) {
                    int j = boxCheck.get(0);
                    int refy = (i / size) * size, refx = (i % size) * size;

                    if (grid[refy + j / size][refx + j % size] == 0) {
                        grid[refy + j / size][refx + j % size] = num+1;
                        actionTaken = true;
                    }
                }
            }
        }
        return actionTaken;
    }

    public boolean equals(Puzzle compare) {
        if (size != compare.size) return false;

        for (int row = 0; row < size*size; row++) {
            for (int col = 0; col < size*size; col++) {
                if (grid[row][col] != compare.grid[row][col]) return false;
            }
        }
        return true;
    }

    public static int[] topTwo(int[] list) {
        if (list.length == 0) return new int[] {-1};

        // modified selection sort algorithm
        int maxVal1 = list[0], maxVal2 = list[0], ind = 0;

        for (int i = 0; i < list.length; i++) {
            if (list[i] > maxVal1) {
                maxVal1 = list[i];
                ind = i;
            }
        }

        for (int i : list) {
            if (i > maxVal2 && i <= maxVal1) maxVal2 = i;
        }

        return new int[] {maxVal1, maxVal2, ind};
    }
}