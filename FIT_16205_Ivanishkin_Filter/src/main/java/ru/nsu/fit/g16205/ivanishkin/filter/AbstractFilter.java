package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class AbstractFilter implements Filter {
    protected BufferedImage result;
    protected WritableRaster raster;
    protected int width;
    protected int height;
    protected int[] before;
    protected int[] after;

    protected AbstractFilter() {}

    protected void init(BufferedImage target) {
        if (target == null) {
            throw new IllegalArgumentException("Target image shouldn't be null");
        }
        result = new BufferedImage(
                target.getColorModel(),
                target.copyData(null),
                target.isAlphaPremultiplied(),
                null);

        raster = result.getRaster();
        width = raster.getWidth();
        height = raster.getHeight();
        before = raster.getPixels(0, 0, raster.getWidth(), raster.getHeight(), (int[]) null);
        after = new int[before.length];
    }

    protected int toX(int i) {
        return (i / 3) % width;
    }

    protected int toY(int i) {
        return (i / 3) / height;
    }

    protected int toI(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("x = " + x + ", y = " + y);
        }
        return (y * width + x) * 3;
    }

    /**
     * Returns index of translated pixel
     *
     * @param i  - index of current pixel
     * @param dx - translation by X
     * @param dy - translation by Y
     * @return relative index
     */
    protected int relInd(int i, int dx, int dy) {
        int x = toX(i);
        int y = toY(i);
        if (x + dx < 0 || x + dx >= width || y + dy < 0 || y + dy >= height) {
            throw new IllegalArgumentException("x = " + x + ", dx = " + dx + ", y = " + y + ", dy = " + dy);
        }
        return toI(x + dx, y + dy);
    }

    @Override
    public BufferedImage apply(BufferedImage target) {
        return null;
    }
}
