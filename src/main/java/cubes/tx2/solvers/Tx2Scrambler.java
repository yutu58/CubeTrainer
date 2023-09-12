package cubes.tx2.solvers;

import cubes.tx2.Tx2State;

import java.util.HashSet;
import java.util.Set;

public class Tx2Scrambler extends Tx2Mover {
    public static String stateToScrambler(Tx2State state, int successes) {
        Set<int[]> foundScramblesSet = new HashSet<>();
        //Put in random orientation ??
        boolean randomOrientation = false;
        if (randomOrientation) {

        }
        //Convert to white-green orientation
        state.recolorToScrambleOrientation();

        //If this is too slow, try to go move by move instead of start from a solved cube everytime
        Tx2Iterator iterator = new Tx2Iterator();
        int foundSolutions = 0;
        int[] scrambleArr = new int[0];

        if (state.isSolved()) {
            foundSolutions = successes;
        }


        if (pruningTable.containsKey(state)) {
            System.out.println("hello");
            foundSolutions += 1;
            foundScramblesSet.add(pruningTable.get(state));
        }

        while (foundSolutions < successes && iterator.getSize() <= (tx2GodsNo - pruningDepth )) {
            Tx2State copy = state.copy();
            iterator.next();
            int[] moves = iterator.toArr();
            copy.applyWCAMoves(moves);
            if (pruningTable.containsKey(copy)) {
                //Make sure the pruned move and the applied move don't cancel out
                int[] foundMoves = reverse2x2(moves);
                int[] prunedMoves = pruningTable.get(copy);

//                System.out.println(Arrays.toString(foundMoves));
//                System.out.println(Arrays.toString(prunedMoves));
//                System.out.println("=====");

                if (prunedMoves[prunedMoves.length-1] / 3 != foundMoves[0] / 3) {
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
