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
    private static final Map<Integer, Color> colorMap = new HashMap<>(){{
        put(0, Color.WHITE);
        put(1, Color.YELLOW);
        put(2, Color.LAWNGREEN);
        put(3, Color.RED);
        put(4, Color.BLUE);
        put(5, Color.ORANGE);
        put(6, Color.GRAY);
    }};

    public static void drawSkewbImage(GraphicsContext gc, String pattern, double scale,
                                      boolean bottom, boolean resizeCanvas) throws RuntimeException {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        pattern = pattern.replace(" ", "");

        gc.scale(scale, scale);
        double maxWidth = Math.ceil(scale*320);
        double maxHeight = Math.ceil(scale*180);

        if (resizeCanvas) {
            gc.getCanvas().setWidth(maxWidth);
            gc.getCanvas().setHeight(maxHeight);
        }

        int[] colors = Arrays.stream(pattern.split("")).mapToInt(Integer::parseInt).toArray(); //Can throw NumberFormatException here

        //Can throw arrayIndexOutOfBoundsException here that should be handled
        drawTriangle(gc, 160, 0, 120, 20, 200, 20, colorMap.get(colors[0]));
        drawTriangle(gc, 200, 20, 240, 40, 200, 60, colorMap.get(colors[1]));
        drawTriangle(gc, 200, 60, 160, 80, 120, 60, colorMap.get(colors[2]));
        drawTriangle(gc, 120, 60, 80, 40, 120, 20, colorMap.get(colors[3]));
        drawRectangle(gc, 120, 20, 120, 60, 200, 60, 200, 20, colorMap.get(colors[4]));


//        if (bottom) {
//            change Max height
//            drawTriangle(gc);
//            drawTriangle(gc);
//            drawTriangle(gc);
//            drawTriangle(gc);
//            drawRectangle(gc);
//        }
//
        drawTriangle(gc, 80, 40, 120, 60, 80, 90, colorMap.get(colors[10]));
        drawTriangle(gc, 120, 60, 160, 80, 160, 130, colorMap.get(colors[11]));
        drawTriangle(gc, 160, 130, 160, 180, 120, 160, colorMap.get(colors[12]));
        drawTriangle(gc, 120, 160, 80, 140, 80, 90, colorMap.get(colors[13]));
        drawRectangle(gc, 120, 60, 80, 90, 120, 160, 160, 130, colorMap.get(colors[14]));

        drawTriangle(gc, 160, 80, 200, 60, 160, 130, colorMap.get(colors[15]));
        drawTriangle(gc, 200, 60, 240, 40, 240, 90, colorMap.get(colors[16]));
        drawTriangle(gc, 240, 90, 240, 140, 200, 160, colorMap.get(colors[17]));
        drawTriangle(gc, 200, 160, 160, 180, 160, 130, colorMap.get(colors[18]));
        drawRectangle(gc, 160, 130, 200, 160, 240, 90, 200, 60, colorMap.get(colors[19]));
//
        drawTriangle(gc, 240, 40, 240, 90, 280, 20, colorMap.get(colors[20]));
        drawTriangle(gc, 280, 20, 320, 60, 320, 0, colorMap.get(colors[21]));
        drawTriangle(gc, 320, 50, 320, 100, 280, 120, colorMap.get(colors[22]));
        drawTriangle(gc, 280, 120, 240, 140, 240, 90, colorMap.get(colors[23]));
        drawRectangle(gc, 240, 90, 280, 120, 320, 50, 280, 20, colorMap.get(colors[24]));
//
        drawTriangle(gc, 0, 0, 0, 50, 40, 20, colorMap.get(colors[25]));
        drawTriangle(gc, 40, 20, 80, 40, 80, 90, colorMap.get(colors[26]));
        drawTriangle(gc, 80, 90, 80, 140, 40, 120, colorMap.get(colors[27]));
        drawTriangle(gc, 40, 120, 0, 100, 0, 50, colorMap.get(colors[28]));
        drawRectangle(gc, 0, 50, 40, 120, 80, 90, 40, 20, colorMap.get(colors[29]));
    }

    private static void drawRectangle(GraphicsContext gc, int a, int b, int c, int d, int e, int f, int g, int h, Color color) {
        gc.beginPath();
        gc.moveTo(a, b);
        gc.lineTo(c, d);
        gc.lineTo(e, f);
        gc.lineTo(g, h);
        gc.lineTo(a, b);
        gc.closePath();

        if (color == null) {
            color = Color.GRAY;
        }
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
