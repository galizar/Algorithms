/* *****************************************************************************
 *  Name: Combinations
 *  Date: 2019-06-20
 *  Description: Produces all combinations of size k for any array of size n.
 *               Thanks to Devesh Agrawal for the logic.
 **************************************************************************** */
public class Combinations<T> {
    public Combinations(T[] arr, int k)
    {
        int n = arr.length;
        T[] data = (T[]) new Object[k];

        produceCombinations(arr, data, 0, n-1, 0, k);
    }

    public void produceCombinations(T[] arr, T[] data, int start,
                             int end, int index, int k)
    {
        // Current combination is ready to be printed, print it
        if (index == k)
        {
            for (T el : data) {
                System.out.print(el + " ");
            }
            System.out.println();
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i = start; i <= end && end-i+1 >= k-index; i++)
        {
            data[index] = arr[i];
            produceCombinations(arr, data, i+1, end, index+1, k);
        }
    }
}
