/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (compareTo(that) == 0)  return Double.NEGATIVE_INFINITY;
        else if (this.x == that.x) return Double.POSITIVE_INFINITY;

        double slope = (double) (this.y - that.y) / (this.x - that.x);

        if (slope == 0) return +0.0;
        return slope;
    }
    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        if (this.y > that.y) {
            return 1;
        } else if (this.y < that.y) {
            return -1;
        } else {
            if (this.x > that.x)       return 1;
            else if (this.x < that.x)  return -1;
            else                       return 0;
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new PointComparator();
    }

    private class PointComparator implements Comparator<Point> {
        public int compare(Point p1, Point p2) {
            double s1 = slopeTo(p1), s2 = slopeTo(p2);

            if      (s1 > s2)  return 1;
            else if (s1 < s2)  return -1;
            else return 0;
        }
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        // Point testPoint0 = new Point(2, 4);
        // Point testPoint1 = new Point(4, 8);
        // Point testPoint2 = new Point(4, 6);
        // Point testPoint3 = new Point(3, 6);

        // int EQUAL = 0;
        // int BIGGER = 1;
        // int SMALLER = -1;
        //
        // assert testPoint0.compareTo(testPoint0) == EQUAL : "test for equal points failed";
        // assert testPoint1.compareTo(testPoint0) == BIGGER : "test for bigger points failed";
        // assert testPoint2.compareTo(testPoint1) == SMALLER : "test for smaller points failed";
        //
        // assert testPoint0.slopeTo(testPoint1) == 2 : "test generic slope failed";
        // assert testPoint0.slopeTo(testPoint0) == Double.NEGATIVE_INFINITY : "equal point test failed";
        // assert testPoint1.slopeTo(testPoint2) == Double.POSITIVE_INFINITY : "equal x test failed";
        // assert testPoint2.slopeTo(testPoint3) == 0 : "zero slope test failed";

        Point testPoint0 = new Point(3, 3);
        Point testPoint1 = new Point(1, 1);
        Point testPoint2 = new Point(2,2);
        Point testPoint3 = new Point(4, 4);

        Point[] points = {testPoint0, testPoint1, testPoint2, testPoint3};

        Point currHighest = points[0], currLowest = points[0];

        for (int i = 0; i < points.length; i++) {
            if (currHighest.compareTo(points[i]) == -1) {
                currHighest = points[i];
            } else if (currLowest.compareTo(points[i]) == 1) {
                currLowest = points[i];
            }
        }

        System.out.println("highest: " + currHighest);
        System.out.println("lowest " + currLowest);


        Point tpoint1 = new Point(0, 1);
        Point tpoint2 = new Point(2, 1);

        System.out.println(tpoint1.slopeTo(tpoint2));
    }
}
