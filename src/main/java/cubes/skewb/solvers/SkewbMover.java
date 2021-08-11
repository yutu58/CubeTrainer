package cubes.skewb.solvers;

import cubes.skewb.SkewbState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class SkewbMover {
    protected static final Map<SkewbState, int[]> pruningTable = new HashMap<>();
    protected static final Map<Integer, String> moveTable = new HashMap<>() {{
        put(0, "R");
        put(1, "R'");
        put(2, "L");
        put(3, "L'");
        put(4, "U");
        put(5, "U'");
        put(6, "B");
        put(7, "B'");
    }};
    protected static final int skewbGodsNo = 11;
    protected static final int pruningDepth = 6;

    public static void prune() {
        SkewbIterator it = new SkewbIterator();
        SkewbState s = new SkewbState("00000 11111 22222 33333 44444 55555");
        pruningTable.put(s, new int[]{});
        while (it.getSize() < (pruningDepth + 1)) {
            s = new SkewbState("00000 11111 22222 33333 44444 55555");
            it.next();
            s.applyWCAMoves(it.toArr());
            if (!pruningTable.containsKey(s)) {
                pruningTable.put(s, it.toArr());
            }
        }
        System.out.println(pruningTable.size());
    }

    public static int[] reverseSkewb(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            int c = a[a.length-1-i];

            if (c < 8) {                            //Regular moves ||
                if (c % 2 == 0) {
                    b[i] = c + 1;
                } else {
                    b[i] = c - 1;
                }
            } else if (c < 17) {                    //Rotations
                if (c % 3 == 1) {
                    b[i] = c;
                } else if (c % 3 == 0) {
                    b[i] = c-1;
                } else {
                    b[i] = c+1;
                }
            }
        }
        return b;
    }
}
