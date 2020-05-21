import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

import java.awt.Color;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
            for (int y = 0; y < H; y++)
                pixels[x][y] = picture.getRGB(x, y);
        }

        updateEnergies(this.energy, this.pixels);
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

    public int width() {
        return !isTransposed ? pixels.length : pixels[0].length;
    }

    public int height() {
        return !isTransposed ? pixels[0].length : pixels.length;
    }

    public double energy(int x, int y) {

        if (!isPixelInRange(x, y)) throw new IllegalArgumentException("pixel is out of range");

        return !isTransposed ? energy[x][y] : energy[y][x];
    }

    private double calcEnergy(int x, int y, int[][] pixels) {

        if (x == 0 || x == W() - 1 || y == 0 || y == H() - 1) return 1000.0;

        int left = pixels[x - 1][y];
        int right = pixels[x + 1][y];
        int up = pixels[x][y - 1];
        int down = pixels[x][y + 1];

        double redXGradient = Math.abs(red(left) - red(right));
        double greenXGradient = Math.abs(green(left) - green(right));
        double blueXGradient = Math.abs(blue(left) - blue(right));

        double redYGradient = Math.abs(red(up) - red(down));
        double greenYGradient = Math.abs(green(up) - green(down));
        double blueYGradient = Math.abs(blue(up) - blue(down));

        double xGradientSq = Math.pow(redXGradient, 2) +
                Math.pow(greenXGradient, 2) +
                Math.pow(blueXGradient, 2);

        double yGradientSq = Math.pow(redYGradient, 2) +
                Math.pow(greenYGradient, 2) +
                Math.pow(blueYGradient, 2);

        return Math.sqrt(xGradientSq + yGradientSq);
    }

    // red component of a BufferedImage rgb int
    private int red(int rgb) {
        return (rgb >> 16) & 0x000000FF;
    }

    // green component of a BufferedImage rgb int
    private int green(int rgb) {
        return (rgb >> 8) & 0x000000FF;
    }

    // blue component of a BufferedImage rgb int
    private int blue(int rgb) {
        return (rgb) & 0x000000FF;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        if (!isTransposed) transpose();
        return findSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        if (isTransposed) transpose();
        return findSeam();
    }

    private int[] findSeam() {

        double[] energyTo = new double[width() * height()];
        int[] pixelTo = new int[width() * height()];
        IndexMinPQ<Double> pq = new IndexMinPQ<>(width() * height());


        // skip two top pixels each time.
        // traverse just enough sources so as to reach every relevant non-source pixel
        // doing this also helps to know which top pixel will be used for the seam

        for (int i = 2; i < W(); i += 3) {
            energyTo[i] = 0;
            pixelTo[i] = i;
            pq.insert(pixelIdx(i, 0), energy[i][0]);
        }

        for (int i = W(); i < width() * height(); i++)
            energyTo[i] = Double.POSITIVE_INFINITY;

        while (!pq.isEmpty()) {

            int pixelIdx = pq.delMin();

            for (int adjPixelIdx : adjPixels(pixelIdx)) {
                relax(pixelIdx, adjPixelIdx, energyTo, pixelTo, pq);
            }
        }

        int[] seam = new int[H()];
        buildSeam(energyTo, pixelTo, seam);
        return seam;
    }

    private int W() {
        return !isTransposed ? width() : height();
    }

    private int H() {
        return !isTransposed ? height() : width();
    }

    private void relax(int fromIdx,
                       int toIdx,
                       double[] energyTo,
                       int[] pixelTo,
                       IndexMinPQ<Double> pq) {

        int[] toPixelPos = pixelPos(toIdx);
        int iToPixel = toPixelPos[0];
        int jToPixel = toPixelPos[1];

        if (energyTo[toIdx] > energyTo[fromIdx] + energy[iToPixel][jToPixel]) {

            energyTo[toIdx] = energyTo[fromIdx] + energy[iToPixel][jToPixel];
            pixelTo[toIdx] = fromIdx;

            if (pq.contains(toIdx)) pq.decreaseKey(toIdx, energyTo[toIdx]);
            else                    pq.insert(toIdx, energyTo[toIdx]);
        }
    }

    // works only for images where W >= 3
    private void buildSeam(double[] energyTo, int[] pixelTo, int[] seam) {

        int nextSeamPixel = H() * W() - W(); // initial: first pixel of last row
        double leastEnergy = energyTo[nextSeamPixel];

        // find first the last pixel of the seam (a pixel on bottom row of least energy to source)
        // notice the jump over two pixels on each iteration. just need to go through enough
        // pixels so as to cover the whole SPT
        for (int idx = nextSeamPixel + 3; idx < W() * H(); idx += 3) {
            if (energyTo[idx] < leastEnergy) {
                nextSeamPixel = idx;
                leastEnergy = energyTo[idx];
            }
        }

        int nextPosToAdd;

        for (int i = seam.length - 1; i >= 0; i--) {
            nextPosToAdd = pixelPos(nextSeamPixel)[0];
            seam[i] = nextPosToAdd;
            nextSeamPixel = pixelTo[nextSeamPixel];
        }
    }

    private int[] adjPixels(int idx) {

        int[] adj;

        if (idx > W() * H() - W() - 1) {
            // pixel is on last row
            adj = new int[0];
            return adj;
        }
        else if (idx % W() == 0) { // idx pixel is in first column
            adj = new int[] {idx + W(),
                             idx + W() + 1};

        } else if ((idx + 1) % W() == 0) { // idx pixel is in last column
            adj = new int[] {idx + W() - 1,
                             idx + W()};

        } else {
            adj = new int[] {idx + W() - 1,
                             idx + W(),
                             idx + W() + 1};
        }
        return adj;
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
            removeSeamPixel(x, y, newPixels);
        }

        pixels = newPixels;

        updateEnergies(newEnergy, pixels);
        energy = newEnergy;
    }

    private void removeSeamPixel(int x, int y, int[][] newPixels) {

        int length = !isTransposed ? height() : width();
        int i = !isTransposed ? x : y;
        int j = !isTransposed ? y : x;

        if (j == 0) {
            System.arraycopy(pixels[i], 1, newPixels[i], 0, length - 1);
        } else if (j == length - 1) {
            System.arraycopy(pixels[i], 0, newPixels[i], 0, length - 1);
        } else {
            // 1st half, top if !transposed, left if transposed
            System.arraycopy(pixels[i], 0, newPixels[i], 0, j);

            // 2nd half, bottom if !transp., right if transp.
            System.arraycopy(pixels[i], j + 1, newPixels[i], j, length - 1 - j);
        }
    }

    private void updateEnergies(double[][] energy, int[][] pixels) {

        for (int i = 0; i < W(); i++) {
            for (int j = 0; j < H(); j++) {
                energy[i][j] = calcEnergy(i, j, pixels);
            }
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
            removeSeamPixel(x, y, newPixels);
        }

        pixels = newPixels;

        updateEnergies(newEnergy, pixels);
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

    private int pixelIdx(int i, int j) {
        return (j + 1) * W() - (W() - i);
    }

    private int[] pixelPos(int pixelIdx) {

        int j = Math.floorDiv(pixelIdx, W());
        int i = pixelIdx - (j * W());

        return new int[] {i, j};
    }

    public static void main(String[] args) {

    }
}
