package cubes.skewb.algRater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grip {
    private Map<String, Integer> moveCost;
    private Map<String, String> destGrips;

    private static int defaultHardRegrip = 15;
    private static int defaultEasyRegrip = 9;

    public Grip() {
        moveCost = new HashMap<>();
        destGrips = new HashMap<>();
    }

    private void addMoveCost(String move, int cost) {
        this.moveCost.put(move, cost);
    }

    private void addMove(String move, int cost, String destGrip) {
        this.moveCost.put(move, cost);
        this.destGrips.put(move, destGrip);
    }

    public int getCost(String move) {
        return moveCost.get(move);
    }

    public String getDest(String move) {
        return destGrips.get(move);
    }


    /**
     *
     * @return a map with name of each grip and the corresponding rip
     */



    public static Map<String, Grip> allGrips() {
        Map<String, Grip> resMap = new HashMap<>();
        

        Grip rby = new Grip();
        rby.addMove("r", 13, "byr");            //With regrip
        rby.addMove("r'", 10, "yrb");
        rby.addMove("R", 14, "wry");
        rby.addMove("R'", 15, "bwy");
        rby.addMove("B", 11, "rby");
        rby.addMove("B'", 11, "rby");
        rby.addMove("b", 14, "roy");            //With regrip
        rby.addMove("b'", 12, "rby ");
        resMap.put("rby", rby);
//        rby.addMove("z", -1, null);
//        rby.addMove("z'", -1, null);
//        rby.addMove("z2", -1, null);


        resMap.put("yrb", addGripHelper(resMap, new int[]{10, -1, 10, 14, -1, 15, -1, -1},
                new String[]{"rby", "rby", "ywr", "ybo", "rby", "yrb", "rby", "rby"},
                new boolean[]{false, false, false, false, false, false, false, false}));


        resMap.put("ywr", addGripHelper(resMap, new int[]{10, -1, -1, 10, -1, 17, -1, -1},
                new String[]{"rwb", "rby", "yrb", "yrb", "rby", "ybr", "rby", "rby"},
                new boolean[]{false, false, false, false, true, false, true, true}));

        resMap.put("rwb", addGripHelper(resMap, new int[]{-1, 14, 12, 11, -1, 15, -1, 15},
                new String[]{"yrb", "ywr", "ybr", "brw", "rby", "rby", "rby", "rby"},
                new boolean[]{false, false, false, false, true, false, true, false}));

        resMap.put("brw", addGripHelper(resMap, new int[]{12, -1, 11, 18, -1, -1, -1, -1},
                new String[]{"ybo", "rby", "rwb", "wbr", "rby", "rby", "rby", "rby"},
                new boolean[]{false, true, false, false, true, true, true, true}));

        return resMap;
    }

    private static Grip addGripHelper(Map<String, Grip> resMap, int[] costs, String[] dests, boolean[] hardRegrip) {

        Grip x = new Grip();
        String[] moves = new String[]{"r", "r'", "R", "R'", "B", "B'", "b", "b'"};

        for (int i = 0; i < moves.length; i++) {
            int cost = costs[i];
            if (cost != -1) {
                x.addMove(moves[i], cost, dests[i]);
            } else {
                Grip g = resMap.get(dests[i]);
                if (g == null) {
                    System.out.println("Regrip grip not found");
                    return null;
                }
                if (hardRegrip[i]) {
                    x.addMove(moves[i], defaultHardRegrip + g.getCost(moves[i]), g.getDest(moves[i]));
                } else {
                    x.addMove(moves[i], defaultEasyRegrip + g.getCost(moves[i]), g.getDest(moves[i]));
                }
            }
        }
        return x;
    }
}
