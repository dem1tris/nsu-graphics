package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.fit.g16205.ivanishkin.geom.PointTranslator;
import ru.nsu.fit.g16205.ivanishkin.geom.PointTranslatorBuilder;
import ru.nsu.fit.g16205.ivanishkin.model.Spline;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static ru.nsu.fit.g16205.ivanishkin.utils.Utils.clearImg;
import static ru.nsu.fit.g16205.ivanishkin.utils.Utils.drawSegmented;

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
    private List<Point2D> pivots;

    {
        setPreferredSize(DIM);
    }

    public SplineView() {
        pivots = new CopyOnWriteArrayList<>(
                Arrays.asList(
                        new Point2D.Double(-150., -100.),
                        new Point2D.Double(-50., 100.),
                        new Point2D.Double(50., 100.),
                        new Point2D.Double(150., -100.)));
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

    // todo: fix spline cross pivots (reproduced once)
    public void addPivot(int x, int y) {
        PointTranslator t = new PointTranslatorBuilder().shift(-getWidth() / 2., -getHeight() / 2., 0).build();
        Point2D created = t.translate(new Point2D.Double(x, y));
        pivots.add(created);
        spline = new Spline(pivots, DETAILING);
        repaint();
    }

    public Point2D getPivot(int x, int y) {
        int index = getPivotIndex(x, y);
        if (index >= 0) {
            return pivots.get(index);
        } else {
            return null;
        }
    }

    public int getPivotIndex(int x, int y) {
        PointTranslator t = new PointTranslatorBuilder().shift(-getWidth() / 2., -getHeight() / 2., 0).build();
        Point2D clicked = t.translate(new Point2D.Double(x, y));
        int found = -1;
        for (int i = 0; i < pivots.size(); i++) {
            if (clicked.distance(pivots.get(i)) < MARKER_RADIUS) {
                System.err.println("FOUND!");
                found = i;
            }
        }
        return found;
    }

    public void replacePivot(int index, Point2D p) {
        PointTranslator t = new PointTranslatorBuilder().shift(-getWidth() / 2., -getHeight() / 2., 0).build();
        p = t.translate(p);
        pivots.set(index, p);
        spline = new Spline(pivots, DETAILING);
        repaint();
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

    public Spline getSpline() {
        return spline;
    }

    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("SplineView.paintComponent");
        drawSpline();
        g.drawImage(img, 0, 0, this);
    }

    private void drawSpline() {
        clearImg(img);
        Graphics2D g = img.createGraphics();
        PointTranslator t = new PointTranslatorBuilder().shift(getWidth() / 2., getHeight() / 2., 0).build();
        // axes
        g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight() - 1);
        g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);

        // pivots
        g.setStroke(pivotsStroke);
        g.setColor(pivotsColor);
        List<Point2D> translatedPivots = spline.getPivots().stream().map(t::translate).collect(Collectors.toList());
        translatedPivots.forEach(p -> g.drawOval(
                (int) (p.getX() - MARKER_RADIUS), (int) (p.getY() - MARKER_RADIUS),
                MARKER_RADIUS * 2, MARKER_RADIUS * 2));
        drawSegmented(g, translatedPivots, null);

        g.setStroke(splineStroke);
        g.setColor(Color.GRAY);
        drawSegmented(g, spline.getLeftTailPoints(), t);
        g.setColor(splineColor);
        drawSegmented(g, spline.getSubsplinePoints(), t);
        g.setColor(Color.GRAY);
        drawSegmented(g, spline.getRightTailPoints(), t);
    }
}
