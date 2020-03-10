import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {

    private Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        checkNotNullArgs(G);
        this.digraph = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        checkVertexIsInRange(v);
        checkVertexIsInRange(w);

        BreadthFirstAncestorSearch search = new BreadthFirstAncestorSearch(digraph, v, w);
        return search.ancestorPathLength;
    }


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {

        checkVertexIsInRange(v);
        checkVertexIsInRange(w);

        BreadthFirstAncestorSearch search = new BreadthFirstAncestorSearch(digraph, v, w);
        return search.commonAncestorVertix;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        
        checkNotNullArgs(v, w);

        BreadthFirstAncestorSearch search = new BreadthFirstAncestorSearch(digraph, v, w);
        return search.ancestorPathLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

        checkNotNullArgs(v, w);

        BreadthFirstAncestorSearch search = new BreadthFirstAncestorSearch(digraph, v, w);
        return search.commonAncestorVertix;
    }

    private void checkNotNullArgs(Object... args) {

        for (Object arg : args) {
            if (arg == null) throw new IllegalArgumentException("arguments can not be null");

            if (arg instanceof Iterable) {
                for (Object objInArg : (Iterable) arg) {
                    if (objInArg == null) {
                        throw new IllegalArgumentException("iterables can not contain null");
                    }
                }
            }
        }
    }

    private void checkVertexIsInRange(int v) {

        if (v >= 0 && v < digraph.V()) return;
        throw new IllegalArgumentException("vertex is out of range");
    }

    // do unit testing of this class
    public static void main(String[] args) {


    }
}
