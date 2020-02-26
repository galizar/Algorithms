import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints
{
    private ArrayList<LineSegment> segments = new ArrayList<>();
    private int numberOfSegments = 0;
    private boolean calculatedNSegments = false;
    private Point[] points;

    public FastCollinearPoints(Point[] points)
    {
        if (points == null) {
            throw new IllegalArgumentException("points array should not be null");
        } else if (anyNull(points)) {
            throw new IllegalArgumentException("points array should not contain null points");
        }

        Arrays.sort(points);
        if (duplicates(points)) {
            throw new IllegalArgumentException("points array should not countain duplicate points");
        }

        // for each point p
        // calculate the slope with respect to the rest of points
        // sort the points according the slopes they make with p

        // Check if any 3 (or more) adjacent points in the sorted order have equal
        // slopes with respect to p. If so, these points, together with p, are collinear.


        int ncollin; // represents the number of collinear points to the current point in the loop
                     // it does not count the current point
        final int lo = 0;
        final int hi = points.length;
        int startCollinSeq = 0;
        int endCollinSeq = 0;
        Point currPoint;
        Point[] sortedPoints;
        LineSegment newSegment;
        Comparator<Point> slopeComparator;

        for (int i = 0; i < points.length; i++) {
            ncollin = 1;
            currPoint = points[i];
            slopeComparator = currPoint.slopeOrder();
            sortedPoints = sort(points, lo, hi, slopeComparator);

            // System.out.println();
            // for (Point p : sortedPoints) System.out.println(p + " " + currPoint.slopeTo(p));
            // System.out.println();

            for (int j = 1; j < points.length-1; j++) {
                if (slopeComparator.compare(sortedPoints[j], sortedPoints[j+1]) == 0) {
                    ncollin++;

                    if (ncollin == 2) {
                        startCollinSeq = j;
                    } else if (ncollin >= 3) {
                        endCollinSeq = j+2;
                    }

                    if (ncollin >= 3 && (j+2 == points.length)) {
                        addSegment(sortedPoints, currPoint, ncollin, startCollinSeq, endCollinSeq);
                    }
                } else if (ncollin >= 3) {
                    addSegment(sortedPoints, currPoint, ncollin, startCollinSeq, endCollinSeq);
                    ncollin = 1;
                } else if (ncollin > 1) {
                    ncollin = 1;
                }
            }

        }
    }     // finds all line segments containing 4 or more points


    /*
    *  This implementation requires the points to be sorted with Arrays.sort
    * */
    private boolean duplicates(Point[] points)
    {
        Point curr;
        Point next;

        for (int i = 0; i < points.length-1; i++) {
            curr = points[i];
            next = points[i+1];

            if (curr.compareTo(next) == 0) {
                System.out.println("Duplicate point: " + curr);
                return true;
            }
        }

        return false;
    }

    private boolean anyNull(Point[] points)
    {
        for (Point point : points) {
            if (point == null) return true;
        }

        return false;
    }

    /*
    *  Recursively sorts an array of points. Using a given comparator.
    *
    *  lo represents the start index (inclusive) of the subset to sort
    *  mid represents the start of the second half of the subset and
    *  hi represents the end index (exclusive) of the subset.
    * */
    private Point[] sort(Point[] points, int lo, int hi, Comparator<Point> comparator)
    {
        int n = hi-lo;
        int mid = lo + n / 2;

        // for sorting a sub-array where n = 3 this check is necessary:
        if (n == 1) return points;
        // both halves are "sorted" (they have one element only):
        else if (n == 2) return mergeSort(points, lo, mid, hi, comparator);

        points = sort(points, lo, mid, comparator); // left sort
        points = sort(points, mid, hi, comparator); // right sort

        return mergeSort(points, lo, mid, hi, comparator);
    }

    private Point[] mergeSort(Point[] points, int lo, int mid, int hi,
                              Comparator<Point> comparator)
    {
        Point[] aux = new Point[points.length];
        for (int i = 0; i < aux.length; i++) aux[i] = points[i];


        int k = lo; // current idx in aux
        int i = lo; // current idx in left half
        int j = mid; // current idx in right half

        while (k < hi) {
            if (i == mid) {
                // fill with the rest of points in the right half
                aux[k++] = points[j++];
            } else if (j == hi) {
                // fill with the rest of points in the left half
                aux[k++] = points[i++];
            } else {

                if (comparator.compare(points[i], points[j]) == 1) {
                    // left half element is bigger, fill with right half
                    aux[k++] = points[j++];
                } else {
                    // left half element is smaller or equal, fill with left half
                    aux[k++] = points[i++];
                }
            }
        }

        return aux;
    }

    private void addSegment(Point[] points, Point currPoint, int ncollin, int start, int end) {
        Point[] collinPoints = new Point[ncollin+1];
        collinPoints[0] = currPoint;
        Point[] restOfCollinP = pointsSubset(points, start, end);

        for (int k = 0; k < restOfCollinP.length; k++) {
            collinPoints[k+1] = restOfCollinP[k];
        }

        restOfCollinP = null; // free memory
        Point[] extremes = this.extremes(collinPoints);

        if (currPoint.compareTo(extremes[0]) == 0) { // only add if currPoint is the min in the seg.
            numberOfSegments++;
            segments.add(new LineSegment(extremes[0], extremes[1]));
        }
    }

    public int numberOfSegments() // the number of line segments
    {
        return numberOfSegments;
    }

    public LineSegment[] segments() // the line segments
    {
        LineSegment[] s = new LineSegment[numberOfSegments];
        return segments.toArray(s);
    }

    private Point[] pointsSubset(Point[] points, int start, int end)
    {
        if (end == start) {
            Point[] subset = { points[start] };
            return subset;
        }
        else {
            Point[] subset = new Point[end-start];
            int k = 0;

            for (int i = start; i < end; i++) {
                subset[k++] = points[i];
            }
            return subset;
        }
    }

   /*
   *  Point[] -> Point[]
   *  Returns array of extreme points of given array of points.
   *  First index contains minimum, second index contains max.
   *
   * */
    private Point[] extremes(Point[] points) {
        Point min = points[0];
        Point max = points[0];

        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(min) == -1) {
                min = points[i];
            } else if (points[i].compareTo(max) == 1) {
                max = points[i];
            }
        }

        Point[] extremes = {min, max};
        return extremes;
    }

    public static void main(String[] args)
    {
        // Point testPoint1  = new Point(1, -3);
        // Point testPoint2  = new Point(0, -1);
        // Point testPoint3  = new Point(3, -1);
        // Point testPoint4  = new Point(4, 0);
        // Point testPoint5  = new Point(2, -3);
        // Point testPoint6  = new Point(2, -2);
        // Point testPoint7  = new Point(2, 0);
        // Point testPoint8  = new Point(2, 1);
        // Point testPoint9  = new Point(-2, -2);
        // Point testPoint10 = new Point(-1, -1);
        // Point testPoint11 = new Point(0, -2);
        // Point testPoint12 = new Point(-3, 1);
        // Point testPoint13 = new Point(2, 2);
        // Point testPoint14 = new Point(-4, 2);
        // Point testPoint15 = new Point(3, -3);
        // Point testPoint16 = new Point(-2, -3);
        // Point testPoint17 = new Point(-3, -3);
        // Point testPoint18 = new Point(2, -4);
        //
        // int expectedNSegments = 9;
        //
        // Point[] testPoints = {null, testPoint2, testPoint3, testPoint4, testPoint5,
        //                       testPoint6, testPoint7, testPoint8, testPoint9, testPoint10,
        //                       testPoint11, testPoint12, testPoint13, testPoint14, testPoint15,
        //                       testPoint16, testPoint17, testPoint18};
        //
        // FastCollinearPoints collinPoints = new FastCollinearPoints(testPoints);


        //read the n points from a file

        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        System.out.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
