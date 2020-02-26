import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int n;
    private int nOpen = 0;
    private boolean[] fullState;
    private boolean[] openState;
    private boolean doesPercolate;
    private WeightedQuickUnionUF connections;
    private static String[] CONTIGSQ = {"up", "right", "bottom", "left"};
    private static int NCONTIGSQ = 4;

    public Percolation(int n)
    {

        if (n <= 0) {
            throw new IllegalArgumentException("n must be a positive integer");
        }

        this.n = n;
        this.fullState = new boolean[n * n];
        this.openState = new boolean[n * n];
        this.connections = new WeightedQuickUnionUF(n * n);

        for (int i = 0; i < n * n; i++) {
            this.openState[i] = false;
            this.fullState[i] = false;
        }

    }

    private boolean inRange(int row, int col)
    {
        if (row > 0 && col > 0) {
            if (row <= n && col <= n) {
                return true;
            }
        }

        return false;
    }

    private int squareIdx(int row, int col)
    {
        // returns the index of this grid element (square)
        if (this.inRange(row, col)) {
            return (row - 1) * n + col - 1;
        }

        throw new IllegalArgumentException(
                String.format("(%d, %d) is an invalid (row, col) combination", row, col));
    }

    private boolean[] doesContigExist(int row, int col) {
        // Checks if the contiguous openState exist
        // Useful for knowing if this square is on a extreme of the grid
        // Returns an array of booleans of length 4
        // true if the contiguous square exists, false otherwise
        // ordering is clockwise starting from the upper square

        boolean[] doesExist = new boolean[NCONTIGSQ];

        if (row == 1) {
            doesExist[0] = false;
        } else {
            doesExist[0] = true;
        }

        if (col == n) {
            doesExist[1] = false;
        } else {
            doesExist[1] = true;
        }

        if (row == n) {
            doesExist[2] = false;
        } else {
            doesExist[2] = true;
        }

        if (col == 1) {
            doesExist[3] = false;
        } else {
            doesExist[3] = true;
        }

        return doesExist;

    }

    private int[] contigPos(int row, int col, String contig) {
        if (this.inRange(row, col)) {

            int[] pos = new int[2];
            switch (contig) {
                case "up":
                    pos[0] = row - 1;
                    pos[1] = col;
                    break;
                case "right":
                    pos[0] = row;
                    pos[1] = col + 1;
                    break;
                case "bottom":
                    pos[0] = row + 1;
                    pos[1] = col;
                    break;
                case "left":
                    pos[0] = row;
                    pos[1] = col - 1;
                    break;
                default:
                    throw new IllegalArgumentException(contig + " is not a valid contig square");
            }

            return pos;
        }

        throw new IllegalArgumentException(
                String.format("(%d, %d) is an invalid (row, col) combination", row, col));
    }

    private void setFull(int row, int col) {
        if (!this.isFull(row, col)) {
            int idx = this.squareIdx(row, col);
            this.fullState[idx] = true;

            if (row == this.n) this.doesPercolate = true;

            boolean[] contigExists = this.doesContigExist(row, col);
            int[] contigPos;
            int contigRow;
            int contigCol;

            for (int i = 0; i < NCONTIGSQ; i++) {
                if (contigExists[i]) {
                    contigPos = this.contigPos(row, col, CONTIGSQ[i]);
                    contigRow = contigPos[0];
                    contigCol = contigPos[1];

                    if (this.isOpen(contigRow, contigCol) && !this.isFull(contigRow, contigCol)) {
                        this.setFull(contigRow, contigCol);
                    }
                }
            }
        }
    }

    public void open(int row, int col)
    {
        if (!this.isOpen(row, col)) {
            if (this.inRange(row, col)) {
                int squareIndex = this.squareIdx(row, col);
                this.openState[squareIndex] = true;
                nOpen += 1;

                boolean[] contigExists = this.doesContigExist(row, col);
                boolean isContigOpen;
                boolean isContigFull;
                int contigSquareIdx;
                int[] contigSquarePos;

                if (row == 1) this.setFull(row, col);

                for (int i = 0; i < NCONTIGSQ; i++) {
                    if (contigExists[i]) {
                        contigSquarePos = this.contigPos(row, col, CONTIGSQ[i]);
                        contigSquareIdx = this.squareIdx(contigSquarePos[0],
                                                         contigSquarePos[1]);
                        isContigOpen = this.openState[contigSquareIdx];
                        isContigFull = this.fullState[contigSquareIdx];

                        if (isContigOpen) {
                            connections.union(contigSquareIdx, squareIndex);

                            if (isContigFull && !this.isFull(row, col)) {
                                this.setFull(row, col);
                            }
                        }

                    }
                }

                return;
            }

            throw new IllegalArgumentException(
                    String.format("(%d, %d) is an invalid (row, col) combination", row, col));
        }
    }

    public boolean isOpen(int row, int col)
    {
        if (this.inRange(row, col)) {
            return this.openState[this.squareIdx(row, col)];
        }

        throw new IllegalArgumentException(
                String.format("(%d, %d) is an invalid (row, col) combination", row, col));
    }

    public boolean isFull(int row, int col)
    {
        if (this.inRange(row, col)) {
            return this.fullState[this.squareIdx(row, col)];
        }

        throw new IllegalArgumentException(
                String.format("(%d, %d) is an invalid (row, col) combination", row, col));
    }

    public int numberOfOpenSites()
    {
        return nOpen;
    }

    public boolean percolates()
    {
        return doesPercolate;
    }
}
