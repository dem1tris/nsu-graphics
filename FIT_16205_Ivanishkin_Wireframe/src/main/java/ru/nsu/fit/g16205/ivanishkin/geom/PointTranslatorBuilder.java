package ru.nsu.fit.g16205.ivanishkin.geom;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class PointTranslatorBuilder {
    private static final RealMatrix E = new Array2DRowRealMatrix(
            new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1}
            });

    private RealMatrix m = E;

    public PointTranslatorBuilder shift(double tx, double ty, double tz) {
        m = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 0, 0, tx},
                        {0, 1, 0, ty},
                        {0, 0, 1, tz},
                        {0, 0, 0, 1}
                }).multiply(m);
        return this;
    }

    public PointTranslatorBuilder scale(double sx, double sy, double sz) {
        m = new Array2DRowRealMatrix(
                new double[][]{
                        {sx, 0, 0, 0},
                        {0, sy, 0, 0},
                        {0, 0, sz, 0},
                        {0, 0, 0, 1}
                }).multiply(m);
        return this;
    }

    public PointTranslatorBuilder rotateOverX(double fi) {
//        double rad = Math.toRadians(fi);
        double c = Math.cos(fi);
        double s = Math.sin(fi);
        m = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 0, 0, 0},
                        {0, c, -s, 0},
                        {0, s, c, 0},
                        {0, 0, 0, 1}
                }).multiply(m);
        return this;
    }

    public PointTranslatorBuilder rotateOverY(double fi) {
//        double rad = Math.toRadians(fi);
        double c = Math.cos(fi);
        double s = Math.sin(fi);
        m = new Array2DRowRealMatrix(
                new double[][]{
                        {+c, 0, +s, 0},
                        {+0, 1, +0, 0},
                        {-s, 0, +c, 0},
                        {+0, 0, +0, 1}
                }).multiply(m);
        return this;
    }

    public PointTranslatorBuilder rotateOverZ(double fi) {
        //double rad = Math.toRadians(fi);
        double c = Math.cos(fi);
        double s = Math.sin(fi);
        m = new Array2DRowRealMatrix(
                new double[][]{
                        {c, -s, 0, 0},
                        {s, -c, 0, 0},
                        {0, +0, 1, 0},
                        {0, +0, 0, 1}
                }).multiply(m);
        return this;
    }

    public PointTranslatorBuilder pspProject(double d) {
        m = new Array2DRowRealMatrix(
                new double[][]{
                        {1, 0, 0, 0},
                        {0, 1, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 1 / d, 1}
                }).multiply(m);
        return this;
    }

    public PointTranslatorBuilder complex(PointTranslator t) {
        m = t.getMatrix().multiply(m);
        return this;
    }

    public PointTranslator build() {
        return new PointTranslator(m);
    }
}

