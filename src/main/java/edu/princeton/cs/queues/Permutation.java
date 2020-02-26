import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation
{
    public static void main(String[] args)
    {
        int leftToPrint = Integer.parseInt(args[0]);
        RandomizedQueue<String> randq = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            randq.enqueue(StdIn.readString());
        }

        while (leftToPrint > 0) {
            leftToPrint--;
            StdOut.println(randq.dequeue());
        }
    }
}
