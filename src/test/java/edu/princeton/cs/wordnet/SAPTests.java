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
    SAP testSAP1 = new SAP(digraph25);

    @Test
    void testIllegalNullArguments() {

        assertAll("illegal null arguments",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> new SAP(null)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.length(null, new Bag<Integer>())),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.length(new Bag<Integer>(), null)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.ancestor(null, new Bag<Integer>())),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.ancestor(new Bag<Integer>(), null)));

    }

    @Test
    void testVertexArgumentOutsidePrescribedRange() {

        assertAll("length method vertix arguments should be in range",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.length(-1, 3)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.length(25, 5)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.length(30, 4)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.length(3, -1)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.length(5, 25)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.length(4, 30)));

        assertAll("ancestor method vertix arguments should be in range",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.ancestor(-1, 3)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.ancestor(25, 5)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.ancestor(30, 4)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.ancestor(3, -1)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.ancestor(5, 25)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.ancestor(4, 30)));
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
                                     () -> testSAP1.length(vertexIterable1, new Bag<Integer>())),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.length(new Bag<Integer>(), vertexIterable1)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.length(vertexIterable2, new Bag<Integer>())),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> testSAP1.length(vertexIterable3, new Bag<Integer>())));
    }

    @Test
    void testLengthAndAncestorOnInts() {

        assertAll("length method on ints",
                  () -> assertEquals(-1, testSAP1.length(0, 0), "1"),
                  () -> assertEquals(4, testSAP1.length(13, 16), "2"),
                  () -> assertEquals(2, testSAP1.length(21, 22), "3"),
                  () -> assertEquals(5, testSAP1.length(17, 24), "4"),
                  () -> assertEquals(6, testSAP1.length(13, 6), "5"),
                  () -> assertEquals(10, testSAP1.length(21, 23), "6"),
                  () -> assertEquals(2, testSAP1.length(21, 1), "7"),
                  () -> assertEquals(2, testSAP1.length(1, 21), "8"),
                  () -> assertEquals(-1, testSAP1.length(0, 1), "9"),
                  () -> assertEquals(0, testSAP1.length(1, 1), "10"));

        assertAll("ancestor method on ints",
                  () -> assertEquals(-1, testSAP1.ancestor(0, 0), "1"),
                  () -> assertEquals(3, testSAP1.ancestor(13, 16), "2"),
                  () -> assertEquals(16, testSAP1.ancestor(21, 22), "3"),
                  () -> assertEquals(5, testSAP1.ancestor(17, 24), "4"),
                  () -> assertEquals(0, testSAP1.ancestor(13, 6), "5"),
                  () -> assertEquals(0, testSAP1.ancestor(21, 23), "6"),
                  () -> assertEquals(0, testSAP1.ancestor(21, 1), "7"),
                  () -> assertEquals(0, testSAP1.ancestor(1, 21), "8"),
                  () -> assertEquals(-1, testSAP1.ancestor(0, 1), "9"),
                  () -> assertEquals(0, testSAP1.ancestor(1, 1), "10"));
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
                  () -> assertEquals(3, testSAP1.length(v1, w1)), // shortest = 15 <-> 22 
                  () -> assertEquals(-1, testSAP1.length(v2, w2)),
                  () -> assertEquals(0, testSAP1.length(v3, w3))); // shortest possible path: two equal vertices

        assertAll("ancestor method on iterables",
                  () -> assertEquals(9, testSAP1.ancestor(v1, w1)),
                  () -> assertEquals(-1, testSAP1.ancestor(v2, w2)),
                  () -> assertEquals(20, testSAP1.ancestor(v3, w3)));
    }
}
