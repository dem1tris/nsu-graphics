package ru.nsu.fit.g16205.ivanishkin.geom;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.awt.geom.Point2D;

public class PointTranslator {
    private final RealMatrix m;

    public PointTranslator(RealMatrix m) {
        this.m = m;
    }

    public RealVector translate(RealVector v) {
        return m.operate(v);
    }

    public UniformPoint3D translate(UniformPoint3D p) {
        RealVector v = translate(new ArrayRealVector(new double[]{p.getX(), p.getY(), p.getZ(), p.getW()}));
        return new UniformPoint3D(v.toArray());
    }

    public Point2D translate(Point2D p) {
        RealVector v = translate(new ArrayRealVector(new double[]{p.getX(), p.getY(), 0, 1}));
        double[] xy = v.toArray();
        return new Point2D.Double(xy[0], xy[1]);
    }

    public RealMatrix getMatrix() {
        return m;
    }
}
