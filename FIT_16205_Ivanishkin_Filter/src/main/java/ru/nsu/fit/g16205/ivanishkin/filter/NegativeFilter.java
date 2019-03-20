package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class NegativeFilter implements Filter {
    @Override
    public BufferedImage apply(BufferedImage target) {
        if (target == null) {
            return null;
        }
        BufferedImage result = new BufferedImage(
                target.getColorModel(),
                target.copyData(null),
                target.isAlphaPremultiplied(),
                null);

        WritableRaster r = result.getRaster();
        int[] before = r.getPixels(0, 0, r.getWidth(), r.getHeight(), (int[]) null);
        int[] after = new int[before.length];
        for (int i = 0; i < target.getHeight() * target.getWidth() * 3; ++i) {
            after[i] = 255 - before[i];
        }
        r.setPixels(0, 0, r.getWidth(), r.getHeight(), after);
        return result;
    }
}
