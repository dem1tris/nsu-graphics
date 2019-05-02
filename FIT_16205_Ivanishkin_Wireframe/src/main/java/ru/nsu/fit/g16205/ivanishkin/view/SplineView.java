package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.fit.g16205.ivanishkin.model.Spline;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class SplineView extends JPanel {
    private static final int DETAILING = 100;
    private static final int MARKER_RADIUS = 6;
    private static final Dimension DIM = new Dimension(900, 600);


    private BufferedImage img = new BufferedImage(DIM.width, DIM.height, BufferedImage.TYPE_INT_RGB);
    private Color splineColor = Color.WHITE;
    private Stroke splineStroke = new BasicStroke(2);
    private Color pivotsColor = Color.RED;
    private Stroke pivotsStroke = new BasicStroke(1);

    private Spline spline;
    private ArrayList<Point2D> pivots;

    {
        setPreferredSize(DIM);
    }

    public SplineView() {
        pivots = new ArrayList<>(
                Arrays.asList(
                        new Point2D.Double(100., 100.),
                        new Point2D.Double(200., 200.),
                        new Point2D.Double(300., 200.),
                        new Point2D.Double(400., 100.)));
//                new Point2D.Double(850., 100.)
//            new Point2D.Double(1000., 700.)
//            new Point2D.Double(400., 300.),
//            new Point2D.Double(500., 300.)
//        ));
        spline = new Spline(pivots, DETAILING);
    }

    public SplineView(Spline spline) {
        throw new RuntimeException("Not implemented");
//        points = spline.evaluate(DETAILING);
    }

    public void addPivot(int x, int y) {
        Point2D created = new Point2D.Double(x, y);
        pivots.add(created);
        spline = new Spline(pivots, DETAILING);
        repaint();
    }

    public Point2D getPivot(int x, int y) {
        Point2D clicked = new Point2D.Double(x, y);
        Point2D found = null;
        for (Point2D p : pivots) {
            System.err.println(clicked.distance(p));
            if (clicked.distance(p) < MARKER_RADIUS) {
                System.err.println("FOUND!");
                found = p;
            }
        }
        return found;
    }

    public void replacePivot(Point2D old, Point2D fresh) {
        int i = pivots.indexOf(old);
        if (i != -1) {
            pivots.set(i, fresh);
            spline = new Spline(pivots, DETAILING);
            repaint();
        } else {
            throw new NoSuchElementException("" + old);
        }
    }

    public void removePivot(Point2D old) {
        if (pivots.size() > 4) {
            if (!pivots.remove(old)) {
                throw new NoSuchElementException("" + old);
            }
            spline = new Spline(pivots, DETAILING);
            repaint();
        }
    }

    public Color getSplineColor() {
        return splineColor;
    }

    public void setSplineColor(Color color) {
        splineColor = color;
        repaint();
    }

    public void setHighlightedSegment(double start, double end) {
        spline.setSelectedSubspline(start, end);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSpline();
        g.drawImage(img, 0, 0, this);
    }

    private void drawSpline() {
        clearImg(img);
        Graphics2D g = img.createGraphics();
        g.setStroke(pivotsStroke);
        g.setColor(pivotsColor);
        spline.getPivots().forEach(p -> g.drawOval(
                (int) (p.getX() - MARKER_RADIUS), (int) (p.getY() - MARKER_RADIUS),
                MARKER_RADIUS * 2, MARKER_RADIUS * 2));

        g.setStroke(splineStroke);
        g.setColor(Color.GRAY);
        drawSegmented(g, spline.getLeftTailPoints());
        g.setColor(splineColor);
        drawSegmented(g, spline.getSubsplinePoints());
        g.setColor(Color.GRAY);
        drawSegmented(g, spline.getRightTailPoints());
    }

    private void drawSegmented(Graphics2D g, List<Point2D> points) {
        for (int i = 0; i < points.size() - 1; i++) {
            Point2D p = points.get(i);
            Point2D pp = points.get(i + 1);
            g.drawLine(
                    (int) (p.getX()), (int) (p.getY()),
                    (int) (pp.getX()), (int) (pp.getY()));
        }
    }

    private void clearImg(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
    }
}
