package cubes.tx2.solvers;

import cubes.tx2.Tx2State;

import java.util.HashMap;
import java.util.Map;

public abstract class Tx2Mover {
    protected static final Map<Tx2State, int[]> pruningTable = new HashMap<>();
    protected static final Map<Integer, String> moveTable = new HashMap<>() {{
        put(0, "R");
        put(1, "R'");
        put(2, "R2");
        put(3, "U");
        put(4, "U'");
        put(5, "U2");
        put(6, "F");
        put(7, "F'");
        put(8, "F2");
    }};
    protected static final int tx2GodsNo = 11;
    protected static final int pruningDepth = 6;

    public static void prune() {
        Tx2Iterator it = new Tx2Iterator();
        Tx2State s = new Tx2State("0000 1111 2222 3333 4444 5555");
        pruningTable.put(s, new int[]{});
        while (it.getSize() < (pruningDepth + 1)) {
            s = new Tx2State("0000 1111 2222 3333 4444 5555");
            it.next();
            s.applyWCAMoves(it.toArr());
            if (!pruningTable.containsKey(s)) {
                pruningTable.put(s, it.toArr());
            }
        }
        System.out.println(pruningTable.size());
    }

    public static int[] reverse2x2(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            int c = a[a.length-1-i];
            if (c % 3 == 2) {
                b[i] = c;
            } else if (c % 3 == 0) {
                b[i] = c+1;
            } else {
                b[i] = c-1;
            }
        }
        return b;
    }
}
