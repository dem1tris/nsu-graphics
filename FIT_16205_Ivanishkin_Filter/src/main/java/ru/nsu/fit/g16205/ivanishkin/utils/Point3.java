package ru.nsu.fit.g16205.ivanishkin.utils;


public class Point3 {
    public final double x;
    public final double y;
    public final double z;

    public Point3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3(final Point3 p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }
}
