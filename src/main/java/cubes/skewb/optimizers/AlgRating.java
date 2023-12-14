package cubes.skewb.optimizers;

public class AlgRating {
    private final double score;
    private final String optimizedAlg;

    public AlgRating(double score, String optimizedAlg) {
        this.score = score;
        this.optimizedAlg = optimizedAlg;
    }

    public double getScore() {
        return score;
    }

    public String getOptimizedAlg() {
        return optimizedAlg;
    }
}