public class ResizingArray<T>
{
    private T[] array;
    private int N;

        public ResizingArray(int cap)
        { array = (T[]) new Object[cap]; }

        public boolean isEmpty() { return N == 0; }
        public int size()        { return N; }

        public void push(T item, int idx)
        {
            if (idx < 0 || idx >= array.length) {
                throw new IllegalArgumentException("Index " + idx +
                        " is out of bounds for array of size " +
                        array.length);
            }

            N++;
            if (N == array.length) resize(2*array.length);
            array[idx] = item;
        }

        public T remove(int idx)
        {
            if (idx < 0 || idx >= array.length) {
                throw new IllegalArgumentException("Index " + idx +
                        " is out of bounds for array of size " +
                        array.length);
            }

            N--;
            T item = array[idx];
            array[idx] = null;
            if (N > 0 && N <= array.length/4) resize(array.length/2);
            return item;
        }

        private void resize(int max)
        {
            T[] temp = (T[]) new Object[max];

            for (int i = 0; i < N; i++) {
                temp[i] = array[i];
            }

            array = temp;
        }
}
