package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;

public class NegativeFilter extends AbstractFilter {
    @Override
    public BufferedImage apply(BufferedImage target) {
        prepareFor(target);
        for (int i = 0; i < target.getHeight() * target.getWidth() * 3; ++i) {
            after[i] = 255 - before[i];
        }
        raster.setPixels(0, 0, raster.getWidth(), raster.getHeight(), after);
        return result;
    }
}
