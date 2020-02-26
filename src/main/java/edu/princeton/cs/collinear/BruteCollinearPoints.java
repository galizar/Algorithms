/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints
{
    private LineSegment[] segments = new LineSegment[0];
    private int numberOfSegments;
    private boolean calculatedNSegments = false;
    private int pos = 0;

    public BruteCollinearPoints(Point[] points)
    {
        if (points.length < 4) return;

        verifyCorrectenessOfInputPoints(points);
        calculateCombinationsAndSegments(points, 4);
    }


    private void verifyCorrectenessOfInputPoints(Point[] points)
    {
        if (points == null) {
            throw new IllegalArgumentException("points array should not be null");
        } else if (anyNull(points)) {
            throw new IllegalArgumentException("points array should not contain null points");
        } else if (duplicates(points)) {
            throw new IllegalArgumentException("points array should not countain duplicate points");
        }
    }


    private boolean duplicates(Point[] points)
    {
        Point curr;
        Point next;

        for (int i = 0; i < points.length-1; i++) {
            curr = points[i];
            next = points[i+1];

            if (curr.compareTo(next) == 0) return true;
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


    private void calculateCombinationsAndSegments(Point[] arr, int k)
    {
        int n = arr.length;
        Point[] pointComb = new Point[k];

        produceCombinations(arr, pointComb, 0, n-1, 0, k);

        calculatedNSegments = true;
        segments = new LineSegment[numberOfSegments];

        produceCombinations(arr, pointComb, 0, n-1, 0, k);
    }


    // Thanks to Davesh Agrawal for the combinations logic
    private void produceCombinations(Point[] arr, Point[] pointComb, int start,
                                    int end, int index, int k)
    {
        // combination is ready
        if (index == k) {
            if (collinear(pointComb)) {
                if (!calculatedNSegments) numberOfSegments++;
                else {
                    Point[] extremes = extremes(pointComb);

                    if (notInSegments(new LineSegment(extremes[0], extremes[1]))) {
                        segments[pos++] = new LineSegment(extremes[0], extremes[1]);
                    }
                }
            }
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i = start; i <= end && end-i+1 >= k-index; i++) {
            pointComb[index] = arr[i];
            produceCombinations(arr, pointComb, i+1, end, index+1, k);
        }
    }

    private boolean collinear(Point[] points)
    {
        double requiredSlope = points[0].slopeTo(points[1]);
        double slope;

        for (int i = 1; i < points.length-1; i++) {
            slope = points[i].slopeTo(points[i+1]);
            if (slope != requiredSlope) return false;
        }

        return true;
    }

    private Point[] extremes(Point[] points) {
        Point min = points[0];
        Point max = points[0];

        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(min) < 0) {
                min = points[i];
            } else if (points[i].compareTo(max) > 0) {
                max = points[i];
            }
        }

        Point[] extremes = {min, max};
        return extremes;
    }

    private boolean notInSegments(LineSegment ls)
    {
        if (pos == 0) return true;
        for (int i = 0; i < pos; i++) {
            if (segments[i].toString().equals(ls.toString())) {
                return false;
            }
        }
        return true;
    }

    public int numberOfSegments()
    {
        return this.numberOfSegments;
    }


    public LineSegment[] segments()
    {
        return this.segments;
    }


    public static void main(String[] args)
    {
        // // w/collinear points (ascending), nsegments = 1 =====================
        // Point[] testPoints1 = new Point[4];
        //
        // for (int i = 0; i < testPoints1.length; i++) {
        //     testPoints1[i] = new Point(i+1, i+1);
        // }
        //
        // BruteCollinearPoints collinPoints = new BruteCollinearPoints(testPoints1);
        //
        // assert collinPoints.numberOfSegments() == 1 : "test failed";
        // assert collinPoints.segments().length == 1;
        //
        // String expectedPath = "(1, 1) -> (4, 4)";
        //
        // System.out.println(collinPoints.segments()[0].toString());
        // assert collinPoints.segments()[0].toString().equals(expectedPath);
        //
        // // w/collinear points (descending), nsegments = 1 =====================
        // // test for asserting that the correct order is kept in the LineSegments
        // Point[] testPoints2 = new Point[4];
        //
        // for (int i = 0; i < testPoints2.length; i++) {
        //     testPoints2[i] = new Point(4-i, 4-i);
        // }
        //
        // BruteCollinearPoints collinPoints2 = new BruteCollinearPoints(testPoints2);
        //
        // assert collinPoints2.numberOfSegments() == 1 : "test failed";
        // assert collinPoints2.segments().length == 1;
        //
        // System.out.println(collinPoints2.segments()[0].toString());
        // assert collinPoints2.segments()[0].toString().equals(expectedPath);
        //
        // // w/collinear points, nsegments = 20Choose4 = 4845 ====================
        // Point[] testPoints3 = new Point[20];
        //
        // for (int i = 0; i < testPoints3.length; i++) {
        //     testPoints3[i] = new Point(i+1, i+1);
        // }
        //
        // BruteCollinearPoints collinPoints3 = new BruteCollinearPoints(testPoints3);
        //
        // System.out.println(collinPoints3.numberOfSegments());
        //
        //
        // // w/no collinear points, nsegments = 0 ===========================
        // Point[] testPoints4 = new Point[4];
        //
        // Point tpoint0 = new Point(4, 7);
        // Point tpoint1 = new Point(8, 2);
        // Point tpoint2 = new Point(7, 3);
        // Point tpoint3 = new Point(2, 0);
        //
        // testPoints4[0] = tpoint0;
        // testPoints4[1] = tpoint1;
        // testPoints4[2] = tpoint2;
        // testPoints4[3] = tpoint3;
        //
        // BruteCollinearPoints collinPoints4 = new BruteCollinearPoints(testPoints4);
        //
        // assert collinPoints4.numberOfSegments() == 0;

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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);

        System.out.println();
        System.out.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
