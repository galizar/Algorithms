/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Comparator;

public class QuickSort
{
    private Point[] sortedPoints;

    private QuickSort(Point[] points, Comparator<Point> comparator)  {
        sortedPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) sortedPoints[i] = points[i];

        sort(points, comparator);
    }

    private static void splits(Point[] points, int lo, int hi)
    {
        int n = hi-lo;
        int mid = lo + n / 2;

        int i = lo;  // start of left half
        int j = mid; // start of right half

        System.out.println("Left half:");
        System.out.print("[");
        while (i < mid) {
            System.out.print(points[i++] + ",");
        }
        System.out.println("]");
        System.out.println();

        System.out.println("Right half:");
        System.out.print("[");
        while (j < n) {
            System.out.print(points[j++] + ",");
        }
        System.out.println("]");
    }

    public void sort(Point[] points, Comparator<Point> comparator)
    {
        StdRandom.shuffle(points);
        sort(points, 0, points.length - 1, comparator);
    }

    private void sort(Point[] points, int lo, int hi, Comparator<Point> comparator)
    {
        if (hi <= lo) return;
        int j = partition(points, lo, hi, comparator);
        sort(points, lo, j-1, comparator);
        sort(points, j+1, hi, comparator);
    }

    public int partition(Point[] points, int lo, int hi, Comparator<Point> comparator)
    {
        // Increment index i while on element less than partitioning element
        // Decrement index j while on element greater than partitioning element
        // Exchange values
        // Repeat until indexes have crossed (i.e j <= i)
        // Move partitioning element to index j

        int i = lo;
        int j = hi+1;

        while (true) {
            while (less(points[i++], points[lo], comparator))
                if (i == hi) break;

            while (less(points[lo], points[--j], comparator))
                if (j == lo) break;

            if (j <= i) break;
            exch(points, i, j);
        }

        exch(points, lo, j);
        return j;
    }


    private boolean less(Point a, Point b, Comparator<Point> comparator)
    {
        if (comparator.compare(a, b) < 0) return true;
        else                              return false;
    }

    private void exch(Point[] points, int i, int j)
    {
        Point x = points[i];
        points[i] = points[j];
        points[j] = x;
    }

    private static void printPoints(Point[] points)
    {
        System.out.println();
        System.out.print("[");
        for (Point p : points ) {
            System.out.print(p + ",");
        }
        System.out.print("]");
        System.out.println();
    }

    private static void assertSort(QuickSort tester, Point[] expected)
    {
        for (int i = 0; i < expected.length; i++) {
            assert (tester.sortedPoints[i].equals(expected[i])) : i + ": expected " +
                    expected[i] + " got " + tester.sortedPoints[i];
        }
    }

    private static void printSlopes(Point[] points, Point qp)
    {
        int i = 0;
        System.out.println();
        for (Point p : points) {
            System.out.println(i++ + ": " + p + " -> " + qp.slopeTo(p));
        }
        System.out.println();
    }

    public static void main(String[] args)
    {
        Point testPoint1  = new Point(1, -3);
        Point testPoint2  = new Point(0, -1);
        Point testPoint3  = new Point(3, -1);
        Point testPoint4  = new Point(4, 0);
        Point testPoint5  = new Point(2, -3);
        Point testPoint6  = new Point(2, -2);
        Point testPoint7  = new Point(2, 0);
        Point testPoint8  = new Point(2, 1);
        Point testPoint9  = new Point(-2, -2);
        Point testPoint10 = new Point(-1, -1);
        Point testPoint11 = new Point(0, -2);
        Point testPoint12 = new Point(-3, 1);
        Point testPoint13 = new Point(2, 2);
        Point testPoint14 = new Point(-4, 2);
        Point testPoint15 = new Point(3, -3);
        Point testPoint16 = new Point(-2, -3);
        Point testPoint17 = new Point(-3, -3);
        Point testPoint18 = new Point(2, -4);

        Point[] points;
        Comparator<Point> comparator;
        Point[] expectedSort;
        QuickSort sortTester;

        // test two points array
        points = new Point[] {testPoint1, testPoint2};
        comparator = testPoint2.slopeOrder();
        expectedSort =  new Point[] {testPoint2, testPoint1};
        sortTester = new QuickSort(points, comparator);
        assertSort(sortTester, expectedSort);
        System.out.println("PASSED: two points array");

        // test three points array
        points = new Point[] {testPoint1, testPoint2, testPoint3};
        comparator = testPoint1.slopeOrder();
        expectedSort = new Point[] {testPoint1, testPoint2, testPoint3};
        sortTester = new QuickSort(points, comparator);
        assertSort(sortTester, expectedSort);
        System.out.println("PASSED: three points array");

        // test five points array
        points = new Point[] {testPoint1, testPoint14, testPoint3, testPoint11,
                              testPoint7};
        comparator = testPoint3.slopeOrder();
        expectedSort = new Point[] {testPoint3, testPoint7, testPoint14, testPoint11,
                                    testPoint1};
        sortTester = new QuickSort(points, comparator);
        printSlopes(points, testPoint3);
        printPoints(points);
        splits(points, 0, points.length);
        assertSort(sortTester, expectedSort);
        System.out.println("PASSED: five points array");





        //splits(points, 0, points.length);

        // Point[] points = {testPoint1, testPoint2, testPoint3, testPoint4, testPoint5,
        //                   testPoint6, testPoint7, testPoint8, testPoint9, testPoint10,
        //                   testPoint11, testPoint12, testPoint13, testPoint14, testPoint15,
        //                   testPoint16, testPoint17, testPoint18};

        //FastCollinearPoints FCpoints = new FastCollinearPoints(points);

        // for (int i = 0; i < points.length; i++) {
        //     System.out.println("Point: " + points[i].toString());
        //     System.out.println();
        //     for (int j = 0; j < points.length; j++) {
        //             System.out.print("Slope to: " + points[j].toString());
        //             System.out.print(" = " + points[i].slopeTo(points[j]) + "\n");
        //     }
        //     System.out.println();
        // }
        // Test Sorting with each testPoint

        // Comparator<Point> slopeComparator;
        // // testPoint 1 ====================>
        // slopeComparator = testPoint1.slopeOrder();
        // FCPointsTester ts1 = new FCPointsTester(points, slopeComparator);
        // Point[] expectedSort = {testPoint1, testPoint2, testPoint10, testPoint11,
        //                         testPoint12, testPoint14, testPoint18, testPoint9,
        //                         testPoint5, testPoint15, testPoint16, testPoint17,
        //                         testPoint3, testPoint4, testPoint6, testPoint7,
        //                         testPoint8, testPoint13};
        //
        // System.out.println();
        //
        // System.out.println(slopeComparator.compare(testPoint3, testPoint14));
        //
        // for (Point p : ts1.sortedPoints) {
        //     System.out.println(p.toString() + " -> " + testPoint1.slopeTo(p));
        // }
        //
    }
}
