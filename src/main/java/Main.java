import application.gui.MainAppWindow;

import cubes.skewb.SkewbState;
import cubes.skewb.scramblers.SkewbScrambler;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        SkewbScrambler.prune();
        SkewbState s = new SkewbState("53443 00000 53445 12334 11221 21552");
        System.out.println(SkewbScrambler.stateToScrambler(s, 1));
        MainAppWindow.main(new String[0]);
    }
}
