package cubes.skewb.scramblers;

import cubes.skewb.SkewbState;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SkewbScrambler {
    private static final Map<SkewbState, int[]> pruningTable = new HashMap<>();
    private static final Map<Integer, String> moveTable = new HashMap<>() {{
        put(0, "R");
        put(1, "R'");
        put(2, "L");
        put(3, "L'");
        put(4, "U");
        put(5, "U'");
        put(6, "B");
        put(7, "B'");
    }};
    private static final int skewbGodsNo = 11;
    private static final int pruningDepth = 6;

    public static void prune() {
        SkewbIterator it = new SkewbIterator();
        SkewbState s = new SkewbState("00000 11111 22222 33333 44444 55555");
        pruningTable.put(s, new int[]{});
        while (it.getSize() < (pruningDepth + 1)) {
            s = new SkewbState("00000 11111 22222 33333 44444 55555");
            it.next();
            s.applyMoves(it.toArr());
            if (!pruningTable.containsKey(s)) {
                pruningTable.put(s, it.toArr());
            }
        }
    }

    public static String stateToScrambler(SkewbState state, int successes) {
        //Put in random orientation ??
        boolean randomOrientation = true;
        if (randomOrientation) {

        }

        //Convert to white-green orientation
        state.recolorToScrambleOrientation();

        //If this is too slow, try to go move by move instead of start from a solved cube everytime
        SkewbIterator iterator = new SkewbIterator();
        int[] moves = new int[0];
        int foundSolutions = 0;

        if (state.isSolved()) {
            foundSolutions = successes;
        }

        while (foundSolutions < successes && iterator.getSize() <= (skewbGodsNo - pruningDepth )) {
            SkewbState copy = state.copy();
            iterator.next();
            moves = iterator.toArr();
            copy.applyMoves(moves);
            if (pruningTable.containsKey(copy) || copy.isSolved()) {
                foundSolutions+= 1;
                state = copy;
            }
        }
        //Note: state here is most likely a different state than the argument because it changed in the loop

        int[] prunedMoves = pruningTable.get(state);

        moves = reverse(moves);
        StringBuilder res = new StringBuilder();

        for (int i : prunedMoves) {
            res.append(moveTable.get(i)).append(" ");
        }
        for (int i : moves) {
            res.append(moveTable.get(i)).append(" ");
        }

        return res.toString().trim();
    }

    private static int[] reverse(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            int c = a[a.length-1-i];

            if (c % 2 == 0) {
                b[i] = c+1;
            } else {
                b[i] = c-1;
            }
        }
        return b;
    }
}
