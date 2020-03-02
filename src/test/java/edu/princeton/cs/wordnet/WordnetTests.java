/* *****************************************************************************
 *  Name: Wordnet
 *  Date: 2019-10-10
 *  Description: Tests for ./src/WordnetTests.java
 **************************************************************************** */

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class WordnetTests {
    WordNet wordnet0 = new WordNet("wordnet/synsets100-subgraph.txt", "wordnet/hypernyms100-subgraph.txt");

    @Test
    void testIllegalNullArguments() {

        // should split up, but wanted to try the assertAll syntax 

        assertAll("constructor",
                () -> assertThrows(IllegalArgumentException.class, 
                                   () -> new WordNet(null, "wordnet/hypernyms.txt")),
                () -> assertThrows(IllegalArgumentException.class, 
                                   () -> new WordNet("wordnet/synsets.txt", null)),
                () -> assertThrows(IllegalArgumentException.class,
                                   () -> new WordNet(null, null)));


        WordNet testObj = new WordNet("wordnet/synsets.txt", "wordnet/hypernyms.txt");

        assertAll("instance methods",
                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testObj.isNoun(null)),

                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testObj.distance(null, "Abel")),
                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testObj.distance("Abel", null)),
                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testObj.distance(null, null)),

                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testObj.sap("Abel", null)),
                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testObj.sap(null, "Abel")),
                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testObj.sap(null, null)));
    }

    @Test
    void testIllegalConstructorArgumentNonRootedDAG() {
        assertThrows(IllegalArgumentException.class, 
                     () -> new WordNet("wordnet/synsets.txt", "wordnet/cyclicDigraph.txt"));
    }

    @Test
    void testIllegalDistanceMethodArgumentNotAWordNetNoun() {
    }

    @Test
    void testIllegalSAPMethodArgumentNotAWordNetNoun() {

    }
}
