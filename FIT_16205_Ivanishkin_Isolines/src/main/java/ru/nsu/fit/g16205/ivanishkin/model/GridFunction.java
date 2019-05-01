package ru.nsu.fit.g16205.ivanishkin.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import static java.lang.Math.signum;

public class GridFunction {
    private static final double EPS = 1e-10;
    private final BiFunction<Double, Double, Double> fun;
    private double x0;
    private double y0;
    private double xEnd;
    private double yEnd;
    private int nx;

    public double x0() {
        return x0;
    }

    public double y0() {
        return y0;
    }

    public double xEnd() {
        return xEnd;
    }

    public double yEnd() {
        return yEnd;
    }

    public int nX() {
        return nx;
    }

    public int nY() {
        return ny;
    }

    private int ny;
    private double dx;
    private double dy;
    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;
    private List<Double> values;


    public GridFunction(BiFunction<Double, Double, Double> fun,
                        double x0, double y0, double xEnd, double yEnd, int nx, int ny) {
        this.fun = fun;
        this.x0 = x0;
        this.y0 = y0;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.nx = nx;
        this.ny = ny;
        this.dx = (xEnd - x0) / (nx - 1);
        this.dy = (yEnd - y0) / (ny - 1);
        // (0, 1) (1, 1)
        // (0, 0) (1, 0)
        ArrayList<Double> gridValues = new ArrayList<>(nx * ny);
        for (int i = 0; i < ny; i++) {
            for (int j = 0; j < nx; j++) {
                Double val = fun.apply(x0 + dx * j, y0 + dy * i);
                if (min > val) min = val;
                if (max < val) max = val;
                gridValues.add(val);
            }
        }
        values = Collections.unmodifiableList(gridValues);
    }

    // j
    // ^
    // |
    // |
    //  ----> i
    public double atNode(int ix, int jy) {
        if (ix < 0 || ix >= nx || jy < 0 || jy >= ny) {
            throw new IllegalArgumentException(ix + " " + jy + " " + nx + " " + ny);
        }
        return values.get(jy * nx + ix);
    }

    public double at(Point2D p) {
        return at(p.getX(), p.getY());
    }

    public double at(double x, double y) {
        if (x < x0 || x > xEnd || y < y0 || y > yEnd) {
            if (x0 - x <= EPS) {
                x = x0;
            }
            if (y0 - y <= EPS) {
                y = y0;
            }
            if (x - xEnd <= EPS) {
                x = xEnd;
            }
            if (y - yEnd <= EPS) {
                y = yEnd;
            }
            if (x < x0 || x > xEnd || y < y0 || y > yEnd) {
                throw new IllegalArgumentException("x == " + x + ", y == " + y);
            }
        }
        // left-top grid node coordinates
        int leftI = (int) ((x - x0) / dx);
        int topJ = (int) ((y - y0) / dy);


        if (leftI < 0 || topJ < 0 || leftI >= nx || topJ >= ny) throw new RuntimeException(leftI + " " + topJ);

        double deltaI = (x - (x0 + leftI * dx)) / dx;
        double deltaJ = (y - (y0 + topJ * dy)) / dy;
        deltaI = deltaI > 1 ? 1 : deltaI < 0 ? 0 : deltaI;
        deltaJ = deltaJ > 1 ? 1 : deltaJ < 0 ? 0 : deltaJ;

        if (deltaI < 0 || deltaI > 1 || deltaJ < 0 || deltaJ > 1) {
            throw new RuntimeException("deltaI == " + deltaI + ", deltaJ == " + deltaJ);
        }


        if (x < xEnd && y < yEnd) {
            return atNode(leftI, topJ) * (1 - deltaI) * (1 - deltaJ)
                    + atNode(leftI + 1, topJ) * deltaI * (1 - deltaJ)
                    + atNode(leftI, topJ + 1) * (1 - deltaI) * deltaJ
                    + atNode(leftI + 1, topJ + 1) * deltaI * deltaJ;
        } else if (x >= xEnd && y < yEnd) {
            return atNode(leftI, topJ) * (1 - deltaJ)
                    + atNode(leftI, topJ + 1) * deltaJ;
        } else if (x < xEnd && y >= yEnd) {
            return atNode(leftI, topJ) * (1 - deltaI)
                    + atNode(leftI + 1, topJ) * deltaI;
        } else {
            return atNode(leftI, topJ);
        }

    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }

    public List<Segment> getIsoline(double level) {
        final double EPS = 1e-6;
        ArrayList<Segment> isolineSegments = new ArrayList<>();

        for (int j = 0; j < ny - 1; j++) {
            for (int i = 0; i < nx - 1; i++) {
                ArrayList<Point2D> intersects = new ArrayList<>();
                double loX = x0 + dx * i;
                double hiX = x0 + dx * (i + 1);
                double loY = y0 + dy * j;
                double hiY = y0 + dy * (j + 1);

                double zTopLeft = atNode(i, j + 1);
                double zTopRight = atNode(i + 1, j + 1);
                double zBottomLeft = atNode(i, j);
                double zBottomRight = atNode(i + 1, j);

                // bottom
                if (isIntersected(zBottomLeft, zBottomRight, level)) {
                    double x = loX + dx * (level - zBottomLeft) / (zBottomRight - zBottomLeft);
                    Point2D.Double p = new Point2D.Double(x, loY);
                    intersects.add(p);
                }

                // top
                if (isIntersected(zTopLeft, zTopRight, level)) {
                    double x = loX + dx * (level - zTopLeft) / (zTopRight - zTopLeft);
                    Point2D.Double p = new Point2D.Double(x, hiY);
                    intersects.add(p);
                }

                // left
                if (isIntersected(zBottomLeft, zTopLeft, level)) {
                    double y = loY + dy * (level - zBottomLeft) / (zTopLeft - zBottomLeft);
                    Point2D.Double p = new Point2D.Double(loX, y);
                    intersects.add(p);
                }

                // right
                if (isIntersected(zBottomRight, zTopRight, level)) {
                    double y = loY + dy * (level - zBottomRight) / (zTopRight - zBottomRight);
                    Point2D.Double p = new Point2D.Double(hiX, y);
                    intersects.add(p);
                }

                switch (intersects.size()) {
                    case 0:
                        break;
                    case 2:
                        isolineSegments.add(new Segment(intersects.get(0), intersects.get(1)));
                        break;
                    case 4:
                        // signs are relative to isoline level
                        int centerSign = (int) signum((zBottomLeft + zBottomRight + zTopLeft + zTopRight) / 4 - level);
                        int topLeftSign = (int) signum(zTopLeft - level);

                        // top left corner and center are on the same height relative to isoline level
                        if (centerSign == topLeftSign) {
                            // bottom to left
                            isolineSegments.add(new Segment(intersects.get(0), intersects.get(2)));
                            // top to right
                            isolineSegments.add(new Segment(intersects.get(1), intersects.get(3)));
                        } else {
                            // bottom to right
                            isolineSegments.add(new Segment(intersects.get(0), intersects.get(3)));
                            // top to left
                            isolineSegments.add(new Segment(intersects.get(1), intersects.get(2)));
                        }
                        break;
                    default:
                        return getIsoline(level + EPS);
                }
            }
        }
        return isolineSegments;
    }

    private boolean isIntersected(double zLeft, double zRight, double z) {
        return zLeft <= z && z <= zRight || zLeft >= z && z >= zRight;
    }
}
