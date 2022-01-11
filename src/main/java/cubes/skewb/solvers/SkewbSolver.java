package cubes.skewb.solvers;

import cubes.skewb.SkewbState;
import cubes.skewb.algRater.Grip;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.*;

public class SkewbSolver extends SkewbMover implements Runnable {
    private boolean cancelled;

    private SkewbState state;
    private int maxDepth;
    private int found;
    private Set<String> foundSolutions;
    private Set<String> rightFoundSolutions;
    private SkewbIterator skewbIterator;
    private Label statusBar;

    private boolean convertRB;

    public SkewbSolver(SkewbState state, int maxDepth, Label statusBar, boolean convertRB) { //Also textfield / scrollpane to add the algs to
        this.cancelled = false;
        this.found = 0;
        this.state = state;
        this.maxDepth = maxDepth;
        this.skewbIterator = new SkewbIterator();
        this.foundSolutions = new HashSet<>();
        this.rightFoundSolutions = new HashSet<>();
        this.statusBar = statusBar;
        this.convertRB = convertRB;
    }

    private void solveToWCA() {
        updateStatus("Solving to WCA notation");

        state.recolorToScrambleOrientation();

        if (state.isSolved()) {
            createFoundSolution(new int[0], new int[0]);
        }

        if (pruningTable.containsKey(state)) {
            createFoundSolution(new int[0], pruningTable.get(state));
        }

        while (skewbIterator.getSize() <= maxDepth - pruningDepth && !cancelled) {
            SkewbState copy = state.copy();
            skewbIterator.next();
            int[] moves = skewbIterator.toArr();
            copy.applyWCAMoves(moves);
            if (pruningTable.containsKey(copy)) {
                //Make sure the pruned move and the applied move don't cancel out
                createFoundSolution(moves, pruningTable.get(copy));
            }
        }
    }

    private void createFoundSolution(int[] appliedMoves, int[] prunedMoves) {
        prunedMoves = reverseSkewb(prunedMoves);

        if (appliedMoves.length != 0 && prunedMoves.length != 0) {
            if (prunedMoves[0] / 2 == appliedMoves[appliedMoves.length - 1] / 2) {
                return;
            }
        }

        int[] solutionArr = new int[appliedMoves.length + prunedMoves.length];
        System.arraycopy(appliedMoves, 0, solutionArr, 0, appliedMoves.length);
        System.arraycopy(prunedMoves, 0, solutionArr, appliedMoves.length, prunedMoves.length);

        StringBuilder res = new StringBuilder();

        for (int i : solutionArr) {
            res.append(moveTable.get(i)).append(" ");
        }
        String solution = res.toString().trim();
        if (!foundSolutions.contains(solution)) {
            found++;
            foundSolutions.add(solution);
        }
    }



    @Override
    public void run() {
        try {
            solveToWCA();
            if (convertRB) {
                convertToRightMoves();
            }

            for (String s : rightFoundSolutions) {
                System.out.println(s);
            }

//            if (true) {
//                Map<String, Integer> ratedAlgs = rateAndSortAlgs();
//                for (Map.Entry<String, Integer> e : ratedAlgs.entrySet()) {
//                    System.out.println(e.getKey() + "::-> " + e.getValue());
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateStatus("Done, " + foundSolutions.size() + " solutions found!");

        if (cancelled) {
            updateStatus("Cancelled successfully");
        }
    }

    private Map<String, Integer> rateAndSortAlgs() {
        Map<String, Integer> ratedAlgs = new HashMap<>();
        for (String s : rightFoundSolutions) {
            Map.Entry<String, Integer> entry = rateAlg(s);
            if (entry != null) {
                ratedAlgs.put(entry.getKey(), entry.getValue());
            }
        }
        return ratedAlgs;
    }



    private void convertToRightMoves(){
        updateStatus("Converting to only fast moves");
        char[] movesOnIndex = new char[]{'B', 'R', 'b', 'r', 'B', 'r', 'b', 'R'};
        Map<Character, Integer> moveToCornerMap = new HashMap<>(){{
            put('L', 7);
            put('R', 5);
            put('U', 0);
            put('B', 6);
        }};

        for (String wcaSolution : foundSolutions) {
            if (cancelled) {
                break;
            }
            int[] cornerStatesWCA = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
            int[] cornerStatesRight = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
            String[] moves = wcaSolution.split(" ");
            StringBuilder res = new StringBuilder();
            for (String m : moves) {
                char side = m.charAt(0);

                StringBuilder fin = new StringBuilder();

                fin.append(movesOnIndex[find(cornerStatesRight, cornerStatesWCA[moveToCornerMap.get(side)])]);
                if (m.endsWith("'")) {
                    fin.append("'");
                }
                String rightMove = fin.toString();

                wcaMovesArrayHelper(cornerStatesWCA, m);
                rightMovesArrayHelper(cornerStatesRight, rightMove);


                res.append(rightMove).append(" ");
            }
//            System.out.println(wcaSolution + " ::-> " + res.toString());
            rightFoundSolutions.add(res.toString().trim());
        }
    }

    private void updateStatus(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statusBar.setText(text);
            }
        });
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private void wcaMovesArrayHelper(int[] allCorners, String move) {
        int temp;
        switch (move) {
            case "L" -> {
                temp = allCorners[4];
                allCorners[4] = allCorners[3];
                allCorners[3] = allCorners[6];
                allCorners[6] = temp;
            }
            case "L'" -> {
                temp = allCorners[6];
                allCorners[6] = allCorners[3];
                allCorners[3] = allCorners[4];
                allCorners[4] = temp;
            }
            case "R" -> {
                temp = allCorners[4];
                allCorners[4] = allCorners[6];
                allCorners[6] = allCorners[1];
                allCorners[1] = temp;
            }
            case "R'" -> {
                temp = allCorners[1];
                allCorners[1] = allCorners[6];
                allCorners[6] = allCorners[4];
                allCorners[4] = temp;
            }
            case "U" -> {
                temp = allCorners[6];
                allCorners[6] = allCorners[3];
                allCorners[3] = allCorners[1];
                allCorners[1] = temp;
            }
            case "U'" -> {
                temp = allCorners[1];
                allCorners[1] = allCorners[3];
                allCorners[3] = allCorners[6];
                allCorners[6] = temp;
            }
            case "B" -> {
                temp = allCorners[0];
                allCorners[0] = allCorners[5];
                allCorners[5] = allCorners[7];
                allCorners[7] = temp;
            }
            case "B'" -> {
                temp = allCorners[7];
                allCorners[7] = allCorners[5];
                allCorners[5] = allCorners[0];
                allCorners[0] = temp;
            }
        }
    }

    private void rightMovesArrayHelper(int[] allCorners, String move) {
        int temp;
        switch (move) {
            case "R" -> {
                temp = allCorners[0];
                allCorners[0] = allCorners[2];
                allCorners[2] = allCorners[5];
                allCorners[5] = temp;
            }
            case "R'" -> {
                temp = allCorners[5];
                allCorners[5] = allCorners[2];
                allCorners[2] = allCorners[0];
                allCorners[0] = temp;
            }
            case "r" -> {
                temp = allCorners[4];
                allCorners[4] = allCorners[6];
                allCorners[6] = allCorners[1];
                allCorners[1] = temp;
            }
            case "r'" -> {
                temp = allCorners[1];
                allCorners[1] = allCorners[6];
                allCorners[6] = allCorners[4];
                allCorners[4] = temp;
            }
            case "B" -> {
                temp = allCorners[6];
                allCorners[6] = allCorners[3];
                allCorners[3] = allCorners[1];
                allCorners[1] = temp;
            }
            case "B'" -> {
                temp = allCorners[1];
                allCorners[1] = allCorners[3];
                allCorners[3] = allCorners[6];
                allCorners[6] = temp;
            }
            case "b" -> {
                temp = allCorners[0];
                allCorners[0] = allCorners[5];
                allCorners[5] = allCorners[7];
                allCorners[7] = temp;
            }
            case "b'" -> {
                temp = allCorners[7];
                allCorners[7] = allCorners[5];
                allCorners[5] = allCorners[0];
                allCorners[0] = temp;
            }
        }
    }

    private int find(int[] arr, int x) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == x) {
                return i;
            }
        }
        return -1;
    }

    private Map.Entry<String, Integer> rateAlg(String alg) {
        int rating = 0;                             //Lower rating is better;
        String[] moves = alg.split(" ");

        Map<String, Grip> gripMap = Grip.allGrips();

        String currentGrip = "rby";

        for (String m : moves) {
            try {
                rating += gripMap.get(currentGrip).getCost(m);
                currentGrip = gripMap.get(currentGrip).getDest(m);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        int finalRating = rating;
        return new Map.Entry<String, Integer>() {
            @Override
            public String getKey() {
                return alg;
            }

            @Override
            public Integer getValue() {
                return finalRating;
            }

            @Override
            public Integer setValue(Integer value) {
                return value;
            }
        };
    }
}
