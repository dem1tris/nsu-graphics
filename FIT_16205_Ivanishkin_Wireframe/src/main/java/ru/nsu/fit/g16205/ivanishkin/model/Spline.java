package ru.nsu.fit.g16205.ivanishkin.model;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.ToDoubleFunction;

import static ru.nsu.fit.g16205.ivanishkin.utils.Utils.reversed;

public class Spline {

    //region static
    private static final int MIN_PIVOTS = 4;
    private static final RealMatrix M = new Array2DRowRealMatrix(
            new double[][]{
                    {-1, +3, -3, 1},
                    {+3, -6, +3, 0},
                    {-3, +0, +3, 0},
                    {+1, +4, +1, 0}
            }).scalarMultiply(1. / 6);
    private static final RealMatrix T = new Array2DRowRealMatrix(new double[][]{{1, 1, 1, 1}});
    //endregion

    private final List<Point2D> pivots;
    private List<Point2D> points = new ArrayList<>();
    private double length;
    private double startRatio;
    private double endRatio;
    private int subsplineStart;
    private int subsplineEnd;
    private List<PolynomialFunction> xByTFuncs = new ArrayList<>();
    private List<PolynomialFunction> yByTFuncs = new ArrayList<>();

    public Spline(List<Point2D> pivots, int stepsPerSegment) {
        if (stepsPerSegment < 1) {
            throw new IllegalArgumentException("stepsPerSegment must be greater then 0");
        }
        if (pivots.size() >= MIN_PIVOTS) {
            this.pivots = pivots;
//            this.pivots = Collections.unmodifiableList(pivots);
            rebuild();
            evaluate(stepsPerSegment);
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

    private void evaluate(int stepsPerSegment) {
        if (points.size() != 0 || length != 0) {
            throw new IllegalStateException("Already evaluated");
        }
        double delta = 1. / (stepsPerSegment);
        Point2D lastPoint = null;

        for (int i = 0; i < xByTFuncs.size(); i++) {
            for (double t = 0; t <= 1.; t += delta) {
                Point2D p = new Point2D.Double(xByTFuncs.get(i).value(t), yByTFuncs.get(i).value(t));
                if (lastPoint != null) {
                    length += p.distance(lastPoint);
                }
                lastPoint = p;
                points.add(lastPoint);
            }
        }
        points = Collections.unmodifiableList(points);
        startRatio = 0;
        endRatio = 1;
        subsplineStart = 0;
        subsplineEnd = points.size() - 1;
    }

    public List<Point2D> getPivots() {
        // is unmodifiable
        return pivots;
    }

    public List<Point2D> getPoints() {
        // is unmodifiable
        return points;
    }

    public List<Point2D> getSubsplinePoints() {
        return points.subList(subsplineStart, subsplineEnd + 1);
    }

    public List<Point2D> getLeftTailPoints() {
        return points.subList(0, subsplineStart + 1);
    }

    public List<Point2D> getRightTailPoints() {
        return points.subList(subsplineEnd, points.size());
    }

    /**
     * Returns index of point that split spline by specified ratio
     *
     * @param ratio in [0, 1]. Part of spline's length to split
     * @return index of first point after (or exactly at) the specified part of spline
     */
    public int getSplitPoint(double ratio) {
        // todo: read task, need function u -> {k, t}
        if (ratio < 0 || ratio > 1) {
            throw new IllegalArgumentException(ratio + " not in [0, 1]");
        } else if (ratio == 0) {
            return 0;
        } else if (ratio == 1) {
            return points.size() - 1;
        }
        double l = 0;
        double skipLength = length * ratio;
        for (int i = 1; i < points.size(); i++) {
            if (l >= skipLength) {
                return i;
            }
            l += points.get(i - 1).distance(points.get(i));
        }
        return points.size() - 1;
    }

    /**
     * @param detailing
     * @return indices of points, crossed by parallels, in subspline points
     */
    public int[] getParallelsPoints(int detailing) {
        List<Point2D> subsplinePoints = getSubsplinePoints();
        int[] indices = new int[detailing + 1];
        indices[0] = 0;
        indices[detailing] = subsplinePoints.size() - 1;

        double len = 0;
        int j = 1;
        double skipLength = length * (endRatio - startRatio) / detailing;

        for (int i = 1; i < subsplinePoints.size(); i++) {
            if (len >= skipLength) {
                len -= skipLength;
                indices[j++] = i;
            }
            len += subsplinePoints.get(i - 1).distance(subsplinePoints.get(i));
        }

        if (j != detailing) {
            throw new IllegalStateException("" + j);
        }
        return indices;
    }

    public void setSelectedSubspline(double startRatio, double endRatio) {
        if (startRatio < 0 || startRatio > 1 || startRatio > endRatio || endRatio < 0 || endRatio > 1) {
            throw new IllegalArgumentException(startRatio + ", " + endRatio);
        }
        this.startRatio = startRatio;
        this.endRatio = endRatio;
        subsplineStart = getSplitPoint(startRatio);
        subsplineEnd = getSplitPoint(endRatio);
    }

    public double length() {
        return length;
    }
}
