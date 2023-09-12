package cubes.tx2;

import java.util.HashMap;
import java.util.Map;

public class Tx2Notations {
    public enum notationEnum {
        WCA_RUF_NOTATION,
        LITHIUM_2x2_CODE
    }

    public static Map<String, int[]> wcaRUFNotation = new HashMap<>() {{
        put("R", new int[]{0});
        put("R'", new int[]{1});
        put("R2", new int[]{2});
        put("U", new int[]{3});
        put("U'", new int[]{4});
        put("U2", new int[]{5});
        put("F", new int[]{6});
        put("F'", new int[]{7});
        put("F2", new int[]{8});
        put("x", new int[]{9});
        put("x'", new int[]{10});
        put("x2", new int[]{11});
        put("y", new int[]{12});
        put("y'", new int[]{13});
        put("y2", new int[]{14});
        put("z", new int[]{15});
        put("z'", new int[]{16});
        put("z2", new int[]{17});
    }};
}