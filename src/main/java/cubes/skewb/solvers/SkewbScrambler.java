package cubes.skewb.solvers;

import cubes.skewb.SkewbState;

import java.util.*;

public class SkewbScrambler extends SkewbMover{
    public static String stateToScrambler(SkewbState state, int successes) {
        Set<int[]> foundScramblesSet = new HashSet<>();
        //Put in random orientation ??
        boolean randomOrientation = false;
        if (randomOrientation) {

        }
        //Convert to white-green orientation
        state.recolorToScrambleOrientation();

        //If this is too slow, try to go move by move instead of start from a solved cube everytime
        SkewbIterator iterator = new SkewbIterator();
        int foundSolutions = 0;
        int[] scrambleArr = new int[0];

        if (state.isSolved()) {
            foundSolutions = successes;
        }
        if (pruningTable.containsKey(state)) {
            foundSolutions += 1;
            foundScramblesSet.add(pruningTable.get(state));
        }

        while (foundSolutions < successes && iterator.getSize() <= (skewbGodsNo - pruningDepth )) {
            SkewbState copy = state.copy();
            iterator.next();
            int[] moves = iterator.toArr();
            copy.applyWCAMoves(moves);
            if (pruningTable.containsKey(copy)) {
                //Make sure the pruned move and the applied move don't cancel out
                int[] foundMoves = reverseSkewb(moves);
                int[] prunedMoves = pruningTable.get(copy);

                if (prunedMoves[prunedMoves.length-1] / 2 != foundMoves[0] / 2) {
                    scrambleArr = new int[moves.length + prunedMoves.length];
                    System.arraycopy(prunedMoves, 0, scrambleArr, 0, prunedMoves.length);
                    System.arraycopy(foundMoves, 0, scrambleArr, prunedMoves.length, foundMoves.length);
                    if (!foundScramblesSet.contains(scrambleArr)) {
                        foundSolutions += 1;
                        foundScramblesSet.add(scrambleArr);
                    }

                }
            }
        }
        StringBuilder res = new StringBuilder();

        for (int i : scrambleArr) {
            res.append(moveTable.get(i)).append(" ");
        }

        return res.toString().trim();
    }
}
