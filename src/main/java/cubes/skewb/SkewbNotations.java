package cubes.skewb;

import java.util.HashMap;
import java.util.Map;

public class SkewbNotations {
    public enum notationEnum {
        RubikSkewbNotation,
        WCASkewbNotation,
        LithiumSkewbCode
    }

    public static Map<String, int[]> wcaNotation = new HashMap<>() {{
       put("R", new int[]{0});
       put("R'", new int[]{1});
       put("L", new int[]{2});
       put("L'", new int[]{3});
       put("U", new int[]{4});
       put("U'", new int[]{5});
       put("B", new int[]{6});
       put("B'", new int[]{7});
       put("x", new int[]{8});
       put("x'", new int[]{9});
       put("x2", new int[]{10});
       put("y", new int[]{11});
       put("y'", new int[]{12});
       put("y2", new int[]{13});
       put("z", new int[]{14});
       put("z'", new int[]{15});
       put("z2", new int[]{16});
       //Rotations
    }};

    public static Map<String, int[]> rubikSkewbNotation = new HashMap<>() {{
        put("r", new int[]{0});
        put("r'", new int[]{1});
        put("R", new int[]{2, 11, 8});
        put("R'", new int[]{3, 9, 12});
        put("l", new int[]{2});
        put("l'", new int[]{3});
        put("L", new int[]{0, 11, 9});
        put("L'", new int[]{1, 8, 12});
        put("f", new int[]{4, 14, 12});
        put("f'", new int[]{5, 15, 11});
        put("F", new int[]{6, 8, 11});
        put("F'", new int[]{7, 12, 9});
        put("B", new int[]{4});
        put("B'", new int[]{5});
        put("b", new int[]{6});
        put("b'", new int[]{7});
        put("x", new int[]{8});
        put("x'", new int[]{9});
        put("x2", new int[]{10});
        put("y", new int[]{11});
        put("y'", new int[]{12});
        put("y2", new int[]{13});
        put("z", new int[]{14});
        put("z'", new int[]{15});
        put("z2", new int[]{16});
    }};
}
