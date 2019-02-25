package ru.nsu.fit.g16205.ivanishkin;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static java.lang.Math.abs;

public final class DrawingUtils {
    private static final Stroke ONE_PIXEL_STROKE = new BasicStroke(1);

    private DrawingUtils() {
    }

    public static void drawLine(Graphics g, Point a, Point b) {
        drawLine(g, a.x, a.y, b.x, b.y);
    }

    /**
     * Bresenham's line algorithm implementation
     *
     * @param g  Graphics to draw onto
     * @param x1 the first point's <i>x</i> coordinate.
     * @param y1 the first point's <i>y</i> coordinate.
     * @param x2 the second point's <i>x</i> coordinate.
     * @param y2 the second point's <i>y</i> coordinate.
     */
    @SuppressWarnings({"ConstantConditions", "SuspiciousNameCombination"})
    public static void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        try {
            if (!((Graphics2D) g).getStroke().equals(ONE_PIXEL_STROKE)) {
                System.err.println("Library drawing method used");
                g.drawLine(x1, y1, x2, y2);
                return;
            }
        } catch (ClassCastException e) {
            System.err.println("Library drawing method used");
            g.drawLine(x1, y1, x2, y2);
            throw e;
        }
        boolean leftToRight = x1 <= x2;
        boolean topToDown = y1 <= y2;
        boolean lowSlope = abs(y1 - y2) <= abs(x1 - x2);
        if (lowSlope && leftToRight) {
            drawCanonicalLine(g, x1, y1, x2, y2, false);
        } else if (lowSlope && !leftToRight) {
            drawCanonicalLine(g, x2, y2, x1, y1, false);
        } else if (!lowSlope && topToDown) { // big slope, swap axes
            drawCanonicalLine(g, y1, x1, y2, x2, true);
        } else {
            drawCanonicalLine(g, y2, x2, y1, x1, true);
        }
    }

    private static void drawCanonicalLine(Graphics g, int x1, int y1, int x2, int y2, boolean swappedAxes) {
        // Deltas and err are multiplied by abs(x1 - x2) to stay in integers
        final int deltaX = abs(x1 - x2);
        final int deltaErr = abs(y1 - y2);
        int err = 0; // Error between exact Y coordinate and the center of current pixel
        int directionY = y2 - y1;
        if (directionY != 0) {
            directionY /= abs(directionY); // == -1, 0, 1
        }

        int y = y1;
        int startX = x1;
        int startY = y1;

        for (int x = x1; x <= x2; x++) {
            err += deltaErr;
            if (2 * err >= deltaX || x == x2) { // err >= 0.5 * deltaX OR last iter
                // draw straight line
                if (swappedAxes) {
                    //noinspection SuspiciousNameCombination
                    g.drawLine(startY, startX, y, x);
                } else {
                    g.drawLine(startX, startY, x, y);
                }

                y += directionY;
                err -= deltaX;

                startX = x + 1;
                startY = y;
            }
        }
    }

    private static class Span {
        private final int fromX;
        private final int toX;
        private final int y;

        private Span(int fromX, int toX, int y) {
            this.fromX = fromX;
            this.toX = toX;
            this.y = y;
        }

        private List<Span> neighbours(BufferedImage img) {
            List<Span> spans = new ArrayList<>();
            // down
            Point iter = new Point(fromX, y + 1);
            for (int i = 0; i < 2; i++) {
                if (0 <= iter.y && iter.y < img.getHeight()) {
                    while (iter.x <= this.toX) {
                        // if connected
                        if (img.getRGB(iter.x, iter.y) == img.getRGB(this.fromX, this.y)) {
                            Span s = findSpan(iter, img);
                            spans.add(s);
                            iter.move(s.toX + 1, s.y);
                        } else {
                            iter.translate(1, 0);
                        }
                    }
                }
                // up
                iter.move(fromX, y - 1);
            }
            return spans;
        }
    }

    public static void spanFill(BufferedImage img, Point seed, Color newColor) {
        seed = seed.getLocation();
        int oldColor = img.getRGB(seed.x, seed.y);
        if (oldColor == newColor.getRGB()) {
            return;
        }
        Graphics2D g = img.createGraphics();
        g.setColor(newColor);
        Deque<Span> stack = new ArrayDeque<>();

        stack.push(findSpan(seed, img));

        while (!stack.isEmpty()) {
            Span span = stack.pop();
            if (img.getRGB(span.fromX, span.y) == oldColor) {
                stack.addAll(span.neighbours(img));
                g.drawLine(span.fromX, span.y, span.toX, span.y);
            }
        }
    }

    private static Span findSpan(Point seed, BufferedImage img) {
        seed = seed.getLocation();

        int seedColor = img.getRGB(seed.x, seed.y);
        int y = seed.y;
        int fromX = seed.x;
        int toX = seed.x;

        for (int x = seed.x; x - 1 >= 0 && x < img.getWidth(); x--) {
            fromX = x;
            if (seedColor != img.getRGB(x - 1, y)) {
                break;
            }
        }
        for (int x = seed.x; x >= 0 && x + 1 < img.getWidth(); x++) {
            toX = x;
            if (seedColor != img.getRGB(x + 1, y)) {
                break;
            }
        }
        return new Span(fromX, toX, y);
    }
}
