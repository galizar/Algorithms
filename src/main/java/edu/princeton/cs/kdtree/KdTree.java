/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;

public class KdTree {
    private static final int VERTICAL = 0;
    private static final int HORIZONTAL = 1;

    private TwoDTree points;

    public KdTree()
    {
        points = new TwoDTree();
    }

    public boolean isEmpty()
    {
        return points.isEmpty();
    }

    public int size()
    {
        return points.size();
    }

    public void insert(Point2D p)
    {
        if (p == null) throw new IllegalArgumentException("null argument");
        points.put(p);
    }

    public boolean contains(Point2D p)  // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException("null argument");
        return points.contains(p);
    }

    public void draw()  // draw all points to standard draw
    {
        points.draw();
    }

    public Iterable<Point2D> range(RectHV rect)   // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException("null argument");
        return points.inside(rect);
    }

    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException("null argument");
        else if (points.size() == 0) return null;

        return points.nearest(p);
    }

    private class TwoDTree {
        private static final boolean RED = true;
        private static final boolean BLACK = false;
        private static final int VERTICAL = 0;
        private static final int HORIZONTAL = 1;

        private static final char LEFT = 'l';
        private static final char RIGHT = 'r';

        private Node root;     // root of the BST

        // 2DTree node helper node data type
        private class Node implements Comparable<Node> {
            private Point2D point;
            private RectHV rectangle;  // the sub rectangle the point is cointained in
            private Node parent;       // link to parent node
            private Node left, right;  // links to left and right subtrees
            private int size;          // subtree count
            private int split;         // the direction this node splits space

            public Node(Point2D p, int size) {
                this.point = p;
                this.size = size;
            }

            public Node(Point2D p) { // only for use in contains method
                this.point = p;
            }

            public int compareTo(Node that) {
                if (that.split == VERTICAL) {
                    return Double.compare(point.x(), that.point.x());
                }
                else {
                    return Double.compare(point.y(), that.point.y());
                }
            }

            public void flipSplit() {
                if (this.split == VERTICAL) this.split = HORIZONTAL;
                else this.split = VERTICAL;
            }

            public RectHV leftSubRect() {
                double subXMin = this.rectangle.xmin();
                double subYMin = this.rectangle.ymin();
                double subXMax;
                double subYMax;

                if (this.split == VERTICAL) {
                    subXMax = this.point.x();
                    subYMax = this.rectangle.ymax();
                }
                else {
                    subXMax = this.rectangle.xmax();
                    subYMax = this.point.y();
                }

                return new RectHV(subXMin, subYMin, subXMax, subYMax);
            }

            public RectHV rightSubRect() {
                double subXMin;
                double subYMin;
                double subXMax = this.rectangle.xmax();
                double subYMax = this.rectangle.ymax();

                if (this.split == VERTICAL) {
                    subXMin = this.point.x();
                    subYMin = this.rectangle.ymin();
                }
                else {
                    subXMin = this.rectangle.xmin();
                    subYMin = this.point.y();
                }

                return new RectHV(subXMin, subYMin, subXMax, subYMax);
            }
        }

        public TwoDTree() {

        }


        /***************************************************************************
         *  Node helper methods.
         ***************************************************************************/
        // number of node in subtree rooted at x; 0 if x is null
        private int size(Node x) {
            if (x == null) return 0;
            return x.size;
        }


        /**
         * Returns the number of key-value pairs in this symbol table.
         *
         * @return the number of key-value pairs in this symbol table
         */
        public int size() {
            return size(root);
        }

        /**
         * Is this symbol table empty?
         *
         * @return {@code true} if this symbol table is empty and {@code false} otherwise
         */
        public boolean isEmpty() {
            return root == null;
        }


        /***************************************************************************
         *  Points range and neighbor relations.
         ***************************************************************************/

        // returns all points in this tree that are inside a given rectangle
        public Iterable<Point2D> inside(RectHV rect) {
            Queue<Point2D> pointsInside = new Queue<Point2D>();
            inside(root, rect, pointsInside);
            return pointsInside;
        }

        // add to the queue points rooted at node h which are inside the given rectangle
        private void inside(Node h, RectHV rect, Queue<Point2D> q) {
            if (h == null) return;

            if (h.rectangle.intersects(rect)) {
                if (rect.contains(h.point)) q.enqueue(h.point);
                inside(h.left, rect, q);
                inside(h.right, rect, q);
            }
        }

        public Point2D nearest(Point2D p) {
            Point2D nearestPoint = nearest(root, new Node(p), root.point);

            return nearestPoint;
        }

        private Point2D nearest(Node x, Node h, Point2D nearest) {
            if (x == null) return nearest;

            double dist = x.point.distanceTo(h.point);
            double minDist = nearest.distanceTo(h.point);

            if (!x.point.equals(h.point)) {
                if (dist < minDist) {
                    nearest = x.point;
                    minDist = dist;
                }
            }

            boolean isLeftCloser;
            boolean isRightCloser;

            if (x.left != null && x.right != null) {
                isLeftCloser =  minDist > x.left.rectangle.distanceTo(h.point);
                isRightCloser = minDist > x.right.rectangle.distanceTo(h.point);

                if (isLeftCloser && isRightCloser) {
                    nearest = nearest(x.left, h, nearest);
                    nearest = nearest(x.right, h, nearest);
                } else if (isLeftCloser) {
                    nearest = nearest(x.left, h, nearest);
                } else if (isRightCloser) {
                    nearest = nearest(x.right, h, nearest);
                }
            } else if (x.left != null) {
                isLeftCloser =  minDist > x.left.rectangle.distanceTo(h.point);
                if (isLeftCloser) nearest = nearest(x.left, h, nearest);
            } else if (x.right != null) {
                isRightCloser = minDist > x.right.rectangle.distanceTo(h.point);
                if (isRightCloser) nearest = nearest(x.right, h, nearest);
            }

            return nearest;
        }


        /***************************************************************************
         *  Draw points and the subdivisions they produce
         ***************************************************************************/

        public void draw() {
            if (this.isEmpty()) return;
            draw(root);
        }

        // draws the point and the subdivision line
        private void draw(Node h) {
            if (h == null) return;

            Point2D p = h.point;
            RectHV rect = h.rectangle;

            StdDraw.setPenRadius(0.005);

            if (h.split == VERTICAL) {
                StdDraw.setPenColor(Color.RED);
                StdDraw.line(p.x(), rect.ymin(), p.x(), rect.ymax());
            }
            else {
                StdDraw.setPenColor(Color.BLUE);
                StdDraw.line(rect.xmin(), p.y(), rect.xmax(), p.y());
            }

            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.point(p.x(), p.y());

            draw(h.left);
            draw(h.right);
        }


        /***************************************************************************
         *  Standard BST search.
         ***************************************************************************/

        /**
         * Does this tree contain a node with the given point?
         *
         * @param point the point
         * @return {@code true} if this 2DTree contains {@code point} and {@code false} otherwise
         * @throws IllegalArgumentException if {@code point} is {@code null}
         */
        public boolean contains(Point2D point) {
            return contains(root, new Node(point));
        }

        private boolean contains(Node x, Node k) {
            if (x == null) return false;
            else if (x.point.equals(k.point)) return true;

            if (k.compareTo(x) < 0) return contains(x.left, k);
            else return contains(x.right, k);
        }

        /***************************************************************************
         *  Red-black tree insertion.
         ***************************************************************************/

        /**
         * Inserts the specified key into the symbol table, overwriting the old value with the new
         * value if the symbol table already contains the specified key. Deletes the specified key
         * (and its associated value) from this symbol table if the specified value is {@code
         * null}.
         *
         * @param p the point to add
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void put(Point2D p) {
            if (p == null) throw new IllegalArgumentException("first argument to put() is null");

            //System.out.println("start inserting " + p.toString());

            Node newNode = new Node(p, 1);

            root = put(root, newNode, 'n');

            //System.out.println("root " + root.point.toString());
            //System.out.println("end inserting " + p.toString());
            //System.out.println();
            // assert check();
        }

        // insert a new node in the subtree rooted at node h
        private Node put(Node h, Node newNode, char side) {
            if (h == null) {
                // System.out.println("inserting: ");
                // System.out.print(key.point.toString() + "\n");
                Node parent = newNode.parent;

                //if (parent != null) System.out.println(parent.point.toString());
                //System.out.println(side);

                if (parent == null || parent.split == HORIZONTAL)
                    newNode.split = VERTICAL;
                else
                    newNode.split = HORIZONTAL;

                if (side == LEFT) newNode.rectangle = parent.leftSubRect();
                else if (side == RIGHT) newNode.rectangle = parent.rightSubRect();
                else newNode.rectangle = new RectHV(0.0, 0.0, 1.0, 1.0);

                return newNode;
            }

            //if (newNode.parent != null) System.out.println("parent " + newNode.parent.point.toString());
            newNode.parent = h;

            int cmp = newNode.compareTo(h);
            //System.out.println("cmp: " + cmp);
            if (cmp < 0) h.left = put(h.left, newNode, LEFT);
            else h.right = put(h.right, newNode, RIGHT);

            h.size = size(h.left) + size(h.right) + 1;

            return h;
        }
    }

    public static void main(String[] args)
    {
        /* Points for testing */
        Point2D tp1 = new Point2D(0.7, 0.2);
        Point2D tp2 = new Point2D(0.5, 0.4);
        Point2D tp3 = new Point2D(0.2, 0.3);
        Point2D tp4 = new Point2D(0.4, 0.7);
        Point2D tp5 = new Point2D(0.9, 0.6);

        //TwoDTree test2DT = new TwoDTree();

    }
}
