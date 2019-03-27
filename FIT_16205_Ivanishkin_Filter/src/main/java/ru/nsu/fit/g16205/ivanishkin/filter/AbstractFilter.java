package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public abstract class AbstractFilter implements Filter {
    protected BufferedImage result;
    protected WritableRaster raster;
    protected int width;
    protected int height;
    protected int[] before;
    protected int[] after;

    protected AbstractFilter() {}

    protected void prepareFor(BufferedImage target) {
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

    @Override
    public abstract BufferedImage apply(BufferedImage target);
}
