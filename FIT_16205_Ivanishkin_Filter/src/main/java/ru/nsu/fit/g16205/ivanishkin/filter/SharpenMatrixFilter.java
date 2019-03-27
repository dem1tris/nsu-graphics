package ru.nsu.fit.g16205.ivanishkin.filter;

import ru.nsu.fit.g16205.ivanishkin.maths.Convolution;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SharpenMatrixFilter extends AbstractFilter {
    private final int[][] matrix = new int[][]{
            {0, -1, 0},
            {-1, 5, -1},
            {0, -1, 0}
    };

    @Override
    public BufferedImage apply(BufferedImage target) {
        prepareFor(target);
        Dimension d = new Dimension(width, height);
        after = new Convolution(matrix, false).apply(before, d);
        raster.setPixels(0, 0, width, height, after);
        return result;
    }
}
