import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OutcastTests {

    WordNet testWordNet100 = new WordNet("wordnet/synsets100-subgraph.txt", "wordnet/hypernyms100-subgraph.txt");

    @Test
    void testOutcast() {

        Outcast testOutcast = new Outcast(testWordNet100);

        String[] testNouns1 = new String[] {"jimdandy", "aminotransferase", "ricin", "telomerase"};
        String[] testNouns2 = new String[] {"entity", "ricin", "lactalbumin"};
        String[] testNouns3 = new String[] {"protein", "telomerase", "fibrinase", "chymosin"};

        assertAll("outcast tests",
                  () -> assertEquals("jimdandy", testOutcast.outcast(testNouns1)),
                  () -> assertEquals("entity", testOutcast.outcast(testNouns2)),
                  () -> assertEquals("fibrinase", testOutcast.outcast(testNouns3)));
    }
}
