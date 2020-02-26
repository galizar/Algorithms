//Dequeue. A double-ended queue or deque (pronounced “deck”) is a generalization of
//a stack and a queue that supports adding and removing items from either the front or
//the back of the data structure.

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>
{
    private Node first, last;
    private int N;

    private class Node
    {
        Node prev;
        Node next;
        Item data;

        public Node(Item data)
        {
            this.data = data;
        }
    }

    public Deque()
    {
        first = null;
        last = null;
        N = 0;
    }

    public boolean isEmpty()
    { return first == null && last == null; }

    public int size()
    { return N; }

    public void addFirst(Item data)
    {
        if (data != null) {
            N++;
            if (N == 1) {
                first = new Node(data);
                last = first;
            } else if (N == 2) {
                last = new Node(first.data);
                first = new Node(data);
                first.next = last;
                last.prev = first;
            } else {
                Node oldFirst = first;
                first = new Node(data);
                first.next = oldFirst;
                oldFirst.prev = first;
            }
            return;
        }

        throw new IllegalArgumentException("Data can not be null.");
    }

    public void addLast(Item data)
    {
        if (data != null) {
            N++;
            if (N == 1) {
                last = new Node(data);
                first = last;
            } else if (N == 2) {
                first = new Node(last.data);
                last = new Node(data);
                last.prev = first;
                first.next = last;
            } else {
                Node oldLast = last;
                last = new Node(data);
                last.prev = oldLast;
                oldLast.next = last;
            }
            return;
        }

        throw new IllegalArgumentException("Data can not be null.");
    }

    public Item removeFirst()
    {
        if (!this.isEmpty()) {
            N--;
            Node oldFirst = first;

            if (N == 1) {
                last.prev = null;
                first = last;
                return oldFirst.data;
            } else if (N == 0) {
                first = null;
                last = null;
                return oldFirst.data;
            }

            first = oldFirst.next;
            return oldFirst.data;
        }

        throw new NoSuchElementException("The deque is empty.");
    }

    public Item removeLast()
    {
        if (!this.isEmpty()) {
            N--;
            Node oldLast = last;

            if (N == 1) {
                first.next = null;
                last = first;
                return oldLast.data;
            } else if (N == 0) {
                last = null;
                first = null;
                return oldLast.data;
            }

            last = oldLast.prev;
            return oldLast.data;
        }

        throw new NoSuchElementException("The deque is empty.");
    }

    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator
    {
        private Node currNode = first;
        private Node oldCurr;

        public boolean hasNext()
        {
            if (currNode != null) {
                return currNode.next != null;
            } else {
                return false;
           }
        }

        public Item next()
        {
            if (this.hasNext() || currNode == last) {
                oldCurr = currNode;
                currNode = oldCurr.next;
                return oldCurr.data;
            } else {
                throw new NoSuchElementException("End of iterator");
            }
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Remove op. is not supported");
        }
    }

    public static void main(String[] args)
    {
        Deque<Integer> testDeque = new Deque<Integer>();
       
        // testing with n = 50
        for (int i = 0; i < 50; i++) {
            testDeque.addLast(i);
        }

        if (!(testDeque.size() == 50)) System.out.println("TEST FAILED");

        Iterator<Integer> testIterator = testDeque.iterator();

        int steps = 0;
        while (true) {
            try {
                System.out.println(testIterator.next());
                steps++;
            } catch (Exception e) {
                System.out.println("Iterator ran for " + steps + " steps");
                return;
            }
        }

    }
}
