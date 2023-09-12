package cubes.tx2;

import cubes.skewb.imageGenerators.UnknownMoveException;
import cubes.tx2.solvers.Tx2Scrambler;

import java.util.*;

public class Tx2State {
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
    public int[] left;
    public int[] front;
    public int[] right;
    public int[] back;

    //A solved "state" would be "0000 1111 2222 3333 4444 5555"
    public Tx2State(String state) {
        String[] temp = state.split(" ");
        this.top = Arrays.stream(temp[0].split("")).mapToInt(Integer::parseInt).toArray();
        this.bottom = Arrays.stream(temp[1].split("")).mapToInt(Integer::parseInt).toArray();
        this.left = Arrays.stream(temp[2].split("")).mapToInt(Integer::parseInt).toArray();
        this.front = Arrays.stream(temp[3].split("")).mapToInt(Integer::parseInt).toArray();
        this.right = Arrays.stream(temp[4].split("")).mapToInt(Integer::parseInt).toArray();
        this.back = Arrays.stream(temp[5].split("")).mapToInt(Integer::parseInt).toArray();
    }

    public Tx2State(int[] top, int[] bottom, int[] left, int[] front, int[] right, int[] back) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.front = front;
        this.right = right;
        this.back = back;
    }

    public boolean isSolved() {
        return Arrays.stream(this.top).distinct().count() == 1 &&
                Arrays.stream(this.bottom).distinct().count() == 1 &&
                Arrays.stream(this.left).distinct().count() == 1 &&
                Arrays.stream(this.front).distinct().count() == 1 &&
                Arrays.stream(this.right).distinct().count() == 1 &&
                Arrays.stream(this.back).distinct().count() == 1;
    }

    public Tx2State copy() {
        return new Tx2State(this.top.clone(), this.bottom.clone(), this.left.clone(),
                this.front.clone(), this.right.clone(), this.back.clone());
    }

    public void applyWCAMoves(int[] moves) {
        if (moves == null) return;
        for (int i : moves) {
            switch (i) {
                case 0 -> this.applyR();
                case 1 -> this.applyRprime();
                case 2 -> this.applyR2();
                case 3 -> this.applyU();
                case 4 -> this.applyUprime();
                case 5 -> this.applyU2();
                case 6 -> this.applyF();
                case 7 -> this.applyFprime();
                case 8 -> this.applyF2();
                case 9 -> this.applyX();
                case 10 -> this.applyXPrime();
                case 11 -> this.applyX2();
                case 12 -> this.applyY();
                case 13 -> this.applyYprime();
                case 14 -> this.applyY2();
                case 15 -> this.applyZ();
                case 16 -> this.applyZprime();
                case 17 -> this.applyZ2();
            }
        }
    }

    /**
     * This method changes the colorization of the cube so that the UF-Corner has white, red and green
     */

    public void recolorToScrambleOrientation() {
        int[] DBLCorner = new int[]{this.bottom[3], this.back[2], this.left[3]};
        Map<Integer, Integer> colorMap = new HashMap<>();
        colorMap.put(DBLCorner[0], 1);
        colorMap.put(opposites.get(DBLCorner[0]), 0);
        colorMap.put(DBLCorner[1], 5);
        colorMap.put(opposites.get(DBLCorner[1]), 3);
        colorMap.put(DBLCorner[2], 2);
        colorMap.put(opposites.get(DBLCorner[2]), 4);
        this.top = Arrays.stream(this.top).map(colorMap::get).toArray();
        this.bottom = Arrays.stream(this.bottom).map(colorMap::get).toArray();
        this.left = Arrays.stream(this.left).map(colorMap::get).toArray();
        this.front = Arrays.stream(this.front).map(colorMap::get).toArray();
        this.right = Arrays.stream(this.right).map(colorMap::get).toArray();
        this.back = Arrays.stream(this.back).map(colorMap::get).toArray();
    }

    public static Tx2State setupCase(String setup, Tx2Notations.notationEnum notation, boolean reverse) {
        Tx2State tx2State = new Tx2State("0000 1111 2222 3333 4444 5555");

        if (notation == Tx2Notations.notationEnum.WCA_RUF_NOTATION) {
            java.util.List<Integer> intMoves = new ArrayList<>();
            String[] moves = setup.trim().split(" ");
            for (String m : moves) {
                if (m.equals("")) {
                    continue;
                }
                int[] triedMoves = Tx2Notations.wcaRUFNotation.get(m);
                if (triedMoves == null) {
                    throw new UnknownMoveException(m + " is not a valid move!");
                }
                for (int i : triedMoves) {
                    intMoves.add(i);
                }
            }
            int[] arrIntMoves = intMoves.stream().mapToInt(i->i).toArray();
            if (reverse) {
                arrIntMoves = Tx2Scrambler.reverse2x2(arrIntMoves);
            }
            tx2State.applyWCAMoves(arrIntMoves);
        }
//        else if (notation == SkewbNotations.notationEnum.RubikSkewbNotation) {
//            List<Integer> intMoves = new ArrayList<>();
//            String[] moves = setup.trim().split(" ");
//            for (String m : moves) {
//                if (m.equals("")) {
//                    continue;
//                }
//                int[] triedMoves = SkewbNotations.rubikSkewbNotation.get(m);
//                if (triedMoves == null) {
//                    throw new UnknownMoveException(m + " is not a valid move!");
//                }
//                for (int i : triedMoves) {
//                    intMoves.add(i);
//                }
//            }
//            int[] arrIntMoves = intMoves.stream().mapToInt(i->i).toArray();
//            if (reverse) {
//                arrIntMoves = SkewbScrambler.reverseSkewb(arrIntMoves);
//            }
//            skewbState.applyWCAMoves(arrIntMoves);
//        }
        else if (notation == Tx2Notations.notationEnum.LITHIUM_2x2_CODE) {
            tx2State = new Tx2State(setup);
        }
        return tx2State;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tx2State)) return false;

        Tx2State that = (Tx2State) o;

        if (!Objects.equals(name, that.name)) return false;
        if (!Arrays.equals(top, that.top)) return false;
        if (!Arrays.equals(bottom, that.bottom)) return false;
        if (!Arrays.equals(left, that.left)) return false;
        if (!Arrays.equals(front, that.front)) return false;
        if (!Arrays.equals(right, that.right)) return false;
        return Arrays.equals(back, that.back);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(top);
        result = 31 * result + Arrays.hashCode(bottom);
        result = 31 * result + Arrays.hashCode(left);
        result = 31 * result + Arrays.hashCode(front);
        result = 31 * result + Arrays.hashCode(right);
        result = 31 * result + Arrays.hashCode(back);
        return result;
    }

    @Override
    public String toString() {
        return "Tx2State{" +
                "name='" + name + '\'' +
                ", top=" + Arrays.toString(top) +
                ", bottom=" + Arrays.toString(bottom) +
                ", left=" + Arrays.toString(left) +
                ", front=" + Arrays.toString(front) +
                ", right=" + Arrays.toString(right) +
                ", back=" + Arrays.toString(back) +
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

        for (int i : left) {
            res.append(i);
        }
        res.append(' ');

        for (int i : front) {
            res.append(i);
        }
        res.append(' ');

        for (int i : right) {
            res.append(i);
        }
        res.append(' ');

        for (int i : back) {
            res.append(i);
        }
        return res.toString();
    }

    //TODO: change this to work with vectors instead of mess like this

    private void applyR() {
        int temp = front[1];
        front[1] = bottom[1];
        bottom[1] = back[3];
        back[3] = top[1];
        top[1] = temp;

        temp = front[2];
        front[2] = bottom[2];
        bottom[2] = back[0];
        back[0] = top[2];
        top[2] = temp;

        temp = right[0];
        right[0] = right[3];
        right[3] = right[2];
        right[2] = right[1];
        right[1] = temp;

    }

    private void applyRprime() {
        applyR();
        applyR();
        applyR();
    }

    private void applyR2() {
        applyR();
        applyR();
    }

    private void applyU() {
        int temp = front[1];
        front[1] = right[1];
        right[1] = back[1];
        back[1] = left[1];
        left[1] = temp;

        temp = front[0];
        front[0] = right[0];
        right[0] = back[0];
        back[0] = left[0];
        left[0] = temp;

        temp = top[0];
        top[0] = top[3];
        top[3] = top[2];
        top[2] = top[1];
        top[1] = temp;
    }

    private void applyUprime() {
        applyU();
        applyU();
        applyU();
    }

    private void applyU2() {
        applyU();
        applyU();
    }

    private void applyF() {
        int temp = top[2];
        top[2] = left[1];
        left[1] = bottom[0];
        bottom[0] = right[3];
        right[3] = temp;

        temp = top[3];
        top[3] = left[2];
        left[2] = bottom[1];
        bottom[1] = right[0];
        right[0] = temp;


        temp = front[0];
        front[0] = front[3];
        front[3] = front[2];
        front[2] = front[1];
        front[1] = temp;
    }

    private void applyFprime() {
        applyF();
        applyF();
        applyF();
    }

    private void applyF2() {
        applyF();
        applyF();
    }

    private void applyX() {
        int temp = top[0];
        top[0] = front[0];
        front[0] = bottom[0];
        bottom[0] = back[2];
        back[2] = temp;

        temp = top[1];
        top[1] = front[1];
        front[1] = bottom[1];
        bottom[1] = back[3];
        back[3] = temp;

        temp = top[2];
        top[2] = front[2];
        front[2] = bottom[2];
        bottom[2] = back[0];
        back[0] = temp;

        temp = top[3];
        top[3] = front[3];
        front[3] = bottom[3];
        bottom[3] = back[1];
        back[1] = temp;

        temp = left[0];
        left[0] = left[1];
        left[1] = left[2];
        left[2] = left[3];
        left[3] = temp;

        temp = right[1];
        right[1] = right[0];
        right[0] = right[3];
        right[3] = right[2];
        right[2] = temp;
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
        for (int i = 0; i < 4; i++) {
            int temp = left[i];
            left[i] = front[i];
            front[i] = right[i];
            right[i] = back[i];
            back[i] = temp;
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
        top[0] = left[3];
        left[3] = bottom[2];
        bottom[2] = right[1];
        right[1] = temp;

        temp = top[1];
        top[1] = left[0];
        left[0] = bottom[3];
        bottom[3] = right[2];
        right[2] = temp;

        temp = top[2];
        top[2] = left[1];
        left[1] = bottom[0];
        bottom[0] = right[3];
        right[3] = temp;

        temp = top[3];
        top[3] = left[2];
        left[2] = bottom[1];
        bottom[1] = right[0];
        right[0] = temp;

        temp = back[0];
        back[0] = back[1];
        back[1] = back[2];
        back[2] = back[3];
        back[3] = temp;

        temp = front[1];
        front[1] = front[0];
        front[0] = front[3];
        front[3] = front[2];
        front[2] = temp;
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
