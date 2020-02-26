/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdDraw;

public class TestStdDraw {
       public static void main(String[] args) {
           StdDraw.setPenRadius(0.005);
           StdDraw.setPenColor(StdDraw.RED);
           StdDraw.line(0.2, 0.2, 0.99, 0.2);
           StdDraw.setPenRadius(0.01);
           StdDraw.setPenColor(StdDraw.BLACK);
           StdDraw.point(0.5, 0.2);
       }
   }

