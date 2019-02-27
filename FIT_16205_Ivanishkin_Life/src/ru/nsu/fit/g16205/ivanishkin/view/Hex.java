package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.fit.g16205.ivanishkin.model.Cell;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Arrays;

import static java.lang.Math.*;
import static ru.nsu.fit.g16205.ivanishkin.utils.DrawingUtils.drawLine;
import static ru.nsu.fit.g16205.ivanishkin.utils.DrawingUtils.spanFill;

public class Hex {

    private int mySize;
    private int myInRadius;
    private int myVertDistance;
    private Point center;
    private Cell cell;
    private boolean valid = false;

    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private static final int NCORNERS = 6;
    private static final String FONT_NAME = "Arial";
    private static final int DEFAULT_SIZE = 20;
    private static int size;
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
        setSize(DEFAULT_SIZE);
    }

    public Hex(Point center, Cell cell) {
        this(center, cell.place());
        this.cell = cell;
    }

    public Hex(Point center, Point placeInGrid) {
        this.mySize = size;
        this.myVertDistance = vertDistance;
        this.myInRadius = inRadius;
        this.center = center.getLocation();
        this.placeInGrid = placeInGrid.getLocation();
        calculateCorners();
    }

    static void setSize(int size) {
        if (Hex.size != size) {
            Hex.size = size;
            Hex.height = 2 * size;
            Hex.vertDistance = (int) (2 * size * 3. / 4);
            Hex.inRadius = (int) round(size * sqrt(3) / 2);
            Hex.horDistance = 2 * Hex.inRadius;
            Hex.font = new Font(FONT_NAME, Font.BOLD, size);
        }
    }

    public static void setShowImpact(boolean val) {
        showImpact = val;
    }

    public static boolean needShowImpact() {
        return showImpact;
    }

    public void paintHex(BufferedImage gridImg, BufferedImage impactImg, BasicStroke stroke, Shape clip) {
        if (!valid && Arrays.stream(corners).anyMatch(clip::contains)) {
            updateSize();
            paintBorder(gridImg, stroke);
            fill(gridImg);
            drawImpact(impactImg);
            valid = true;
        }
    }

    public void fill(BufferedImage img) {
        if (cell.isAlive()) {
            spanFill(img, center, Color.GREEN);
        } else {
            spanFill(img, center, Color.LIGHT_GRAY);
        }
    }

    public void drawImpact(BufferedImage img) {
        if (showImpact) {
            String str;
            Graphics2D g = img.createGraphics();

            Rectangle clip = new Rectangle(corners[3].x + 1, corners[3].y, 2 * inRadius - 1, size);
            g.setClip(clip);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
            g.setColor(TRANSPARENT);
            g.fill(clip);

            double impact = BigDecimal.valueOf(cell.getImpact())
                    .setScale(1, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            if (impact - floor(impact) < 0.1) {
                str = Integer.toString((int) impact);
            } else {
                str = Double.toString(impact);
            }
            g.setColor(Color.GRAY);
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);
            g.drawString(str, center.x - metrics.stringWidth(str) / 2.f,
                    center.y + metrics.getAscent() - metrics.getHeight() / 2.f);
        }
    }

    protected void paintBorder(BufferedImage img, BasicStroke stroke) {
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLACK);
        g.setStroke(stroke);
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
    }

    public void invalidate() {
        valid = false;
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
