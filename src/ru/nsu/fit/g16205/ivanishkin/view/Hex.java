package ru.nsu.fit.g16205.ivanishkin.view;

import java.awt.*;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import static ru.nsu.fit.g16205.ivanishkin.DrawingUtils.drawLine;

public class Hex {
    private static int NCORNERS = 6;

    private static int size = 20;
    private int mySize;
    private int myWidth;
    private int myVertDistance;
    private static int width;
    private static int horDistance;
    private static int height;
    private static int vertDistance;

    private Point center;
    private Point placeInGrid;
    private Point[] corners = new Point[NCORNERS];

    static {
        setSize(size);
    }

    public static void setSize(int size) {

        Hex.size = size;
        Hex.height = 2 * size;

        double width = sqrt(3) * size;
        Hex.width = (int) round(sqrt(3) * size);
        if (Hex.width % 2 != 1) {
            if (Hex.width > width) {
                Hex.width -= 1;
            } else {
                Hex.width += 1;
            }
        }
        System.err.println("width = " + Hex.width);
        Hex.horDistance = Hex.width - 1;
        System.out.println("horDistance = " + horDistance);
        Hex.vertDistance = (int) (height * 3. / 4);
    }


    public Hex(Point center, Point placeInGrid) {
        this.mySize = size;
        this.myWidth = width;
        this.myVertDistance = vertDistance;
        this.center = center.getLocation();
        this.placeInGrid = placeInGrid.getLocation();
        calculateCorners();
    }

    public void paintHex(Graphics g) {
        updateSize();
        paintBorder(g);
        fill(g);
    }

    public void fill(Graphics g) {

    }

    protected void paintBorder(Graphics g) {
        for (int i = 0; i < NCORNERS; i++) {
            drawLine(g, corners[i], corners[(i + 1) % NCORNERS]);
        }
        //drawLine(g, center, center);
    }

    private void updateSize() {
        if (mySize != Hex.size) {
            int deltaSize = Hex.size - mySize;
            int deltaWidth = Hex.width - myWidth; // must be even
            int deltaY = Hex.vertDistance - myVertDistance;
            center.translate(deltaWidth / 2 * (2 * placeInGrid.x + 1) + (placeInGrid.y % 2 == 0 ? 0 : 1) * deltaWidth / 2,
                    deltaSize + deltaY * placeInGrid.y); //копится ошибка
            mySize = Hex.size;
            myWidth = Hex.width;
            myVertDistance = Hex.vertDistance;
            calculateCorners();
        }
    }

    private void calculateCorners() {
        for (int i = 0; i < NCORNERS; i++) {
            switch (i) {
                case 0:
                case 5:
                    corners[i] = new Point(center.x + width / 2, center.y + (i == 0 ? 1 : -1) * size / 2);
                    continue;
                case 1:
                case 4:
                    corners[i] = new Point(center.x, center.y + (i == 1 ? 1 : -1) * size);
                    continue;
                case 2:
                case 3:
                    corners[i] = new Point(center.x - width / 2, center.y + (i == 2 ? 1 : -1) * size / 2);
            }

        }
        for (Point p : corners) {
            System.out.print("(" + p.x + ", " + p.y + ") ");
        }
        System.out.println();
    }

    public static int getSize() {
        return size;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int getHorDistance() {
        return horDistance;
    }

    public static int getVertDistance() {
        return (int) vertDistance;
    }
}
