package ru.nsu.fit.g16205.ivanishkin.filter;

import ru.nsu.fit.g16205.ivanishkin.maths.Convolution;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BlurMatrixFilter extends AbstractFilter {
    private int[][] matrix = {
            {1, 2, 3, 2, 1},
            {2, 4, 5, 4, 2},
            {3, 5, 6, 5, 3},
            {2, 4, 5, 4, 2},
            {1, 2, 3, 2, 1}
    };


    @Override
    public BufferedImage apply(BufferedImage target) {
        prepareFor(target);
        after = new Convolution(matrix, true).apply(before, new Dimension(width, height));
        raster.setPixels(0, 0, width, height, after);
        return result;
    }
}
