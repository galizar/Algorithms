/* *****************************************************************************
 *  Name: Wordnet
 *  Date: 2019-10-10
 *  Description: Tests for ./src/WordnetTests.java
 **************************************************************************** */

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

public class WordnetTests {
    WordNet testWordNet100 = new WordNet("wordnet/synsets100-subgraph.txt", "wordnet/hypernyms100-subgraph.txt");

    @Test
    void testIllegalNullArguments() {

        assertAll("constructor",
                () -> assertThrows(IllegalArgumentException.class, 
                                   () -> new WordNet(null, "wordnet/hypernyms.txt")),
                () -> assertThrows(IllegalArgumentException.class, 
                                   () -> new WordNet("wordnet/synsets.txt", null)),
                () -> assertThrows(IllegalArgumentException.class,
                                   () -> new WordNet(null, null)));


        assertAll("instance methods",
                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testWordNet100.isNoun(null)),

                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testWordNet100.distance(null, "Abel")),
                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testWordNet100.distance("Abel", null)),
                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testWordNet100.distance(null, null)),

                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testWordNet100.sap("Abel", null)),
                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testWordNet100.sap(null, "Abel")),
                () -> assertThrows(IllegalArgumentException.class,
                                   () -> testWordNet100.sap(null, null)));
    }

    @Test
    void testIllegalConstructorArgumentNonRootedDAG() {

        assertAll("constructor",
                  () -> assertThrows(IllegalArgumentException.class, 
                                     () -> new WordNet("", "wordnet/hypernyms3InvalidCycle.txt")),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> new WordNet("", "wordnet/hypernyms6InvalidCycle.txt")),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> new WordNet("", "wordnet/hypernyms3InvalidTwoRoots.txt")),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> new WordNet("", "wordnet/hypernyms6InvalidTwoRoots.txt")));
    }
    
    @Test
    void testIsNoun() {

        assertAll("isNoun tests",
                  () -> assertTrue(testWordNet100.isNoun("CRP")),
                  () -> assertTrue(testWordNet100.isNoun("security_blanket")),
                  () -> assertTrue(testWordNet100.isNoun("immunoglobulin")),
                  () -> assertTrue(testWordNet100.isNoun("immune_gamma_globulin")),
                  () -> assertTrue(testWordNet100.isNoun("immune_globulin")),
                  () -> assertTrue(testWordNet100.isNoun("zymase")),
                  () -> assertFalse(testWordNet100.isNoun("galizar")));

    }

    @Test
    void testIllegalDistanceMethodArgumentNotAWordNetNoun() {

        assertAll("distance method non-wordnet noun tests",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testWordNet100.distance("galizar", "CRP")),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testWordNet100.distance("CRP", "galizar")),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testWordNet100.distance("galizar", "herp derp")));
    }

    @Test
    void testIllegalSAPMethodArgumentNotAWordNetNoun() {
        
        assertAll("sap method non-wordnet noun tests",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testWordNet100.sap("galizar", "CRP")),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testWordNet100.sap("CRP", "galizar")),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testWordNet100.sap("galizar", "herp derp")));
    }
}
