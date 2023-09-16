package cubes.tx2.solvers;

import cubes.tx2.Tx2State;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Tx2Scrambler extends Tx2Mover {
    public static String stateToScrambler(Tx2State state, int successes, boolean randomAUF, boolean randomPreAUF) {
        Set<int[]> foundScramblesSet = new HashSet<>();
        //Put in random orientation ??
        boolean randomOrientation = false;
        if (randomOrientation) {

        }
        //Convert to white-green orientation
        state.recolorToScrambleOrientation();

        if (randomPreAUF) {
            int randomInt = ThreadLocalRandom.current().nextInt(0, 4);
            for (int i = 0; i < randomInt; i++) {
                state.applyWCAMoves(new int[]{3});
            }
        }

        if (randomAUF) {
            int randomInt = ThreadLocalRandom.current().nextInt(0, 4);
            //Already recolored to scramble orientation
            //All pieces with yellow on it must be ignored
            int[] ULB = new int[]{state.top[0], state.left[0], state.back[1]};
            int[] UBR = new int[]{state.top[1], state.back[0], state.right[1]};
            int[] URF = new int[]{state.top[2], state.right[0], state.front[1]};
            int[] UFL = new int[]{state.top[3], state.front[0], state.left[1]};

            int[] DLF = new int[]{state.bottom[0], state.left[2], state.front[3]};
            int[] DFR = new int[]{state.bottom[1], state.front[2], state.right[3]};
            int[] DRB = new int[]{state.bottom[2], state.right[2], state.back[3]};
            int[] DBL = new int[]{state.bottom[3], state.back[2], state.left[3]};

            if (Arrays.stream(ULB).anyMatch(i -> i == 0)) {
                if (ULB[0] != 0) {
                    state.top[0] = (state.top[0] + (randomInt)) % 4 + 2;
                }
                if (ULB[1] != 0) {
                    state.left[0] = (state.left[0] + (randomInt)) % 4 + 2;
                }
                if (ULB[2] != 0) {
                    state.back[1] = (state.back[1] + (randomInt)) % 4 + 2;
                }
            }

            if (Arrays.stream(UBR).anyMatch(i -> i == 0)) {
                if (UBR[0] != 0) {
                    state.top[1] = (state.top[1] + (randomInt)) % 4 + 2;
                }
                if (UBR[1] != 0) {
                    state.back[0] = (state.back[0] + (randomInt)) % 4 + 2;
                }
                if (UBR[2] != 0) {
                    state.right[1] = (state.right[1] + (randomInt)) % 4 + 2;
                }
            }

            if (Arrays.stream(URF).anyMatch(i -> i == 0)) {
                if (URF[0] != 0) {
                    state.top[2] = (state.top[2] + (randomInt)) % 4 + 2;
                }
                if (URF[1] != 0) {
                    state.right[0] = (state.right[0] + (randomInt)) % 4 + 2;
                }
                if (URF[2] != 0) {
                    state.front[1] = (state.front[1] + (randomInt)) % 4 + 2;
                }
            }

            if (Arrays.stream(UFL).anyMatch(i -> i == 0)) {
                if (UFL[0] != 0) {
                    state.top[3] = (state.top[3] + (randomInt)) % 4 + 2;
                }
                if (UFL[1] != 0) {
                    state.front[0] = (state.front[0] + (randomInt)) % 4 + 2;
                }
                if (UFL[2] != 0) {
                    state.left[1] = (state.left[1] + (randomInt)) % 4 + 2;
                }
            }

            if (Arrays.stream(DLF).anyMatch(i -> i == 0)) {
                if (DLF[0] != 0) {
                    state.bottom[0] = (state.bottom[0] + (randomInt)) % 4 + 2;
                }
                if (DLF[1] != 0) {
                    state.left[2] = (state.left[2] + (randomInt)) % 4 + 2;
                }
                if (DLF[2] != 0) {
                    state.front[3] = (state.front[3] + (randomInt)) % 4 + 2;
                }
            }

            if (Arrays.stream(DFR).anyMatch(i -> i == 0)) {
                if (DFR[0] != 0) {
                    state.bottom[1] = (state.bottom[1] + (randomInt)) % 4 + 2;
                }
                if (DFR[1] != 0) {
                    state.front[2] = (state.front[2] + (randomInt)) % 4 + 2;
                }
                if (DFR[2] != 0) {
                    state.right[3] = (state.right[3] + (randomInt)) % 4 + 2;
                }
            }

            if (Arrays.stream(DRB).anyMatch(i -> i == 0)) {
                if (DRB[0] != 0) {
                    state.bottom[2] = (state.bottom[2] + (randomInt)) % 4 + 2;
                }
                if (DRB[1] != 0) {
                    state.right[2] = (state.right[2] + (randomInt)) % 4 + 2;
                }
                if (DRB[2] != 0) {
                    state.back[3] = (state.back[3] + (randomInt)) % 4 + 2;
                }
            }

            if (Arrays.stream(DBL).anyMatch(i -> i == 0)) {
                if (DBL[0] != 0) {
                    state.bottom[3] = (state.bottom[3] + (randomInt)) % 4 + 2;
                }
                if (DBL[1] != 0) {
                    state.back[2] = (state.back[2] + (randomInt)) % 4 + 2;
                }
                if (DBL[2] != 0) {
                    state.left[3] = (state.left[3] + (randomInt)) % 4 + 2;
                }
            }
        }

        //If this is too slow, try to go move by move instead of start from a solved cube everytime
        Tx2Iterator iterator = new Tx2Iterator();
        int foundSolutions = 0;
        int[] scrambleArr = new int[0];

        if (state.isSolved()) {
            foundSolutions = successes;
        }


        if (pruningTable.containsKey(state)) {
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
