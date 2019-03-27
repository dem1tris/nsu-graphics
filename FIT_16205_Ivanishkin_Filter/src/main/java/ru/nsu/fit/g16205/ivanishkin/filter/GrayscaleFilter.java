package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;

public class GrayscaleFilter extends AbstractFilter {

    @Override
    public BufferedImage apply(BufferedImage target) {
        prepareFor(target);
        for (int i = 0; i < target.getHeight() * target.getWidth() * 3; i += 3) {
            int average = (before[i] + before[i + 1] + before[i + 2]) / 3;
            after[i] = average;
            after[i + 1] = average;
            after[i + 2] = average;
        }
        raster.setPixels(0, 0, raster.getWidth(), raster.getHeight(), after);
        return result;
    }
}
