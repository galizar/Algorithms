/* *****************************************************************************
 *  Name: Wordnet
 *  Date: 2019-10-10
 *  Description: Tests for ./src/WordnetTests.java
 **************************************************************************** */

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class WordnetTests {
    WordNet testWordNet100 = new WordNet("wordnet/synsets100-subgraph.txt", "wordnet/hypernyms100-subgraph.txt");
    WordNet testWordNet15 = new WordNet("wordnet/synsets15.txt", "wordnet/hypernyms15Tree.txt");

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

    @Test
    void testDistance() {

        assertAll("with one-noun synsets",
                  () -> assertEquals(4, testWordNet15.distance("c", "j"), "1"),
                  () -> assertEquals(2, testWordNet15.distance("k", "l"), "2"),
                  () -> assertEquals(-1, testWordNet15.distance("a", "m"), "3"),
                  () -> assertEquals(2, testWordNet15.distance("g", "k"), "4"));

        assertAll("with several-noun synsets",
                  () -> assertEquals(6, testWordNet100.distance("aminotransferase", "lactalbumin"), "5"),
                  () -> assertEquals(5, testWordNet100.distance("transferrin", "chymosin"), "6"),
                  () -> assertEquals(3, testWordNet100.distance("CRP", "gamma_globulin"), "7"),
                  () -> assertEquals(-1, testWordNet100.distance("entity", "jimhickey"), "8"),
                  () -> assertEquals(11, testWordNet100.distance("aminotransferase", "jimdandy"), "9"),
                  () -> assertEquals(2, testWordNet100.distance("ricin", "lactalbumin")),
                  () -> assertEquals(-1, testWordNet100.distance("ricin", "entity")),
                  () -> assertEquals(-1, testWordNet100.distance("lactalbumin", "entity")));
    }

    @Test
    void testSAP() {
        // damn sap is a bad name for this method, but we can't change it tho

        assertAll("with one-noun synsets",
                  () -> assertEquals("a", testWordNet15.sap("c", "j"), "1"),
                  () -> assertEquals("f", testWordNet15.sap("k", "l"), "2"),
                  () -> assertEquals("", testWordNet15.sap("a", "m"), "3"),
                  () -> assertEquals("f", testWordNet15.sap("g", "k"), "4"));

        assertAll("with several-noun synsets",
                  () -> assertEquals("protein", testWordNet100.sap("aminotransferase", "lactalbumin"), "1"),
                  () -> assertEquals("protein", testWordNet100.sap("transferrin", "chymosin"), "2"),
                  () -> assertEquals("globulin", testWordNet100.sap("CRP", "gamma_globulin"), "3"),
                  () -> assertEquals("", testWordNet100.sap("entity", "jimhickey"), "4"),
                  () -> assertEquals("entity", testWordNet100.sap("aminotransferase", "jimdandy"), "5"),
                  () -> assertEquals("albumin albumen", testWordNet100.sap("ricin", "serum_albumin")));
    }
}
