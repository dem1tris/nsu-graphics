package ru.nsu.fit.g16205.ivanishkin.view;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import static java.lang.Math.*;
import static ru.nsu.fit.g16205.ivanishkin.DrawingUtils.drawLine;

public class Hex {
    private static int NCORNERS = 6;

    private static int size = 20;
    private int mySize;
    private static int width;
    private static int horDistance;
    private static int height;
    private static int vertDistance;

    private final Point center;
    private Point[] corners = new Point[NCORNERS];

    public static void setSize(int size) {
        Hex.size = size;
        Hex.height = 2 * size;

        double width = sqrt(3) * size;
        Hex.width = (int) round(sqrt(3) * size);
        if (Hex.width % 2 != 1){
            if (Hex.width > width) {
                Hex.width -= 1;
            } else {
                Hex.width += 1;
            }
        }
        System.err.println("width = " + width);
        Hex.horDistance = Hex.width / 2 * 2;
        System.out.println("horDistance = " + horDistance);
        Hex.vertDistance = (int) (height * 3. / 4);
    }


    public Hex(Point center) {
        setSize(size);
        this.center = center.getLocation();
        calculateCorners();
    }

    public Hex(int x, int y) {
        this(new Point(x, y));
    }

    public void paint(Graphics g) {
        calculateCorners();
        paintBorder(g);
        fill(g);
    }

    public void fill(Graphics g) {

    }

    protected void paintBorder(Graphics g) {
        for (int i = 0; i < NCORNERS; i++) {
            drawLine(g, corners[i], corners[(i + 1) % NCORNERS]);
        }
        g.setColor(Color.RED);
        drawLine(g, corners[4], corners[4]);
        g.setColor(Color.BLUE);
        drawLine(g, center, center);
        g.setColor(Color.BLACK);
    }

    private void calculateCorners() {
        if (this.mySize != Hex.size) {
            this.mySize = Hex.size;
            for (int i = 0; i < NCORNERS; i++) {
                switch (i) {
                    case 0:
                    case 5:
                        corners[i] = new Point(center.x + (int) width / 2, center.y + (i == 0 ? 1 : -1) * size / 2);
                        continue;
                    case 1:
                    case 4:
                        corners[i] = new Point(center.x, center.y + (i == 1 ? 1 : -1) * size);
                        continue;
                    case 2:
                    case 3:
                        corners[i] = new Point(center.x - (int) width / 2, center.y + (i == 2 ? 1 : -1) * size / 2);
                }
//            switch (i) {
//                case 0:
//                case 1:
//                case 2:
//                    corners[i] = calculateCorner(i);
//                    continue;
//                case 3:
//                case 4:
//                case 5:
//                    corners[i] = new Point(corners[5 - i].x, corners[5 - i].y - size - (i == 4 ? size : 0));
//            }
            }
            for (Point p : corners) {
                System.out.print("(" + p.x + ", " + p.y + ") ");
            }
            System.out.println();
        }
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
