package cubes.skewb.imageGenerators;

import cubes.skewb.SkewbState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SkewbL2LImageGenerator {
    private static Map<Integer, Color> colorMap = new HashMap<>(){{
        put(0, Color.WHITE);
        put(1, Color.YELLOW);
        put(2, Color.LAWNGREEN);
        put(3, Color.RED);
        put(4, Color.BLUE);
        put(5, Color.ORANGE);
    }};

    public static void drawSkewbImage(GraphicsContext gc, String pattern, boolean bottom) {
        pattern = pattern.replace(" ", "");
        int[] colors = Arrays.stream(pattern.split("")).mapToInt(Integer::parseInt).toArray();
        drawTriangle(gc, 160, 0, 120, 15, 200, 15, colorMap.get(colors[0]));
        drawTriangle(gc, 200, 15, 240, 30, 200, 45, colorMap.get(colors[1]));
        drawTriangle(gc, 200, 45, 160, 60, 120, 45, colorMap.get(colors[2]));
        drawTriangle(gc, 120, 45, 80, 30, 120, 15, colorMap.get(colors[3]));
        drawRectangle(gc, 120, 15, 120, 45, 200, 45, 200, 15, colorMap.get(colors[4]));

//        if (bottom) {
//            drawTriangle(gc);
//            drawTriangle(gc);
//            drawTriangle(gc);
//            drawTriangle(gc);
//            drawRectangle(gc);
//        }
//
        drawTriangle(gc, 80, 30, 120, 45, 80, 80, colorMap.get(colors[10]));
        drawTriangle(gc, 120, 45, 160, 60, 160, 110, colorMap.get(colors[11]));
        drawTriangle(gc, 160, 110, 160, 160, 120, 145, colorMap.get(colors[12]));
        drawTriangle(gc, 120, 145, 80, 130, 80, 80, colorMap.get(colors[13]));
        drawRectangle(gc, 120, 45, 80, 80, 120, 145, 160, 110, colorMap.get(colors[14]));

        drawTriangle(gc, 160, 60, 200, 45, 160, 110, colorMap.get(colors[15]));
        drawTriangle(gc, 200, 45, 240, 30, 240, 80, colorMap.get(colors[16]));
        drawTriangle(gc, 240, 80, 240, 130, 200, 145, colorMap.get(colors[17]));
        drawTriangle(gc, 200, 145, 160, 160, 160, 110, colorMap.get(colors[18]));
        drawRectangle(gc, 160, 110, 200, 145, 240, 80, 200, 45, colorMap.get(colors[19]));
//
        drawTriangle(gc, 240, 30, 240, 80, 280, 15, colorMap.get(colors[20]));
        drawTriangle(gc, 280, 15, 320, 50, 320, 0, colorMap.get(colors[21]));
        drawTriangle(gc, 320, 50, 320, 100, 280, 115, colorMap.get(colors[22]));
        drawTriangle(gc, 280, 115, 240, 130, 240, 80, colorMap.get(colors[23]));
        drawRectangle(gc, 240, 80, 280, 115, 320, 50, 280, 15, colorMap.get(colors[24]));
//
        drawTriangle(gc, 0, 0, 0, 50, 40, 15, colorMap.get(colors[25]));
        drawTriangle(gc, 40, 15, 80, 30, 80, 80, colorMap.get(colors[26]));
        drawTriangle(gc, 80, 80, 80, 130, 40, 115, colorMap.get(colors[27]));
        drawTriangle(gc, 40, 115, 0, 100, 0, 50, colorMap.get(colors[28]));
        drawRectangle(gc, 0, 50, 40, 115, 80, 80, 40, 15, colorMap.get(colors[29]));
    }

    private static void drawRectangle(GraphicsContext gc, int a, int b, int c, int d, int e, int f, int g, int h, Color color) {
        gc.beginPath();
        gc.moveTo(a, b);
        gc.lineTo(c, d);
        gc.lineTo(e, f);
        gc.lineTo(g, h);
        gc.lineTo(a, b);
        gc.closePath();

        gc.setFill(color);
        gc.fill();

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.stroke();
    }

    private static void drawTriangle(GraphicsContext gc, int a, int b, int c, int d, int e, int f, Color color) {
        gc.beginPath();
        gc.moveTo(a, b);
        gc.lineTo(c, d);
        gc.lineTo(e, f);
        gc.lineTo(a, b);
        gc.closePath();

        gc.setFill(color);
        gc.fill();

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.stroke();
    }
}
