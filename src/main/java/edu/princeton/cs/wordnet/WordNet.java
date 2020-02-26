/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;

public class WordNet {

    // constructor takes the name of two input files
    WordNet(String synsets, String hypernims) {
        System.out.println(synsets + " " + hypernims);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return new Bag<String>();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return false;
    }

    // distance between nounA and nounB (see definitions.txt)
    public int distance(String nounA, String nounB) {
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB in a shortest ancestral path (see definitions.txt)
    public String sap(String nounA, String nounB) {
        return "stub";
    }

    public static void main(String[] args) {

    }
}
