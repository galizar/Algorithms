public class Outcast {

    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {

        int maxD = -1;
        String maxDNoun = "";

        for (String currNoun : nouns) {

            int currD = 0;
            for (String otherNoun : nouns) {

                if (!currNoun.equals(otherNoun)) { 
                    int distance = wordnet.distance(currNoun, otherNoun); 
                    if (distance != -1) currD += distance;
                }
            }

            if (currD > maxD) {
                maxD = currD;
                maxDNoun = currNoun;
            }
        }
        return maxDNoun;
    }

    public static void main(String[] args) {}
}
