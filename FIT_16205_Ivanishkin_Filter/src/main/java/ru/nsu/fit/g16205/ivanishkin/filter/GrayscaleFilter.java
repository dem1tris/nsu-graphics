package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class GrayscaleFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage target) {
        BufferedImage result = new BufferedImage(
                target.getColorModel(),
                target.copyData(null),
                target.isAlphaPremultiplied(),
                null);

        WritableRaster r = result.getRaster();
        int[] before = r.getPixels(0, 0, r.getWidth(), r.getHeight(), (int[]) null);
        int[] after = new int[before.length];
        for (int i = 0; i < target.getHeight() * target.getWidth() * 3; i += 3) {
            int average = (before[i] + before[i + 1] + before[i + 2]) / 3;
            after[i] = average;
            after[i + 1] = average;
            after[i + 2] = average;
        }
        r.setPixels(0, 0, r.getWidth(), r.getHeight(), after);
        return result;
    }
}
