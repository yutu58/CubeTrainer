package cubes.skewb.solvers;

import cubes.skewb.SkewbState;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.HashSet;
import java.util.Set;

public class SkewbSolver extends SkewbMover implements Runnable {
    private SkewbState state;
    private int maxDepth;
    private int found;
    private Set<String> foundSolutions;
    private Set<String> rightFoundSolutions;
    private SkewbIterator skewbIterator;
    private Label statusBar;

    public SkewbSolver(SkewbState state, int maxDepth, Label statusBar) { //Also textfield / scrollpane to add the algs to
        this.found = 0;
        this.state = state;
        this.maxDepth = maxDepth;
        this.skewbIterator = new SkewbIterator();
        this.foundSolutions = new HashSet<>();
        this.rightFoundSolutions = new HashSet<>();
        this.statusBar = statusBar;
    }

    private void solveToWCA() {
        updateStatus("Solving to WCA notation");

        state.recolorToScrambleOrientation();

        if (state.isSolved()) {
            foundSolution(new int[0], new int[0]);
        }

        if (pruningTable.containsKey(state)) {
            foundSolution(new int[0], pruningTable.get(state));
        }

        while (skewbIterator.getSize() <= maxDepth - pruningDepth) {
            SkewbState copy = state.copy();
            skewbIterator.next();
            int[] moves = skewbIterator.toArr();
            copy.applyWCAMoves(moves);
            if (pruningTable.containsKey(copy)) {
                //Make sure the pruned move and the applied move don't cancel out
                foundSolution(moves, pruningTable.get(copy));
            }
        }
    }

    private void foundSolution(int[] appliedMoves, int[] prunedMoves) {
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
            convertToRightMoves();
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateStatus("Done, " + foundSolutions.size() + " solutions found!");
    }

    private void convertToRightMoves(){
        updateStatus("Converting to only fast moves");
        int[] fixedCorners = new int[]{0, 1, 2, 3};
        for (String wcaSolution : foundSolutions) {
            int moveOffset = 0;
            String[] moves = wcaSolution.split(" ");
            StringBuilder res = new StringBuilder();
            for (String m : moves) {
                switch (m) {

                }
                res.append(" ");
            }
            System.out.println(wcaSolution + " ::-> " + res.toString());
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
}
