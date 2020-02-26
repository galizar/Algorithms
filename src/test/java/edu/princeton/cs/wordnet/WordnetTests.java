/* *****************************************************************************
 *  Name: Wordnet
 *  Date: 2019-10-10
 *  Description: Tests for ./src/WordnetTests.java
 **************************************************************************** */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WordnetTests {
    WordNet wordnet0 = new WordNet("synsets100-subgraph.txt", "hypenyms100-subgraph");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldTestIllegalNullArguments() throws IllegalArgumentException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Arguments must not be null");

        WordNet test;
        test = new WordNet(null, "hypernyms.txt");
        test = new WordNet("synsets.txt", null);
        test.isNoun(null);
        test.distance(null, "Hola");
        test.distance("Tedious", null);
        test.sap(null, "Lel");
        test.sap("ciao", null);
    }

    @Test
    public void shouldTestIllegalNonRootedDAGForConstructor() throws IllegalArgumentException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Hypernym relationships are not a DAG");

        new WordNet("not relevant", "cyclicDigraph.txt");
    }

    @Test
    public void shouldTestNonWordNetNounAForDistanceMethod() throws IllegalArgumentException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("yibiyabo is not a WordNet noun");

        wordnet0.distance("yibiyabo", "chondrin");
    }

    @Test
    public void shouldTestNonWordNetNounBForDistanceMethod() throws IllegalArgumentException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("quickmaffs is not a WordNet noun");

        wordnet0.distance("elastin", "quickmaffs");
    }

    @Test
    public void shouldTestNonWordNetNounAForSAPMethod() throws IllegalArgumentException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("fufufufufu is not a WordNet noun");

        wordnet0.sap("fufufufufu", "filaggrin");
    }

    @Test
    public void shouldTestNonWordNetNounBForSAPMethod() throws IllegalArgumentException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("illuminatus is not a WordNet noun");

        wordnet0.sap("gelatin", "illuminatus");
    }
}
