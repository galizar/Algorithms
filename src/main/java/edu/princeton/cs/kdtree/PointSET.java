/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
   private SET<Point2D> points;

   public PointSET()        // construct an empty set of points
   {
       points = new SET<Point2D>();
   }

   public boolean isEmpty() // is the set empty?
   {
      return points.size() == 0;
   }

   public int size()   // number of points in the set
   {
      return points.size();
   }

   public void insert(Point2D p)   // add the point to the set (if it is not already in the set)
   {
      if (p == null) throw new IllegalArgumentException("null argument");
      points.add(p);
   }

   public boolean contains(Point2D p)  // does the set contain point p?
   {
      if (p == null) throw new IllegalArgumentException("null argument");
      return points.contains(p);
   }

   public void draw()  // draw all points to standard draw
   {
      for (Point2D p : points) {
         StdDraw.point(p.x(), p.y());
      }
   }

   public Iterable<Point2D> range(RectHV rect)   // all points that are inside the rectangle (or on the boundary)
   {
       if (rect == null) throw new IllegalArgumentException("null argument");

       Queue<Point2D> inRangePoints = new Queue<Point2D>();

       for (Point2D p : points) {
          if (rect.contains(p)) {
             inRangePoints.enqueue(p);
          }
       }

       return inRangePoints;
   }

   public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
   {
       if (p == null) throw new IllegalArgumentException("null argument");
       else if (points.size() == 0) return null;

       Point2D nearest = points.min();
       double minDist  = p.distanceTo(points.min());

       for (Point2D thisP : points) {
           if (p.distanceTo(thisP) < minDist) {
              nearest = thisP;
              minDist = p.distanceTo(thisP);
           }
       }

       return nearest;
   }

   public static void main(String[] args)  // unit testing of the methods (optional)
   {

   }
}

