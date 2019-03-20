package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import static java.lang.Math.abs;

public class SobelFilter extends AbstractFilter {
    private final int threshold;
    private final int filterHeight = 3;
    private final int filterWidth = 3;

    private final int[][] firstMtx = {
            {-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1}
    };
    private final int[][] secondMtx = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };


    public SobelFilter(int threshold) {
        this.threshold = threshold;
        System.err.println(threshold);
    }

    @Override
    public BufferedImage apply(BufferedImage target) {
        init(target);
        int[] first = applyMatrix(firstMtx);
        int[] second = applyMatrix(secondMtx);
        for (int i = 0; i < width * height * 3; i++) {
            if (first[i] + second[i] > threshold) {
                after[i] = 255;
            } else {
                after[i] = 0;
            }
        }
        raster.setPixels(0, 0, width, height, after);
        return result;
    }

    private int[] applyMatrix(int[][] mtx) {
        int[] res = new int[after.length];
        for (int i = 0; i < width * height * 3; i++) {
            for (int j = 0; j < filterHeight; j++) {
                for (int k = 0; k < filterWidth; k++) {
                    try {
                        res[i] += before[relInd(i, j - 1, k - 1)] * mtx[j][k];
                    } catch (IllegalArgumentException e) {
                        res[i] += 0;
                    }
                }
            }
            res[i] = abs(res[i]);
        }
        return res;
    }
}
