import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;

public class SAPTests {

    Digraph digraph25 = new Digraph(new In("wordnet/digraph25.txt"));
    Digraph digraph2 = new Digraph(new In("wordnet/digraph2.txt"));
    Digraph digraph3 = new Digraph(new In("wordnet/digraph3.txt"));
    Digraph digraph5 = new Digraph(new In("wordnet/digraph5.txt"));
    Digraph digraph6 = new Digraph(new In("wordnet/digraph6.txt"));
    Digraph digraph9 = new Digraph(new In("wordnet/digraph9.txt"));
    SAP testSAP25 = new SAP(digraph25);
    SAP testSAP2 = new SAP(digraph2);
    SAP testSAP3 = new SAP(digraph3);
    SAP testSAP5 = new SAP(digraph5);
    SAP testSAP6 = new SAP(digraph6);
    SAP testSAP9 = new SAP(digraph9);

    @Test
    void testIllegalNullArguments() {

        assertAll("illegal null arguments",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> new SAP(null)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.length(null, new Bag<Integer>())),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.length(new Bag<Integer>(), null)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.ancestor(null, new Bag<Integer>())),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.ancestor(new Bag<Integer>(), null)));
    }

    @Test
    void testVertexArgumentOutsidePrescribedRange() {

        assertAll("length method vertix arguments should be in range",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.length(-1, 3)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.length(25, 5)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.length(30, 4)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.length(3, -1)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.length(5, 25)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.length(4, 30)));

        assertAll("ancestor method vertix arguments should be in range",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.ancestor(-1, 3)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.ancestor(25, 5)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.ancestor(30, 4)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.ancestor(3, -1)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.ancestor(5, 25)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.ancestor(4, 30)));
    }

    @Test
    void testIterableContainsNull() {

        Bag<Integer> vertexIterable1 = new Bag<>();
        vertexIterable1.add(3);
        vertexIterable1.add(null);
        vertexIterable1.add(1);

        Bag<Integer> vertexIterable2 = new Bag<>();
        vertexIterable2.add(null);
        vertexIterable2.add(3);
        vertexIterable2.add(1);

        Bag<Integer> vertexIterable3 = new Bag<>();
        vertexIterable3.add(3);
        vertexIterable3.add(1);
        vertexIterable3.add(null);

        assertAll("illegal null in Iterable",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.length(vertexIterable1, new Bag<Integer>())),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.length(new Bag<Integer>(), vertexIterable1)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.length(vertexIterable2, new Bag<Integer>())),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP25.length(vertexIterable3, new Bag<Integer>())));
    }

    @Test
    void testLengthAndAncestorOnInts() {

        assertAll("length method on ints",
                  () -> assertEquals(0, testSAP25.length(0, 0), "1"),
                  () -> assertEquals(4, testSAP25.length(13, 16), "2"),
                  () -> assertEquals(2, testSAP25.length(21, 22), "3"),
                  () -> assertEquals(5, testSAP25.length(17, 24), "4"),
                  () -> assertEquals(6, testSAP25.length(13, 6), "5"),
                  () -> assertEquals(10, testSAP25.length(21, 23), "6"),
                  () -> assertEquals(4, testSAP25.length(21, 1), "7"),
                  () -> assertEquals(4, testSAP25.length(1, 21), "8"),
                  () -> assertEquals(1, testSAP25.length(0, 1), "9"),
                  () -> assertEquals(0, testSAP25.length(1, 1), "10"),
                  () -> assertEquals(2, testSAP25.length(0, 3), "11"),
                  () -> assertEquals(2, testSAP2.length(3, 1), "12"),
                  () -> assertEquals(3, testSAP3.length(14, 10), "13"),
                  () -> assertEquals(2, testSAP5.length(15, 8), "14"),
                  () -> assertEquals(5, testSAP6.length(5, 0), "15"),
                  () -> assertEquals(1, testSAP9.length(0, 3), "16"));

        assertAll("ancestor method on ints",
                  () -> assertEquals(0, testSAP25.ancestor(0, 0), "1"),
                  () -> assertEquals(3, testSAP25.ancestor(13, 16), "2"),
                  () -> assertEquals(16, testSAP25.ancestor(21, 22), "3"),
                  () -> assertEquals(5, testSAP25.ancestor(17, 24), "4"),
                  () -> assertEquals(0, testSAP25.ancestor(13, 6), "5"),
                  () -> assertEquals(0, testSAP25.ancestor(21, 23), "6"),
                  () -> assertEquals(1, testSAP25.ancestor(21, 1), "7"),
                  () -> assertEquals(1, testSAP25.ancestor(1, 21), "8"),
                  () -> assertEquals(0, testSAP25.ancestor(0, 1), "9"),
                  () -> assertEquals(1, testSAP25.ancestor(1, 1), "10"),
                  () -> assertEquals(3, testSAP2.ancestor(3, 1), "11"),
                  () -> assertEquals(11, testSAP3.ancestor(14, 10), "12"));
    }

    @Test
    void testLengthAndAncestorOnIterables() {

        Bag<Integer> v1 = new Bag<>();
        v1.add(14);
        v1.add(15);
        v1.add(23);
        Bag<Integer> w1 = new Bag<>();
        w1.add(4);
        w1.add(17);
        w1.add(22);

        Bag<Integer> v2 = new Bag<>();
        v2.add(0);
        Bag<Integer> w2 = new Bag<>();
        w2.add(3);
        w2.add(12);
        w2.add(8);

        Bag<Integer> v3 = new Bag<>();
        v3.add(14);
        v3.add(15);
        v3.add(23);
        Bag<Integer> w3 = new Bag<>();
        w3.add(4);
        w3.add(17);
        w3.add(22);
        w3.add(23);

        assertAll("length method on iterables",
                  () -> assertEquals(3, testSAP25.length(v1, w1), "1"), // shortest = 15 <-> 22
                  () -> assertEquals(2, testSAP25.length(v2, w2), "2"), // 0 <-> 3
                  () -> assertEquals(0, testSAP25.length(v3, w3), "3")); // 23 <-> 23

        assertAll("ancestor method on iterables",
                  () -> assertEquals(9, testSAP25.ancestor(v1, w1), "1"),
                  () -> assertEquals(0, testSAP25.ancestor(v2, w2), "2"),
                  () -> assertEquals(23, testSAP25.ancestor(v3, w3), "3"));
    }
}
