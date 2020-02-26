/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class Board {
    private final int[][] board;
    private final int N;
    private int[] pivotCoords;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.board = tiles;
        this.N = tiles.length;

        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.board[i][j] == 0)
                    pivotCoords = new int[] {i, j};
            }
        }
    }

    // string representation of this board
    public String toString()
    {
        String representation = this.N + "\n";

        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {

                if (j != this.N-1)  representation += board[i][j] + " ";
                else               representation += board[i][j];
            }
            representation += "\n";
        }

        return representation;
    }

    public int dimension()
    {
        return this.N;
    }

    // number of tiles out of place
    public int hamming()
    {
        int dist = 0;

        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {

                if (i == this.N-1 && j == this.N-1) {
                    break;
                } else {
                    if ((i * this.N + (j+1)) != board[i][j]) dist++;
                }
            }
        }

        return dist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        int dist = 0;
        double correctRow;
        double correctCol;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                    if (board[i][j] == 0) {
                        continue;
                    } else {
                        correctRow = Math.ceil((double) board[i][j] / N) - 1;

                        if (board[i][j] % N == 0) correctCol = N-1;
                        else                      correctCol = (board[i][j] % N) - 1;
                    }

                    //System.out.println();
                    //System.out.println(board[i][j]);
                    // System.out.println(correctRow);
                    // System.out.println(correctCol);
                    // System.out.println(Math.abs(i - correctRow) + Math.abs(j - correctCol));

                    dist += Math.abs(i - correctRow) + Math.abs(j - correctCol);
                }
            }

        return dist;
    }


    public boolean isGoal()
    {
        if (this.hamming() == 0) return true;
        return false;
    }


    public boolean equals(Object y)
    {
        if (this == y)
            return true;
        else if (y == null)
            return false;
        else if (this.getClass() != y.getClass())
            return false;

        Board other = (Board) y;


        if (this.dimension() == other.dimension()) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (this.board[i][j] != other.board[i][j])
                        return false;
                }
            }

            return true;
        }

        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {

        int nNeighbors = 0;
        int[][] swapCoords = new int[4][2];

        int i = pivotCoords[0];
        int j = pivotCoords[1];

        try {
            int up = board[i-1][j];
            swapCoords[nNeighbors++] = new int[] {i-1, j};
        } catch(IndexOutOfBoundsException e) { }

        try {
            int right = board[i][j+1];
            swapCoords[nNeighbors++] = new int[] {i, j+1};
        } catch (IndexOutOfBoundsException e) { }

        try {
            int down = board[i+1][j];
            swapCoords[nNeighbors++] = new int[] {i+1, j};
        } catch (IndexOutOfBoundsException e) { }

        try {
            int left = board[i][j-1];
            swapCoords[nNeighbors++] = new int[] {i, j-1};
        } catch (IndexOutOfBoundsException e) { }


        Queue<Board> neighbors = new Queue<Board>();

        for (int k = 0; k < nNeighbors; k++) {
            neighbors.enqueue(new Board(swapTiles(pivotCoords, swapCoords[k])));
        }

        return neighbors;
    }

    /*
        Parameters:
            - coord1 & coord2: [row, col] coordinates of each tile

        Returns:
            - this.grid with swapped tiles at given coords.
    */
    private int[][] swapTiles(int[] coord1, int[] coord2) {

        int[][] swapped = copyBoard();

        int tmp = swapped[coord1[0]][coord1[1]];

        swapped[coord1[0]][coord1[1]] = swapped[coord2[0]][coord2[1]];
        swapped[coord2[0]][coord2[1]] = tmp;

        return swapped;
    }


    private int[][] copyBoard()
    {
        int[][] copy = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                copy[i][j] = this.board[i][j];
            }
        }

        return copy;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        int x0 = 0;
        int y0 = 0;
        int x1 = 0;
        int y1 = 1;

        if (board[x0][y0] == 0) {
            x0++;
        } else if (board[x1][y1] == 0) {
            x1++;
        }

        int[] coord0 = {x0, y0};
        int[] coord1 = {x1, y1};

        return new Board(swapTiles(coord0, coord1));
    }

    // unit testing (not graded)
    public static void main(String[] args)
    {
        String[] filenames = {"puzzle3x3-14.txt",
                              "puzzle3x3-26.txt",
                              "puzzle4x4-18.txt",
                              "puzzle4x4-40.txt",
                              "puzzle27.txt"};

        // Manhattan Distance tests
        for (String filename : filenames) {

            System.out.println(filename);
            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            Board testBoard = new Board(tiles);
            int manhattan = testBoard.manhattan();
            int hamming   = testBoard.hamming();

            if (filename == "puzzle3x3-14.txt") {

                System.out.println(testBoard.toString());
                System.out.println(testBoard.twin().toString());

                assert manhattan == 14 : "M: expected 14, got " + manhattan;
                assert   hamming == 8 : "H: expected 8, got " + hamming;
            } else if (filename == "puzzle3x3-26.txt") {
                assert manhattan == 18 : "M: expected 18, got " + manhattan;
                assert   hamming == 8 : "H: expected 8, got " + hamming;
            } else if (filename == "puzzle4x4-18.txt") {
                assert manhattan == 10 : "M: expected 10, got " + manhattan;
                assert   hamming == 7 : "H: expected 7, got " + hamming;
            } else if (filename == "puzzle4x4-40.txt") {
                assert manhattan == 28 : "M: expected 28, got " + manhattan;
                assert   hamming == 13 : "H: expected 13, got " + hamming;
            } else if (filename == "puzzle27.txt") {
                assert manhattan == 17 : "M: expected 17, got " + manhattan;
                assert   hamming == 7 : "H: expected 7, got " + hamming;
            }
        }




    }
}

