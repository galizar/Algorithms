/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;

public class WordNet {

    String nullArgMessage = "arguments can not be null";
    Digraph hypernymsDigraph;

    // constructor takes the name of two input files
    WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException(nullArgMessage);
        }

        buildHypernymsDigraphFromInputFile(new In(hypernyms));
        checkHypernymsDigraphIsRootedDAG();
    }

    private void buildHypernymsDigraphFromInputFile(In inputStream) {

        String[] lines = inputStream.readAllLines();
        this.hypernymsDigraph = new Digraph(lines.length);

        for (String line : lines) {
            String[] parts = line.split(",");
            int v = Integer.parseInt(parts[0]);

            for (int i = 1; i < parts.length; i++) {
                int w = Integer.parseInt(parts[i]);
                this.hypernymsDigraph.addEdge(v, w);
            }
        }
    }

    private void checkHypernymsDigraphIsRootedDAG() {
        Topological tryTopologicallySortHypernyms = new Topological(hypernymsDigraph);

        if (!tryTopologicallySortHypernyms.hasOrder()) {
            throw new IllegalArgumentException("input hypernyms digraph is not a rooted DAG");
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return new Bag<String>();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {

        if (word == null) {
            throw new IllegalArgumentException(nullArgMessage);
        }

        return false;
    }

    // distance between nounA and nounB (see definitions.txt)
    public int distance(String nounA, String nounB) {

        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException(nullArgMessage);
        }

        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB in a shortest ancestral path (see definitions.txt)
    public String sap(String nounA, String nounB) {
        
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException(nullArgMessage);
        }

        return "stub";
    }

    public static void main(String[] args) {

    }
}
