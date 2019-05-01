package ru.nsu.fit.g16205.ivanishkin.model;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import ru.nsu.fit.g16205.ivanishkin.utils.Utils;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.ToDoubleFunction;

import static ru.nsu.fit.g16205.ivanishkin.utils.Utils.reversed;

public class Spline {
    private static final int MIN_PIVOTS = 4;
    private static final RealMatrix M = new Array2DRowRealMatrix(
            new double[][]{
                    {-1, +3, -3, 1},
                    {+3, -6, +3, 0},
                    {-3, +0, +3, 0},
                    {+1, +4, +1, 0}
            }).scalarMultiply(1. / 6);
    private static final RealMatrix T = new Array2DRowRealMatrix(new double[][]{{1, 1, 1, 1}});

    private List<Point2D> pivots;
    private List<PolynomialFunction> xByTFuncs = new ArrayList<>();
    private List<PolynomialFunction> yByTFuncs = new ArrayList<>();

    public Spline(List<Point2D> pivots) {
        if (pivots.size() >= MIN_PIVOTS) {
            this.pivots = new ArrayList<>(pivots);
            rebuild();
        } else {
            throw new IllegalArgumentException("Pivots array must contain at least " + MIN_PIVOTS + " points");
        }
    }

    private void rebuild() {
        xByTFuncs.clear();
        yByTFuncs.clear();
        for (int i = 1; i < pivots.size() - 2; i++) {
            addPartFunction(xByTFuncs, i, Point2D::getX);
            addPartFunction(yByTFuncs, i, Point2D::getY);
        }
    }

    private void addPartFunction(List<PolynomialFunction> dest, int sublistIndex, ToDoubleFunction<? super Point2D> axisMapper) {
        RealMatrix G = new Array2DRowRealMatrix(
                pivots.subList(sublistIndex - 1, sublistIndex + 3)
                        .stream()
                        .mapToDouble(axisMapper)
                        .toArray()
        );
        PolynomialFunction TMG = new PolynomialFunction(reversed((M.multiply(G)).getColumn(0)));
        dest.add(TMG);
    }

    public List<Point2D> evaluate(int stepsPerSegment) {
        if (stepsPerSegment >= 1) {
            List<Point2D> points = new ArrayList<>();
            double delta = 1. / (stepsPerSegment);
            Point2D lastPoint = null;
            for (int i = 0; i < xByTFuncs.size(); i++) {
                for (double t = 0; t <= 1.; t += delta) {
                    Point2D p = new Point2D.Double(xByTFuncs.get(i).value(t), yByTFuncs.get(i).value(t));
                    if (!Objects.equals(lastPoint, p)) {
                        lastPoint = p;
                        points.add(lastPoint);
                    }
                }
            }
            return points;
        } else {
            throw new IllegalArgumentException("stepsPerSegment must be greater then 0");
        }
    }
}
