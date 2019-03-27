package ru.nsu.fit.g16205.ivanishkin.maths;

import ru.nsu.fit.g16205.ivanishkin.utils.IndexTranslator;

import java.awt.*;
import java.util.Arrays;

public class Convolution {
    private final int[][] kernel;
    private final int height;
    private final int width;
    private final boolean normalize;
    private final boolean round;
    private final double norm;
    private final int x0; // center X
    private final int y0; // center Y

    public Convolution(int[][] kernel, boolean normalize) {
        this(kernel, normalize, true);
    }

    public Convolution(int[][] kernel, boolean normalize, boolean round) {
        if (kernel == null || kernel.length <= 0 || kernel[0].length <= 0) {
            throw new IllegalArgumentException();
        }
        this.kernel = kernel;
        this.normalize = normalize;
        this.round = round;
        this.height = kernel.length;
        this.width = kernel[0].length;
        this.y0 = (height - 1) / 2;
        this.x0 = (width - 1) / 2;
        if (normalize) {
            norm = Arrays.stream(kernel).flatMapToInt(Arrays::stream).sum();
        } else {
            norm = 1;
        }
    }

    public int[] apply(int[] rasterData, Dimension d) {
        IndexTranslator t = new IndexTranslator(d.width, d.height);
        int[] res = new int[rasterData.length];
        for (int i = 0; i < rasterData.length; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    res[i] += rasterData[t.movedIndexExpanding(i, k - x0, j - y0) + i % 3] * kernel[j][k];
                }
            }
            if (normalize) {
                res[i] /= norm;
            }
            if (round) {
                res[i] = (res[i] < 0) ? 0 : (res[i] > 255 ? 255 : res[i]);
            }
        }
        return res;
    }

}
