import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private double[][] energy;
    private int[][] pixels;

    // false means the nested arrays in pixels represent columns
    // true means the nested arrays in pixels represent rows
    private boolean isTransposed = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {

        if (picture == null) throw new IllegalArgumentException("picture can not be null");

        int W = picture.width();
        int H = picture.height();
        this.energy = new double[W][H];
        this.pixels = new int[W][H];

        for (int x = 0;  x < W; x++) {
            for (int y = 0; y < H; y++) {
                pixels[x][y] = picture.getRGB(x, y);
                energy[x][y] = calcEnergy(x, y, picture);
            }
        }
    }

    // current picture
    public Picture picture() {

        Picture currentPic = new Picture(width(), height());

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {

                int pixelValue = !isTransposed ? pixels[x][y] : pixels[y][x];
                currentPic.setRGB(x, y, pixelValue);
            }
        }

        return currentPic;
    }

    // width of current picture
    public int width() {
        return !isTransposed ? pixels.length : pixels[0].length;
    }

    // height of current picture
    public int height() {
        return !isTransposed ? pixels[0].length : pixels.length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {

        if (!isPixelInRange(x, y)) throw new IllegalArgumentException("pixel is out of range");

        return energy[x][y];
    }

    private double calcEnergy(int x, int y, Picture picture) {

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
        else if (height() <= 1) throw new IllegalArgumentException("picture too short to remove seam");

        int[][] newPixels = new int[width()][height() - 1];
        double[][] newEnergy = new double[width()][height() - 1];

        for (int x = 0; x < width(); x++) {

            int y = seam[x];
            if (!isPixelInRange(x, y)) {
                throw new IllegalArgumentException("entry (" + x + "," + y + ") is out of range");
            } else if (seamHasGap(seam, x)) {
                throw new IllegalArgumentException("seam gap on idx " + x);
            }

            if (isTransposed) transpose();
            removeSeamPixel(x, y, newPixels, newEnergy);
        }

        pixels = newPixels;
        energy = newEnergy;
    }

    private void removeSeamPixel(int x, int y, int[][] newPixels, double[][] newEnergy) {

        int length = !isTransposed ? height() : width();
        int i = !isTransposed ? x : y;
        int j = !isTransposed ? y : x;

        if (j == 0) {
            System.arraycopy(pixels[i], 1, newPixels[i], 0, length - 1);
            System.arraycopy(energy[i], 1, newEnergy[i], 0, length - 1);
        } else if (j == length - 1) {
            System.arraycopy(pixels[i], 0, newPixels[i], 0, length - 1);
            System.arraycopy(energy[i], 0, newEnergy[i], 0, length - 1);
        } else {
            // 1st half, top if !transposed, left if transposed
            System.arraycopy(pixels[i], 0, newPixels[i], 0, j);
            System.arraycopy(energy[i], 0, newEnergy[i], 0, j);

            // 2nd half, bottom if !transp., right if transp.
            System.arraycopy(pixels[i], j + 1, newPixels[i], j, length - 1 - j);
            System.arraycopy(energy[i], j + 1, newEnergy[i], j, length - 1 - j);
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

        if (seam == null) throw new IllegalArgumentException("seam can not be null");
        else if (seam.length != height()) throw new IllegalArgumentException("seam of incorrect length");
        else if (width() <= 1) throw new IllegalArgumentException("picture too narrow to remove seam");

        int[][] newPixels = new int[height()][width() - 1];
        double[][] newEnergy = new double[height()][width() - 1];

        for (int y = 0; y < height(); y++) {

            int x = seam[y];
            if (!isPixelInRange(x, y)) {
                throw new IllegalArgumentException("entry (" + x + "," + y + ") is out of range");
            } else if (seamHasGap(seam, y)) {
                throw new IllegalArgumentException("seam gap on idx: " + y);
            }

            // transpose (if needed) so that the nested arrays (in the pixels array) are the rows
            if (!isTransposed) transpose();
            removeSeamPixel(x, y, newPixels, newEnergy);
        }

        pixels = newPixels;
        energy = newEnergy;
    }

    private boolean seamHasGap(int[] seam, int idx) {

        if (idx == seam.length - 1) return false;

        return Math.abs(seam[idx] - seam[idx + 1]) > 1;
    }

    private void transpose() {

        int[][] transposedPixels = new int[pixels[0].length][pixels.length];
        double[][] transposedEnergy = new double[pixels[0].length][pixels.length];

        for (int i = 0; i < pixels[0].length; i++) {
            for (int j = 0; j < pixels.length; j++) {
                transposedPixels[i][j] = pixels[j][i];
                transposedEnergy[i][j] = energy[j][i];
            }
        }

        pixels = transposedPixels;
        energy = transposedEnergy;
        isTransposed = !isTransposed;
    }

    private boolean isPixelInRange(int x, int y) {
        return (x >= 0 && x < width() && y >= 0 && y < height());
    }

    public static void main(String[] args) {

    }
}
