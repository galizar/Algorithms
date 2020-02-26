/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.ResizingArrayStack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private ResizingArrayStack<Board> optimalSeq = new ResizingArrayStack<Board>();
    private int minNSteps;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
       if (initial == null) {
           throw new IllegalArgumentException("Initial board can not be null.");
       }

       GameTree originalGame = new GameTree(initial);
       GameTree twinGame     = new GameTree(initial.twin());

       GameTree[] games = {originalGame, twinGame};

       int currGame = 0;

       while (true) {
           //if (currGame == 0) System.out.println("ORIGINAL");
           //else               System.out.println("twin");
           games[currGame].step();

           if (currGame == 0) {
               if (originalGame.isSolved()) {
                   solvable              = true;
                   minNSteps             = originalGame.getStepsToSolve();
                   SearchNode smallest   = originalGame.getCurrNode();
                   Board boardOfSmallest;

                   while (smallest != null) {
                       boardOfSmallest = smallest.getBoard();
                       optimalSeq.push(boardOfSmallest);
                       smallest = smallest.getPrevNode();
                   }

                   break;
               }

               currGame = 1;
           } else {
               if (twinGame.isSolved()) {
                   solvable = false;
                   break;
               }

               currGame = 0;
           }
       }
    }

    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return minNSteps;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return optimalSeq;
    }


    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int steps;
        private SearchNode prevNode;
        private int HPF;  // Hamming Priority Function
        private int MPF;  // Manhattan Priority Function

        public SearchNode(Board initial)
        {
            this.board = initial;
            this.steps = 0;
            this.prevNode = null;
        }

        public SearchNode(Board board, int steps, SearchNode prevNode)
        {
            this.board = board;
            this.steps = steps;
            this.prevNode = prevNode;
            this.HPF = board.hamming() + steps;
            this.MPF = board.manhattan() + steps;
        }

        public int compareTo(SearchNode other)
        {
            // With Hamming Prioritity Function
            //return Integer.compare(this.getHPF(), other.getHPF());

            // With Manhattan Prioritity Function
            return Integer.compare(this.getMPF(), other.getMPF());
        }

        public Board getBoard()
        {
            return board;
        }

        public int getSteps()
        {
            return steps;
        }

        public SearchNode getPrevNode() {
            return prevNode;
        }

        public int getHPF()
        {
            return HPF;
        }

        public int getMPF()
        {
            return MPF;
        }
    }


    private class GameTree
    {
        private MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        private SearchNode currNode;
        private int stepsToSolve;
        private boolean solved = false;

        public GameTree(Board initial)
        {
            if (initial.isGoal()) {
                this.solved = true;
                return;
            }

            SearchNode initialNode = new SearchNode(initial);

            for (Board neighbor : initial.neighbors())
                this.pq.insert(new SearchNode(neighbor, initialNode.getSteps()+1, initialNode));

        }

        public void step()
        {
            if (this.isSolved()) {
                return;
            }

            this.currNode = this.pq.delMin();
            SearchNode prevNode = this.currNode.getPrevNode();
            Board boardOfCurr = this.currNode.getBoard();
            Board boardOfPrev = prevNode.getBoard();


            //System.out.println("curr:");
            //System.out.println("hamming: " + this.currNode.getHPF());
            //System.out.println("steps: " + this.currNode.getSteps());
            //System.out.println(boardOfCurr.toString());

            //System.out.println("prev:");
            //System.out.println(this.currNode.getHPF());
            //System.out.println(boardOfPrev.toString());

            if (boardOfCurr.isGoal()) {
                //System.out.println(currNode.getBoard().toString());
                this.solved = true;
                this.stepsToSolve = this.currNode.getSteps();
                return;
            }

            for (Board neighbor : boardOfCurr.neighbors()) {

                //System.out.println("Potentially to add:");
                //System.out.println(neighbor.toString());

                if (!neighbor.equals(boardOfPrev)) {
                    //System.out.println("added neighbor:");
                    //System.out.println(neighbor.toString());
                    pq.insert(new SearchNode(neighbor, this.currNode.getSteps()+1, currNode));
                }
            }
        }

        public boolean isSolved()
        {
            return solved;
        }

        public SearchNode getCurrNode()
        {
            return currNode;
        }

        public int getStepsToSolve()
        {
            return stepsToSolve;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
