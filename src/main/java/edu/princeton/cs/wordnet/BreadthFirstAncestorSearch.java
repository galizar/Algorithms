import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

class BreadthFirstAncestorSearch {

    Digraph G;
    boolean[] isMarked;
    int[] distToSource; 

    int ancestorPathLength = -1;   // stays -1 if no ancestral path is found
    int commonAncestorVertix = -1; // ditto 

    BreadthFirstAncestorSearch(Digraph G, int v, int w) {

        this.G = G;
        isMarked = new boolean[G.V()];
        distToSource = new int[G.V()];

        if (v == w) {
            ancestorPathLength = 0;
            commonAncestorVertix = v;
        } else {
            BreadthFirstDirectedPaths pathsFromV = new BreadthFirstDirectedPaths(G, v);
            ancestorBFS(pathsFromV, v, w);
        }

    }

    BreadthFirstAncestorSearch(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {

        this.G = G;
        BreadthFirstDirectedPaths pathsFromV = new BreadthFirstDirectedPaths(G, v);

        for (int vv : v) {
            for (int ww : w) {
                isMarked = new boolean[G.V()];
                distToSource = new int[G.V()];

                if (vv == ww) {
                    ancestorPathLength = 0;
                    commonAncestorVertix = vv; 
                    return; // found a shortest possible path, so stop
                } else {
                    ancestorBFS(pathsFromV, vv, ww);
                }
            }
        }
    }

    void ancestorBFS(BreadthFirstDirectedPaths pathsFromOther, int other, int s) {

        if (pathsFromOther.hasPathTo(s)) {
            // source is ancestor of other
            setAncestorAndLength(s, pathsFromOther.distTo(s));
        }

        Queue<Integer> q = new Queue<>();
        isMarked[s] = true;
        distToSource[s] = 0;
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();

            for (int w : G.adj(v)) {

                if (other == w) {
                    // source is descendant of other
                    setAncestorAndLength(other, distToSource[v] + 1);
                } else if (!isMarked[w]) {

                    q.enqueue(w);
                    distToSource[w] = distToSource[v] + 1;
                    isMarked[w] = true;

                    if (pathsFromOther.hasPathTo(w)) {
                        setAncestorAndLength(w, pathsFromOther.distTo(w) + distToSource[w]);
                    }
                }
            }
        }
    }

    private void setAncestorAndLength(int newAncestor, int newLength) {

        if (ancestorPathLength == -1 || newLength < ancestorPathLength) {
            commonAncestorVertix = newAncestor;
            ancestorPathLength = newLength;
        }
    }
}
