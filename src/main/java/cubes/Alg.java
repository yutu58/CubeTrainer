package cubes;

public class Alg {
    private String algorithm;
    private int timesTimed;
    private int average;

    public Alg(String alg){
        this.algorithm = alg;
        this.timesTimed = 0;
        this.average = 0;
    }

    public Alg(String algorithm, int timesTimed, int average) {
        this.algorithm = algorithm;
        this.timesTimed = timesTimed;
        this.average = average;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int getTimesTimed() {
        return timesTimed;
    }

    public void setTimesTimed(int timesTimed) {
        this.timesTimed = timesTimed;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }
}
