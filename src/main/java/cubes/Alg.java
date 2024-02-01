package cubes;

public class Alg {
    private String algorithm;
    private float rating;

    public Alg(String alg){
        this.algorithm = alg;
        this.rating = 0.0f;
    }

    public Alg(String algorithm, float rating) {
        this.algorithm = algorithm;
        this.rating = rating;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
