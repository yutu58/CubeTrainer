package cubes.skewb.bulk;

import cubes.Case;
import cubes.skewb.SkewbState;
import cubes.skewb.data.L2LSet;
import cubes.skewb.optimizers.EMraterV2;
import cubes.skewb.solvers.SkewbSolver;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static cubes.skewb.SkewbNotations.wcaNotation;

public class AllSetAlgGenerator implements Runnable {
    List<L2LSet> sets;
    Label statusbar;

    public AllSetAlgGenerator(List<L2LSet> sets, Label statusbar) {
        this.sets = sets;
        this.statusbar = statusbar;
    }

    @Override
    public void run() {

        FileWriter writer;

        try {
            writer = new FileWriter("algs/skewb/L2L/autoratedAlgs.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (L2LSet set : sets) {
            List<Case> cases = set.getCases();

            int i = 0;

            for (Case c : cases) {
                String pattern = c.getPattern();
                SkewbState state = new SkewbState(pattern);
                state.applyWCAMoves(wcaNotation.get("x"));

                SkewbSolver solver = new SkewbSolver(state, 14, new Label(), new ListView<>(), true, new EMraterV2());

                solver.run();


                for (int j = 0; j < 5; j++) {
                    String alg = solver.getFinalAlgs().get(j);
                    alg = alg.replaceFirst(" ", ",");
                    System.out.println(c.getId() + "," + alg);
                    try {
                        writer.write(c.getId() + ","+alg + "\n");
                    } catch (IOException e) {
                        System.out.println("hello");
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
