import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static Percolation percolation;
    private double[] sampleMeans;
    private double mean;
    private double std;
    private double confInterLo;
    private double confInterHi;

    public PercolationStats(int n, int trials)
    {

        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Both N and TRIALS must be positive integers");
        }

        int randrow;
        int randcol;
        sampleMeans = new double[trials];

        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);

            while (!percolation.percolates()) {
                randrow = StdRandom.uniform(n) + 1;
                randcol = StdRandom.uniform(n) + 1;

                //System.out.print(percolation.numberOfOpenSites() + "\n");

                percolation.open(randrow, randcol);
            }

            sampleMeans[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }

        this.mean = StdStats.mean(sampleMeans);
        this.std = StdStats.stddev(sampleMeans);
        this.confInterLo = this.mean - 1.96 * this.std / Math.sqrt(trials);
        this.confInterHi = this.mean + 1.96 * this.std / Math.sqrt(trials);

    }

    public double mean()
    {
        return this.mean;
    }

    public double stddev()
    {
        return this.std;
    }

    public double confidenceLo()
    {
        return this.confInterLo;
    }

    public double confidenceHi()
    {
        return this.confInterHi;
    }

    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        double mean =        stats.mean();
        double std =         stats.stddev();
        double confInterLo = stats.confidenceLo();
        double confInterHi = stats.confidenceHi();

        System.out.print("mean                    = " + mean + "\n");
        System.out.print("stddev                  = " + std + "\n");
        System.out.print("95% confidence interval = [" + confInterLo + ", " + confInterHi + "]\n");

    }
}


