package ru.nsu.fit.g16205.ivanishkin.filter;

import ru.nsu.fit.g16205.ivanishkin.maths.Convolution;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EmbossMatrixFilter extends AbstractFilter {
    private final int[][] kernel = new int[][] {
            { 0, +1,  0},
            {-1,  0, +1},
            { 0, -1,  0}
    };

    @Override
    public BufferedImage apply(BufferedImage target) {
        prepareFor(target);
        Dimension d = new Dimension(width, height);
        after = new Convolution(kernel, false, false).apply(before, d);
        for (int i = 1; i < after.length; i++) {
            after[i] = (after[i] >= 127) ? 255 : (after[i] < -128) ? 0 : after[i] + 128;
        }
        raster.setPixels(0, 0, width, height, after);
        return result;
    }
}
