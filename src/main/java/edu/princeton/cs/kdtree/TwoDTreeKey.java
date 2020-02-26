/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class TwoDTreeKey implements Comparable<TwoDTreeKey>
{
    private static final int VERTICAL = 0;
    private static final int HORIZONTAL = 1;

    public Point2D point;   // the point associated with this key
    public int spaceSplit;  // the way the point splits space. 0 means vertical, 1 horizontal
    public RectHV rectangle;

    // initialization when inserting a key
    public TwoDTreeKey(Point2D p)
    {
        this.point = p;
    }

    public TwoDTreeKey(Point2D p, int split)
    {
        this.point = p;
        this.spaceSplit = split;
    }

    public int compareTo(TwoDTreeKey that)
    {
        if (that.spaceSplit == VERTICAL) {
            return Double.compare(point.x(), that.point.x());
        } else {
            return Double.compare(point.y(), that.point.y());
        }
    }

    public RectHV leftSubRect()
    {
        double subXMin = this.rectangle.xmin();
        double subYMin = this.rectangle.ymin();
        double subXMax;
        double subYMax;

        if (this.spaceSplit == VERTICAL) {
            subXMax = this.point.x();
            subYMax = this.rectangle.ymax();
        } else {
            subXMax = this.rectangle.xmax();
            subYMax = this.point.y();
        }

        return new RectHV(subXMin, subYMin, subXMax, subYMax);
    }

    public RectHV rightSubRect()
    {
        double subXMin;
        double subYMin;
        double subXMax = this.rectangle.xmax();
        double subYMax = this.rectangle.ymax();

        if (this.spaceSplit == VERTICAL) {
            subXMin = this.point.x();
            subYMin = this.rectangle.ymin();
        } else {
            subXMin = this.rectangle.xmin();
            subYMin = this.point.y();
        }

        return new RectHV(subXMin, subYMin, subXMax, subYMax);
    }
}

