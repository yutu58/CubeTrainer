package cubes.skewb.solvers;

import cubes.skewb.SkewbNotations;
import cubes.skewb.SkewbState;
import cubes.skewb.optimizers.AlgRater;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.*;

public class SkewbSolver extends SkewbMover implements Runnable {
    private SkewbState state;
    private int maxDepth;
    private int found;
    private Set<String> foundSolutions;
    private Set<String> rightFoundSolutions;
    private SkewbIterator skewbIterator;
    private Label statusBar;
    private ListView<String> algList;

    private List<String> finalAlgs;
    private boolean allAngles;

    private AlgRater rater;

    public SkewbSolver(SkewbState state, int maxDepth, Label statusBar, ListView<String> algList, boolean allAngles, AlgRater rater) { //Also textfield / scrollpane to add the algs to
        this.found = 0;
        this.state = state;
        this.maxDepth = maxDepth;
        this.skewbIterator = new SkewbIterator();
        this.foundSolutions = new HashSet<>();
        this.rightFoundSolutions = new HashSet<>();
        this.statusBar = statusBar;
        this.algList = algList;
        this.allAngles = allAngles;
        this.rater = rater;
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
            optimize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateAlgs();
        updateStatus("Done, " + foundSolutions.size() + " solutions found!");
    }

    private void convertToRightMoves(){
        updateStatus("Converting to only fast moves");

        for (String wcaSolution : foundSolutions) {
            SkewbState state = new SkewbState("00000 11111 22222 33333 44444 55555");
            String[] rMoves = {"R", "r", "B", "b"};
            int[] movePos = new int[]{0, 1, 2, 3};

            Map<String, Integer> map = new HashMap<>(){{
                put("L", 0);
                put("R", 1);
                put("U", 2);
                put("B", 3);
            }};

            String[] moves = wcaSolution.split(" ");
            StringBuilder res = new StringBuilder();
            for (String m : moves) {
                switch (m) {
                    case "L" -> res.append(rMoves[movePos[0]]);
                    case "L'" -> res.append(rMoves[movePos[0]]).append("'");
                    case "R" -> res.append(rMoves[movePos[1]]);
                    case "R'" -> res.append(rMoves[movePos[1]]).append("'");
                    case "U" -> res.append(rMoves[movePos[2]]);
                    case "U'" -> res.append(rMoves[movePos[2]]).append("'");
                    case "B" -> res.append(rMoves[movePos[3]]);
                    case "B'" -> res.append(rMoves[movePos[3]]).append("'");
                }
                res.append(" ");

                String oldState = state.toPattern();
                state.applyWCAMoves(SkewbNotations.wcaNotation.get(m));
                String newState = state.toPattern();

                if (fixedCenterMoved(oldState, newState)) {
                    List<Integer> asList = new ArrayList<>(){{
                        add(0);
                        add(1);
                        add(2);
                        add(3);
                    }};
                    int fixedIndex = map.get(String.valueOf(m.charAt(0)));
                    asList.remove(Integer.valueOf(fixedIndex));

                    int buff = movePos[asList.get(0)];
                    movePos[asList.get(0)] = movePos[asList.get(1)];
                    movePos[asList.get(1)] = movePos[asList.get(2)];
                    movePos[asList.get(2)] = buff;

                    if (m.equals("R'") || m.equals("L") || m.equals("B") || m.equals("U")) {
                        int buff2 = movePos[asList.get(0)];
                        movePos[asList.get(0)] = movePos[asList.get(1)];
                        movePos[asList.get(1)] = movePos[asList.get(2)];
                        movePos[asList.get(2)] = buff2;
                    }
                }
            }
            rightFoundSolutions.add(res.toString().trim());
        }

        //Add all orientations if needed
        Set<String> solutions = new HashSet<>();
        if (allAngles) {
            for (String s : rightFoundSolutions) {
                solutions.addAll(allZAngles(s));
            }
        } else {
            solutions = rightFoundSolutions;
        }
        rightFoundSolutions = solutions;
    }

    private boolean fixedCenterMoved(String oldState, String newState) {
        int j = -1;
        for (int i = 4; i <= 34; i+= 6) {
            if (oldState.charAt(i) == '2') {
                j = i;
                i = 100;
            }
        }
        return (newState.charAt(j) != '2');
    }

    private void updateStatus(String text) {
        Platform.runLater(() -> {
            statusBar.setStyle("-fx-text-fill: Black");
            statusBar.setText(text);
        });
    }

    private void updateAlgs() {
        Platform.runLater(() -> {
            ObservableList<String> items = FXCollections.observableList(finalAlgs);
            algList.setItems(items);
        });
    }

    private Set<String> allZAngles(String s) {
        Set<String> res = new HashSet<>();
        res.add(s);

        for (int i = 0; i < 3; i++) {
            s = s.replace('r', 'q');
            s = s.replace('R', 'r');
            s = s.replace('B', 'R');
            s = s.replace('b', 'B');
            s = s.replace('q', 'b');
            res.add(s);
        }
        return res;
    }

    private void optimize() {
        finalAlgs = removeDuplicates(rater.rate(this.rightFoundSolutions));
    }

    public static String convertSetToString(Set<String> stringSet) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String element : stringSet) {
            stringBuilder.append(element);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public static List<String> removeDuplicates(List<String> list) {
        LinkedHashSet<String> set = new LinkedHashSet<>(list);
        return new ArrayList<>(set);
    }
}
