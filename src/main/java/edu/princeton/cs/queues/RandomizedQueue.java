import java.util.Iterator;
import java.util.Random;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    private Item[] queue;
    private int N;
    private Random rand = new Random();

    private void resize(int max)
    {
        Item[] temp = (Item[]) new Object[max];

        for (int i = 0; i < N; i++) {
            temp[i] = queue[i];
        }

        queue = temp;
    }

    public RandomizedQueue()
    {
        queue = (Item[]) new Object[1];
    }

    public boolean isEmpty()
    { return N == 0; }

    public int size()
    { return N; }

    public void enqueue(Item data)
    {
        if (data != null) {
            queue[N++] = data;
            if (N == queue.length) resize(2*queue.length);
            return;
        }

        throw new IllegalArgumentException("data can not be null.");
    }

    public Item dequeue()
    {
        if (N != 0) {
            int idx = rand.nextInt(N);

            Item dequeued = queue[idx];
            queue[idx] = queue[N-1];
            queue[N-1] = null;
            N--;

            if (N > 0 && N <= queue.length/4) resize(queue.length/2);

            return dequeued;
        }

        throw new NoSuchElementException("Itemhe queue is empty.");
    }

    public Item sample()
    {
        if (N != 0) {
            int idx = rand.nextInt(N);
            return queue[idx];
        }

        throw new NoSuchElementException("Itemhe queue is empty.");
    }

    public Iterator<Item> iterator()
    {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator
    {
        private Item[] queueCopy = queue.clone();
        private int itemsLeft = N;

        public boolean hasNext()
        {
            return itemsLeft != 0;
        }

        public Item next()
        {
            if (hasNext()) {
                int idx = rand.nextInt(itemsLeft);
                Item data = queueCopy[idx];

                queueCopy[idx] = queueCopy[itemsLeft-1];
                queueCopy[itemsLeft-1] = null;

                itemsLeft--;

                return data;
            }

            throw new NoSuchElementException("End of iterator");
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Remove op. is not supported");
        }

    }

    public static void main(String[] args)
    {
        RandomizedQueue<String> leQueue = new RandomizedQueue<String>();


        for (int i = 0; i < args.length; i++) {
            leQueue.enqueue(args[i]);
        }

        assert leQueue.size() == 4 : "Size should be 4";

        for (int i = 0; i < args.length; i++) {
            System.out.println(leQueue.dequeue());
        }

        assert leQueue.size() == 0 : "Size should be 0";
        assert leQueue.isEmpty() == true : "isEmpty() should return true";

        for (int i = 0; i < args.length; i++) {
            leQueue.enqueue(args[i]);
        }

        assert leQueue.size() == 0 : "size should be 4";
        assert !leQueue.isEmpty() : "isEmpty() should return false";

        Iterator<String> iterator = leQueue.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
