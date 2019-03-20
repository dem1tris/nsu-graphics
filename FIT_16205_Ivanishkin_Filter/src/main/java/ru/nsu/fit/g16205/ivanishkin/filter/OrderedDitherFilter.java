package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;
import java.util.Map;

import static java.lang.Math.round;

public class OrderedDitherFilter extends AbstractFilter {
    private int rLevels = 3;
    private int gLevels = 3;
    private int bLevels = 2;

    private double[][] matrix;
    private int dim;

    public OrderedDitherFilter(Map<String, Integer> params) {
        if (params.size() != 3) {
            throw new IllegalArgumentException("Parameters map should contain 3 entries");
        }
        rLevels = params.get("Red levels");
        gLevels = params.get("Green levels");
        bLevels = params.get("Blue levels");
        initMatrix(4);
    }

    private void initMatrix(int exp) {
        dim = 1 << exp;
        matrix = new double[dim][dim];
        for (int y = 0; y < dim; ++y) {
            for (int x = 0; x < dim; ++x) {
                int v = 0;
                int mask = exp - 1;
                int xored = x ^ y;
                for (int bit = 0; bit < 2 * exp; --mask) {
                    v |= ((y >> mask) & 1) << bit++;
                    v |= ((xored >> mask) & 1) << bit++;
                }
                matrix[y][x] = 1. * v / (dim * dim);
            }
        }
    }

    @Override
    public BufferedImage apply(BufferedImage target) {
        init(target);

        applyForColor(0, rLevels);
        applyForColor(1, gLevels);
        applyForColor(2, bLevels);

        raster.setPixels(0, 0, width, height, after);

        return result;
    }

    /**
     * Apply dithering for specified color
     *
     * @param colorIndex - place for color in triplet (0, 1 or 2)
     * @param levels     in new palette for specified color
     */
    private void applyForColor(int colorIndex, int levels) {
        int quant = 255 / (levels - 1);
        for (int i = 0; i < width * height * 3; i += 3) {
            int oldPixel = before[i + colorIndex];
            int x = toX(i);
            int y = toY(i);
            after[i + colorIndex] = closestColor((int) round(oldPixel + quant * (matrix[x % dim][y % dim] - 1. / 2)), levels);
        }
    }

    private int closestColor(int oldPixel, int levels) {
        int quant = 255 / (levels - 1);
        int newPixel = oldPixel / quant;
        newPixel += oldPixel % quant > quant / 2 ? 1 : 0;
        newPixel *= quant;
        return newPixel;
        //return (int) round(1. * oldPixel / quant) * quant;
    }
}
