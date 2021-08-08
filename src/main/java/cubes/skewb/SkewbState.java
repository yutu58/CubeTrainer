package cubes.skewb;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SkewbState {
    private static final Map<Integer, Integer> opposites = new HashMap<>() {{
        put(0, 1);
        put(1, 0);
        put(2, 4);
        put(3, 5);
        put(4, 2);
        put(5, 3);
    }};

    public String name;

    public int[] top;
    public int[] bottom;
    public int[] frontLeft;
    public int[] frontRight;
    public int[] backRight;
    public int[] backLeft;

    //A solved "state" would be "00000 11111 22222 33333 44444 55555"
    public SkewbState(String state) {
        String[] temp = state.split(" ");
        this.top = Arrays.stream(temp[0].split("")).mapToInt(Integer::parseInt).toArray();
        this.bottom = Arrays.stream(temp[1].split("")).mapToInt(Integer::parseInt).toArray();
        this.frontLeft = Arrays.stream(temp[2].split("")).mapToInt(Integer::parseInt).toArray();
        this.frontRight = Arrays.stream(temp[3].split("")).mapToInt(Integer::parseInt).toArray();
        this.backRight = Arrays.stream(temp[4].split("")).mapToInt(Integer::parseInt).toArray();
        this.backLeft = Arrays.stream(temp[5].split("")).mapToInt(Integer::parseInt).toArray();
    }

    public SkewbState(int[] top, int[] bottom, int[] frontLeft, int[] frontRight, int[] backRight, int[] backLeft) {
        this.top = top;
        this.bottom = bottom;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backRight = backRight;
        this.backLeft = backLeft;
    }

    public boolean isSolved() {
        return Arrays.stream(this.top).distinct().count() == 1 &&
                Arrays.stream(this.bottom).distinct().count() == 1 &&
                Arrays.stream(this.frontLeft).distinct().count() == 1 &&
                Arrays.stream(this.frontRight).distinct().count() == 1 &&
                Arrays.stream(this.backRight).distinct().count() == 1 &&
                Arrays.stream(this.backLeft).distinct().count() == 1;
    }

    public SkewbState copy() {
        return new SkewbState(this.top.clone(), this.bottom.clone(), this.frontLeft.clone(),
                this.frontRight.clone(), this.backRight.clone(), this.backLeft.clone());
    }

    public void applyWCAMoves(int[] moves) {
        for (int i : moves) {
            switch (i) {
                case 0 -> this.applyR();
                case 1 -> this.applyRprime();
                case 2 -> this.applyL();
                case 3 -> this.applyLprime();
                case 4 -> this.applyU();
                case 5 -> this.applyUprime();
                case 6 -> this.applyB();
                case 7 -> this.applyBprime();
                case 8 -> this.applyX();
                case 9 -> this.applyXPrime();
                case 10 -> this.applyX2();
                case 11 -> this.applyY();
                case 12 -> this.applyYprime();
                case 13 -> this.applyY2();
                case 14 -> this.applyZ();
                case 15 -> this.applyZprime();
                case 16 -> this.applyZ2();
            }
        }
    }

    /**
     * This method changes the colorization of the cube so that the UF-Corner has white, red and green
     */

    public void recolorToScrambleOrientation() {
        int[] UFCorner = new int[]{this.top[2], this.frontLeft[1], this.frontRight[0]};
        Map<Integer, Integer> colorMap = new HashMap<>();
        colorMap.put(UFCorner[0], 0);
        colorMap.put(opposites.get(UFCorner[0]), 1);
        colorMap.put(UFCorner[1], 2);
        colorMap.put(opposites.get(UFCorner[1]), 4);
        colorMap.put(UFCorner[2], 3);
        colorMap.put(opposites.get(UFCorner[2]), 5);
        this.top = Arrays.stream(this.top).map(colorMap::get).toArray();
        this.bottom = Arrays.stream(this.bottom).map(colorMap::get).toArray();
        this.frontLeft = Arrays.stream(this.frontLeft).map(colorMap::get).toArray();
        this.frontRight = Arrays.stream(this.frontRight).map(colorMap::get).toArray();
        this.backRight = Arrays.stream(this.backRight).map(colorMap::get).toArray();
        this.backLeft = Arrays.stream(this.backLeft).map(colorMap::get).toArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkewbState)) return false;

        SkewbState that = (SkewbState) o;

        if (!Objects.equals(name, that.name)) return false;
        if (!Arrays.equals(top, that.top)) return false;
        if (!Arrays.equals(bottom, that.bottom)) return false;
        if (!Arrays.equals(frontLeft, that.frontLeft)) return false;
        if (!Arrays.equals(frontRight, that.frontRight)) return false;
        if (!Arrays.equals(backRight, that.backRight)) return false;
        return Arrays.equals(backLeft, that.backLeft);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(top);
        result = 31 * result + Arrays.hashCode(bottom);
        result = 31 * result + Arrays.hashCode(frontLeft);
        result = 31 * result + Arrays.hashCode(frontRight);
        result = 31 * result + Arrays.hashCode(backRight);
        result = 31 * result + Arrays.hashCode(backLeft);
        return result;
    }

    @Override
    public String toString() {
        return "SkewbState{" +
                "name='" + name + '\'' +
                ", top=" + Arrays.toString(top) +
                ", bottom=" + Arrays.toString(bottom) +
                ", frontLeft=" + Arrays.toString(frontLeft) +
                ", frontRight=" + Arrays.toString(frontRight) +
                ", backRight=" + Arrays.toString(backRight) +
                ", backLeft=" + Arrays.toString(backLeft) +
                '}';
    }

    public String toPattern() {
        //Maybe generalize to remove duplicate code here
        StringBuilder res = new StringBuilder();
        for (int i : top) {
            res.append(i);
        }
        res.append(' ');

        for (int i : bottom) {
            res.append(i);
        }
        res.append(' ');

        for (int i : frontLeft) {
            res.append(i);
        }
        res.append(' ');

        for (int i : frontRight) {
            res.append(i);
        }
        res.append(' ');

        for (int i : backRight) {
            res.append(i);
        }
        res.append(' ');

        for (int i : backLeft) {
            res.append(i);
        }
        return res.toString();
    }

    //TODO: change this to work with vectors instead of mess like this

    private void applyR() {
        int temp = backRight[4];
        backRight[4] = frontRight[4];
        frontRight[4] = bottom[4];
        bottom[4] = temp;

        temp = backRight[3];
        backRight[3] = frontRight[2];
        frontRight[2] = bottom[1];
        bottom[1] = temp;

        temp = backRight[2];
        backRight[2] = frontRight[1];
        frontRight[1] = bottom[0];
        bottom[0] = temp;

        temp = backRight[0];
        backRight[0] = frontRight[3];
        frontRight[3] = bottom[2];
        bottom[2] = temp;

        temp = frontLeft[2];
        frontLeft[2] = backLeft[3];
        backLeft[3] = top[1];
        top[1] = temp;

    }

    private void applyRprime() {
        int temp = frontRight[4];
        frontRight[4] = backRight[4];
        backRight[4] = bottom[4];
        bottom[4] = temp;

        temp = frontRight[3];
        frontRight[3] = backRight[0];
        backRight[0] = bottom[2];
        bottom[2] = temp;

        temp = frontRight[2];
        frontRight[2] = backRight[3];
        backRight[3] = bottom[1];
        bottom[1] = temp;

        temp = frontRight[1];
        frontRight[1] = backRight[2];
        backRight[2] = bottom[0];
        bottom[0] = temp;

        temp = backLeft[3];
        backLeft[3] = frontLeft[2];
        frontLeft[2] = top[1];
        top[1] = temp;
    }

    private void applyL() {
        int temp = frontLeft[4];
        frontLeft[4] = backLeft[4];
        backLeft[4] = bottom[4];
        bottom[4] = temp;

        temp = frontLeft[3];
        frontLeft[3] = backLeft[2];
        backLeft[2] = bottom[3];
        bottom[3] = temp;

        temp = frontLeft[2];
        frontLeft[2] = backLeft[1];
        backLeft[1] =  bottom[2];
        bottom[2] = temp;

        temp = frontLeft[0];
        frontLeft[0] = backLeft[3];
        backLeft[3] = bottom[0];
        bottom[0] = temp;

        temp = backRight[2];
        backRight[2] = frontRight[3];
        frontRight[3] = top[3];
        top[3] = temp;
    }

    private void applyLprime() {
        int temp = backLeft[4];
        backLeft[4] = frontLeft[4];
        frontLeft[4] = bottom[4];
        bottom[4] = temp;

        temp = backLeft[3];
        backLeft[3] = frontLeft[0];
        frontLeft[0] = bottom[0];
        bottom[0] = temp;

        temp = backLeft[2];
        backLeft[2] = frontLeft[3];
        frontLeft[3] = bottom[3];
        bottom[3] = temp;

        temp = backLeft[1];
        backLeft[1] = frontLeft[2];
        frontLeft[2] = bottom[2];
        bottom[2] = temp;

        temp = frontRight[3];
        frontRight[3] = backRight[2];
        backRight[2] = top[3];
        top[3] = temp;
    }

    private void applyU() {
        int temp = backRight[4];
        backRight[4] = backLeft[4];
        backLeft[4] = top[4];
        top[4] = temp;

        temp = backRight[2];
        backRight[2] = backLeft[1];
        backLeft[1] = top[1];
        top[1] = temp;

        temp = backRight[1];
        backRight[1] = backLeft[0];
        backLeft[0] = top[0];
        top[0] = temp;

        temp = backRight[0];
        backRight[0] = backLeft[3];
        backLeft[3] = top[3];
        top[3] = temp;

        temp = frontLeft[0];
        frontLeft[0] = frontRight[1];
        frontRight[1] = bottom[2];
        bottom[2] = temp;
    }

    private void applyUprime() {
        int temp = backLeft[4];
        backLeft[4] = backRight[4];
        backRight[4] = top[4];
        top[4] = temp;

        temp = backLeft[3];
        backLeft[3] = backRight[0];
        backRight[0] = top[3];
        top[3] = temp;

        temp = backLeft[1];
        backLeft[1] = backRight[2];
        backRight[2] = top[1];
        top[1] = temp;

        temp = backLeft[0];
        backLeft[0] = backRight[1];
        backRight[1] = top[0];
        top[0] = temp;

        temp = frontRight[1];
        frontRight[1] = frontLeft[0];
        frontLeft[0] = bottom[2];
        bottom[2] = temp;
    }

    private void applyB() {
        int temp = backLeft[4];
        backLeft[4] = backRight[4];
        backRight[4] = bottom[4];
        bottom[4] = temp;

        temp = backLeft[3];
        backLeft[3] = backRight[2];
        backRight[2] = bottom[2];
        bottom[2] = temp;

        temp = backLeft[2];
        backLeft[2] = backRight[1];
        backRight[1] = bottom[1];
        bottom[1] = temp;

        temp = backLeft[0];
        backLeft[0] = backRight[3];
        backRight[3] = bottom[3];
        bottom[3] = temp;

        temp = frontRight[2];
        frontRight[2] = frontLeft[3];
        frontLeft[3] = top[0];
        top[0] = temp;
    }

    private void applyBprime() {
        int temp = backRight[4];
        backRight[4] = backLeft[4];
        backLeft[4] = bottom[4];
        bottom[4] = temp;

        temp = backRight[3];
        backRight[3] = backLeft[0];
        backLeft[0] = bottom[3];
        bottom[3] = temp;

        temp = backRight[2];
        backRight[2] = backLeft[3];
        backLeft[3] = bottom[2];
        bottom[2] = temp;

        temp= backRight[1];
        backRight[1] = backLeft[2];
        backLeft[2] = bottom[1];
        bottom[1] = temp;

        temp = frontLeft[3];
        frontLeft[3] = frontRight[2];
        frontRight[2] = top[0];
        top[0] = temp;
    }

    private void applyX() {
        int temp = top[0];
        top[0] = frontLeft[0];
        frontLeft[0] = bottom[3];
        bottom[3] = backRight[2];
        backRight[2] = temp;

        temp = top[1];
        top[1] = frontLeft[1];
        frontLeft[1] = bottom[0];
        bottom[0] = backRight[3];
        backRight[3] = temp;

        temp = top[2];
        top[2] = frontLeft[2];
        frontLeft[2] = bottom[1];
        bottom[1] = backRight[0];
        backRight[0] = temp;

        temp = top[3];
        top[3] = frontLeft[3];
        frontLeft[3] = bottom[2];
        bottom[2] = backRight[1];
        backRight[1] = temp;

        temp = top[4];
        top[4] = frontLeft[4];
        frontLeft[4] = bottom[4];
        bottom[4] = backRight[4];
        backRight[4] = temp;

        temp = frontRight[0];
        frontRight[0] = frontRight[3];
        frontRight[3] = frontRight[2];
        frontRight[2] = frontRight[1];
        frontRight[1] = temp;

        temp = backLeft[0];
        backLeft[0] = backLeft[1];
        backLeft[1] = backLeft[2];
        backLeft[2] = backLeft[3];
        backLeft[3] = temp;
    }

    private void applyXPrime() {
        applyX();
        applyX();
        applyX();
    }

    private void applyX2() {
        applyX();
        applyX();
    }

    private void applyY() {
        for (int i = 0; i < 5; i++) {
            int temp = frontLeft[i];
            frontLeft[i] = frontRight[i];
            frontRight[i] = backRight[i];
            backRight[i] = backLeft[i];
            backLeft[i] = temp;
        }

        int temp = top[0];
        top[0] = top[3];
        top[3] = top[2];
        top[2] = top[1];
        top[1] = temp;

        temp = bottom[0];
        bottom[0] = bottom[1];
        bottom[1] = bottom[2];
        bottom[2] = bottom[3];
        bottom[3] = temp;
    }

    private void applyYprime() {
        applyY();
        applyY();
        applyY();
    }

    private void applyY2() {
        applyY();
        applyY();
    }

    private void applyZ() {
        int temp = top[0];
        top[0] = backLeft[3];
        backLeft[3] = bottom[1];
        bottom[1] = frontRight[1];
        frontRight[1] = temp;

        temp = top[1];
        top[1] = backLeft[0];
        backLeft[0] = bottom[2];
        bottom[2] = frontRight[2];
        frontRight[2] = temp;

        temp = top[2];
        top[2] = backLeft[1];
        backLeft[1] = bottom[3];
        bottom[3] = frontRight[3];
        frontRight[3] = temp;

        temp = top[3];
        top[3] = backLeft[2];
        backLeft[2] = bottom[0];
        bottom[0] = frontRight[0];
        frontRight[0] = temp;

        temp = top[4];
        top[4] = backLeft[4];
        backLeft[4] = bottom[4];
        bottom[4] = frontRight[4];
        frontRight[4] = temp;

        temp = frontLeft[0];
        frontLeft[0] = frontLeft[3];
        frontLeft[3] = frontLeft[2];
        frontLeft[2] = frontLeft[1];
        frontLeft[1] = temp;

        temp = backRight[0];
        backRight[0] = backRight[1];
        backRight[1] = backRight[2];
        backRight[2] = backRight[3];
        backRight[3] = temp;
    }

    private void applyZprime() {
        applyZ();
        applyZ();
        applyZ();
    }

    private void applyZ2() {
        applyZ();
        applyZ();
    }
}
