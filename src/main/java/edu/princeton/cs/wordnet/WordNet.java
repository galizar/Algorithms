import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;

public class WordNet {

    private final String nullArgMessage = "arguments can not be null";
    private Digraph hypernymsDigraph;
    private String[] synsets;
    private ST<String, Integer> nouns = new ST<>(); // maps a noun to the index of the synset it is contained in
    private SAP ancestralRelations;

    // constructor takes the name of two input files
    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException(nullArgMessage);
        }

        buildHypernymsDigraphFromInputFile(new In(hypernyms));
        checkHypernymsDigraphIsRootedDAG();
        buildSynsetsArrayFromInputFile(new In(synsets));
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

        this.ancestralRelations = new SAP(hypernymsDigraph);
    }

    private void checkHypernymsDigraphIsRootedDAG() {
        Topological tryTopologicallySortHypernyms = new Topological(hypernymsDigraph);

        if (!tryTopologicallySortHypernyms.hasOrder()) {
            throw new IllegalArgumentException("input hypernyms digraph is not a rooted DAG");
        }
    }

    private void buildSynsetsArrayFromInputFile(In inputStream) {

        String[] lines = inputStream.readAllLines();
        this.synsets = new String[lines.length];

        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            String synset = parts[1];
            for (String noun : synset.split(" ")) this.nouns.put(noun, i);
            this.synsets[i] = synset;
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns; 
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {

        if (word == null) {
            throw new IllegalArgumentException(nullArgMessage);
        }
        return nouns.contains(word);
    }

    // distance between nounA and nounB (see definitions.txt)
    public int distance(String nounA, String nounB) {

        checkInputNouns(nounA, nounB);

        int nounASynsetIdx = nouns.get(nounA);
        int nounBSynsetIdx = nouns.get(nounB);

        return ancestralRelations.length(nounASynsetIdx, nounBSynsetIdx);
    }

    private void checkInputNouns(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException(nullArgMessage);
        } else if (!nouns.contains(nounA) || !nouns.contains(nounB)) {
            throw new IllegalArgumentException("input nouns must be a WordNet noun");
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB in a shortest ancestral path (see definitions.txt)
    public String sap(String nounA, String nounB) {

        checkInputNouns(nounA, nounB);

        int nounASynsetIdx = nouns.get(nounA);
        int nounBSynsetIdx = nouns.get(nounB);
        int ancestorSynsetIdx = ancestralRelations.ancestor(nounASynsetIdx, nounBSynsetIdx);

        return ancestorSynsetIdx != -1 ? synsets[ancestorSynsetIdx] : "";
    }

    public static void main(String[] args) {}
}
