package ru.nsu.fit.g16205.ivanishkin.filter;

import ru.nsu.fit.g16205.ivanishkin.maths.Convolution;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RobertsMatrixFilter extends AbstractFilter {
    private final int threshold;
    private final int[][] firstMtx = {
            {1, 0},
            {0, -1}
    };
    private final int[][] secondMtx = {
            {0, 1},
            {-1, 0}
    };

    public RobertsMatrixFilter(int threshold) {
        this.threshold = threshold * threshold;
    }

    @Override
    public BufferedImage apply(BufferedImage target) {
        target = new GrayscaleFilter().apply(target);
        prepareFor(target);
        Dimension d = new Dimension(width, height);
        int[] first = new Convolution(firstMtx, false, false).apply(before, d);
        int[] second = new Convolution(secondMtx, false, false).apply(before, d);
        for (int i = 0; i < after.length; i++) {
            if (first[i] * first[i] + second[i] * second[i] > threshold) {
                after[i] = 254;
            } else {
                after[i] = 0;
            }
        }
        raster.setPixels(0, 0, width, height, after);
        return result;
    }
}
