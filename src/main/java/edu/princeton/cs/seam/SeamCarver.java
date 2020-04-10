import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private Picture picture;
    private double[][] energy;
    private int[][] pixels;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {

        if (picture == null) throw new IllegalArgumentException("picture can not be null");
        this.picture = new Picture(picture);
        this.energy = new double[width()][height()];

        for (int x = 0;  x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                pixels[x][y] = picture.getRGB(x, y);
                energy[x][y] = calcEnergy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {

        if (!isPixelInRange(x, y)) throw new IllegalArgumentException("pixel is out of range");

        return energy[x][y];
    }

    private double calcEnergy(int x, int y) {

        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) return 1000.0;

        Color left = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);
        Color up = picture.get(x, y - 1);
        Color down = picture.get(x, y + 1);

        double redXGradient = Math.abs(left.getRed() - right.getRed());
        double greenXGradient = Math.abs(left.getGreen() - right.getGreen());
        double blueXGradient = Math.abs(left.getBlue() - right.getBlue());

        double redYGradient = Math.abs(up.getRed() - down.getRed());
        double greenYGradient = Math.abs(up.getGreen() - down.getGreen());
        double blueYGradient = Math.abs(up.getBlue() - down.getBlue());

        double xGradientSq = Math.pow(redXGradient, 2) +
                Math.pow(greenXGradient, 2) +
                Math.pow(blueXGradient, 2);

        double yGradientSq = Math.pow(redYGradient, 2) +
                Math.pow(greenYGradient, 2) +
                Math.pow(blueYGradient, 2);

        return Math.sqrt(xGradientSq + yGradientSq);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return new int[0];
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return new int[0];
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

        if (seam == null) throw new IllegalArgumentException("seam can not be null");
        else if (seam.length != width()) throw new IllegalArgumentException("seam of incorrect length");
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

        if (seam == null) throw new IllegalArgumentException("seam can not be null");
        else if (seam.length != height()) throw new IllegalArgumentException("seam of incorrect length");
    }

    private boolean isPixelInRange(int x, int y) {
        return (x >= 0 && x < width() && y >= 0 && y < height());
    }

    public static void main(String[] args) {

    }
}
