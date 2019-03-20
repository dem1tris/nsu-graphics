package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public class GammaFilter extends AbstractFilter {
    private final double gamma;

    public GammaFilter(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public BufferedImage apply(BufferedImage target) {
        init(target);
        System.err.println(gamma);
        for (int i = 0; i < width * height * 3; i++) {
            after[i] = (int) round(pow(1. * before[i] / 255, gamma) * 255);
        }
        raster.setPixels(0, 0, width, height, after);
        return result;
    }
}
