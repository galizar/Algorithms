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

        if (!anyIsARoot(v, w)) {

            if (v == w) {
                ancestorPathLength = 0;
                commonAncestorVertix = G.adj(v).iterator().next();
            } else {
                BreadthFirstDirectedPaths pathsFromV = new BreadthFirstDirectedPaths(G, v);
                ancestorBFS(pathsFromV, v, w);
            }

        }
    }

    BreadthFirstAncestorSearch(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {

        this.G = G;
        isMarked = new boolean[G.V()];
        distToSource = new int[G.V()];

        BreadthFirstDirectedPaths pathsFromV = new BreadthFirstDirectedPaths(G, v);

        for (int vv : v) {
            for (int ww : w) {
                if (anyIsARoot(vv, ww)) return;

                if (vv == ww) {
                    ancestorPathLength = 0;
                    commonAncestorVertix = G.adj(vv).iterator().next();
                    return; // found a shortest possible path, so stop
                } else {
                    int prevAncestorPathLength = ancestorPathLength;
                    int prevCommonAncestorVertix = commonAncestorVertix;

                    ancestorBFS(pathsFromV, vv, ww);

                    // reverse to previous values if not found a shorter path
                    if (prevAncestorPathLength != -1 && ancestorPathLength >= prevAncestorPathLength) {
                        ancestorPathLength = prevAncestorPathLength;
                        commonAncestorVertix = prevCommonAncestorVertix;
                    }
                }
            }
        }
    }

    private boolean anyIsARoot(int... vertices) {
        
        for (int v : vertices) {
            if (!G.adj(v).iterator().hasNext()) {
                return true;
            }
        }
        return false;
    }

    void ancestorBFS(BreadthFirstDirectedPaths pathsFromOther, int other, int s) {
        boolean isSourceAncestorOfOther = pathsFromOther.hasPathTo(s);
        boolean isSourceDescendantOfOther = false;

        Queue<Integer> q = new Queue<>();
        isMarked[s] = true;
        distToSource[s] = 0;
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();

            for (int w : G.adj(v)) {
                if (!isSourceDescendantOfOther && other == w) {
                    isSourceDescendantOfOther = true;
                }
                
                if (!isMarked[w]) {

                    q.enqueue(w);
                    distToSource[w] = distToSource[v] + 1;
                    isMarked[w] = true;

                    if (other != w && pathsFromOther.hasPathTo(w)) {
                        commonAncestorVertix = w;

                        if (isSourceDescendantOfOther) {
                            ancestorPathLength = pathsFromOther.distTo(w) + 1;
                        } else if (isSourceAncestorOfOther) {
                            ancestorPathLength = distToSource[w] + 1;
                        } else {
                            ancestorPathLength = pathsFromOther.distTo(w) + distToSource[w];
                        }

                        return;
                    }
                }
            }
        }
    }
}
