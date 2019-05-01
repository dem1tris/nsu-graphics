package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.fit.g16205.ivanishkin.model.Spline;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

public class SplineView extends JPanel {
    private static int DETAILING = 50;
    private List<Point2D> points;
    private List<Point2D> pivots = Arrays.asList(
            new Point2D.Double(1., 1.),
            new Point2D.Double(2., 2.),
            new Point2D.Double(3., 1.),
            new Point2D.Double(9.5, 1.),
            new Point2D.Double(10., 7.),
            new Point2D.Double(4., 3.),
            new Point2D.Double(5., 3.)
    );

    public SplineView() {
        points = new Spline(pivots).evaluate(DETAILING);
    }

    public SplineView(Spline spline) {
        points = spline.evaluate(DETAILING);
    }

    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("SplineView.paintComponent");
        super.paintComponent(g);
        pivots.forEach(p -> g.fillOval((int) (p.getX() * 100), (int) (p.getY() * 100), 4, 4));
        points.forEach(System.out::println);
        for (int i = 0; i < points.size() - 1; i++) {
            Point2D p = points.get(i);
            Point2D pp = points.get(i + 1);
            g.drawLine(
                    (int) (p.getX() * 100), (int) (p.getY() * 100),
                    (int) (pp.getX() * 100), (int) (pp.getY() * 100));
        }
    }
}
