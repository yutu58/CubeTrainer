package cubes.tx2.solvers;

import cubes.tx2.Tx2State;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.*;

public class Tx2Solver extends Tx2Mover implements Runnable {
    private Tx2State state;
    private int maxDepth;
    private int found;
    private Set<String> foundSolutions;
    private Set<String> rightFoundSolutions;
    private Tx2Iterator tx2Iterator;
    private Label statusBar;
    private ListView<String> algList;
    private boolean allAngles;

    public Tx2Solver(Tx2State state, int maxDepth, Label statusBar, ListView<String> algList, boolean allAngles) { //Also textfield / scrollpane to add the algs to
        this.found = 0;
        this.state = state;
        this.maxDepth = maxDepth;
        this.tx2Iterator = new Tx2Iterator();
        this.foundSolutions = new HashSet<>();
        this.rightFoundSolutions = new HashSet<>();
        this.statusBar = statusBar;
        this.algList = algList;
        this.allAngles = allAngles;
    }

    public Set<String> getRightFoundSolutions() {
        return rightFoundSolutions;
    }

    public void setRightFoundSolutions(Set<String> rightFoundSolutions) {
        this.rightFoundSolutions = rightFoundSolutions;
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

        while (tx2Iterator.getSize() <= maxDepth - pruningDepth) {
            Tx2State copy = state.copy();
            tx2Iterator.next();
            int[] moves = tx2Iterator.toArr();
            copy.applyWCAMoves(moves);
            if (pruningTable.containsKey(copy)) {
                //Make sure the pruned move and the applied move don't cancel out
                foundSolution(moves, pruningTable.get(copy));
            }
        }
    }

    private void foundSolution(int[] appliedMoves, int[] prunedMoves) {
        prunedMoves = reverse2x2(prunedMoves);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateAlgs();
        updateStatus("Done, " + foundSolutions.size() + " solutions found!");
    }

    private void updateStatus(String text) {
        Platform.runLater(() -> {
            statusBar.setStyle("-fx-text-fill: Black");
            statusBar.setText(text);
        });
    }

    private void updateAlgs() {
        Set<String> finalSolutions = foundSolutions;
        Platform.runLater(() -> {
            List<String> list = new ArrayList<>(finalSolutions);
            list.sort((o1, o2) -> {
                int x = 0;
                //First condition: Length
                if (x == 0) {
                    x = Integer.compare(o1.length(), o2.length());
                }

                //2nd condition: alg moves;
                if (x == 0) {
                    x = CharSequence.compare(o1, o2);
                }

                return x;
            });
            ObservableList<String> items = FXCollections.observableList(list);
            algList.setItems(items);
        });
    }

}
