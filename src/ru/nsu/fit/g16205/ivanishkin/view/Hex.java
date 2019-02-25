package ru.nsu.fit.g16205.ivanishkin.view;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import static ru.nsu.fit.g16205.ivanishkin.DrawingUtils.drawLine;
import static ru.nsu.fit.g16205.ivanishkin.DrawingUtils.spanFill;

public class Hex {

    private int mySize;
    private int myInRadius;
    private int myVertDistance;
    private Point center;
    private BufferedImage paintedOn;

    private static final int NCORNERS = 6;
    private static final String FONT_NAME = "Arial";
    private static int size = 30;
    private static int inRadius;
    private static int vertDistance;
    private static int horDistance;
    private static int height;
    private static boolean showImpact = false;
    private static Font font = new Font(FONT_NAME, Font.BOLD, size);

    public Point getPlaceInGrid() {
        return placeInGrid;
    }

    private Point placeInGrid;
    private Point[] corners = new Point[NCORNERS];

    static {
        setSize(size);
    }

    public static void setSize(int size) {
        Hex.size = size;
        Hex.height = 2 * size;
        Hex.vertDistance = (int) (2 * size * 3. / 4);
        Hex.inRadius = (int) round(size * sqrt(3) / 2);
        Hex.horDistance = 2 * Hex.inRadius;
        Hex.font = new Font(FONT_NAME, Font.BOLD, size);
    }

    public static void setShowImpact(boolean val) {
        showImpact = val;
    }


    public Hex(Point center, Point placeInGrid) {
        this.mySize = size;
        this.myVertDistance = vertDistance;
        this.myInRadius = inRadius;
        this.center = center.getLocation();
        this.placeInGrid = placeInGrid.getLocation();
        calculateCorners();
    }

    public void paintHex(BufferedImage img) {
        if (paintedOn != img) {
            updateSize();
            paintBorder(img);
            fill(img);
            drawImpact(img);
            paintedOn = img;
        }
    }

    public void fill(BufferedImage img) {
        spanFill(img, center, Color.GREEN);
    }

    public void drawImpact(BufferedImage img) {
        if (showImpact) {
            String str = "" + placeInGrid.y + "," + placeInGrid.x;
            Graphics2D g = img.createGraphics();
            //todo: draw impact
            g.setColor(Color.GRAY);
            g.setFont(font);
            FontMetrics metr = g.getFontMetrics(font);
            g.setClip(corners[3].x + 1, corners[3].y, 2 * inRadius - 1, size);
            g.drawString(str, center.x - metr.stringWidth(str) / 2.f,
                    center.y + metr.getAscent() - metr.getHeight() / 2.f);
        }
    }

    protected void paintBorder(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLACK);
        //todo: backup and restore color?
        for (int i = 0; i < NCORNERS; i++) {
            drawLine(g, corners[i], corners[(i + 1) % NCORNERS]);
        }
    }

    private void updateSize() {
        if (mySize != Hex.size) {
            int deltaSize = Hex.size - mySize;
            int deltaRadius = Hex.inRadius - myInRadius;
            int deltaY = Hex.vertDistance - myVertDistance;
            mySize = Hex.size;
            myVertDistance = Hex.vertDistance;
            myInRadius = Hex.inRadius;
            center.translate(deltaRadius * (2 * placeInGrid.x + 1) + (placeInGrid.y % 2 == 0 ? 0 : 1) * deltaRadius,
                    deltaSize + deltaY * placeInGrid.y);
            calculateCorners();
        }
    }

    private void calculateCorners() {
        for (int i = 0; i < NCORNERS; i++) {
            switch (i) {
                case 0:
                case 5:
                    corners[i] = new Point(center.x + inRadius, center.y + (i == 0 ? 1 : -1) * size / 2);
                    continue;
                case 1:
                case 4:
                    corners[i] = new Point(center.x, center.y + (i == 1 ? 1 : -1) * size);
                    continue;
                case 2:
                case 3:
                    corners[i] = new Point(center.x - inRadius, center.y + (i == 2 ? 1 : -1) * size / 2);
            }

        }
//        for (Point p : corners) {
//            System.out.print("(" + p.x + ", " + p.y + ") ");
//        }
//        System.out.println();
    }

    public static int getSize() {
        return size;
    }

    public static int getInRadius() {
        return inRadius;
    }

    public static int getHeight() {
        return height;
    }

    public static int getHorDistance() {
        return horDistance;
    }

    public static int getVertDistance() {
        return vertDistance;
    }
}
