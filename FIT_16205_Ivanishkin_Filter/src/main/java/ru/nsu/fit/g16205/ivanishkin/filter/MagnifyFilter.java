package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Random;

public class MagnifyFilter implements Filter {
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
            int x = i / 3 % r.getWidth();
            int y = i / 3 / r.getWidth();
            if (x % 2 == 0 && y % 2 == 0) {
                x = x / 2 + r.getWidth() / 4;
                y = y / 2 + r.getHeight() / 4;
                int ind = (y * r.getWidth() + x) * 3;
                after[i] = before[ind];
                after[i + 1] = before[ind + 1];
                after[i + 2] = before[ind + 2];
            } else {


                
            }

        }
        r.setPixels(0, 0, r.getWidth(), r.getHeight(), after);
        return result;
    }
}
