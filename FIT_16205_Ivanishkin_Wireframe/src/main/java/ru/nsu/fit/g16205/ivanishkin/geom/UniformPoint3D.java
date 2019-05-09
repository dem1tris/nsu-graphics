package ru.nsu.fit.g16205.ivanishkin.geom;

public class UniformPoint3D {
    private final double x;
    private final double y;
    private final double z;
    private final double w;

    public UniformPoint3D(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public UniformPoint3D(double[] ary) {
        this(ary[0], ary[1], ary[2], ary[3]);
    }

    public UniformPoint3D(UniformPoint3D p) {
        this(p.x, p.y, p.z, p.w);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getW() {
        return w;
    }
}
