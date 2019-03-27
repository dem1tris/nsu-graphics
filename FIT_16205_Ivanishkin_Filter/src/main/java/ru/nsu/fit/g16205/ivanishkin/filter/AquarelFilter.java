package ru.nsu.fit.g16205.ivanishkin.filter;

import ru.nsu.fit.g16205.ivanishkin.utils.IndexTranslator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

public class AquarelFilter extends AbstractFilter {
    @Override
    public BufferedImage apply(BufferedImage target) {
        prepareFor(target);
        IndexTranslator t = new IndexTranslator(width, height);
        ArrayList<Integer> list = new ArrayList<>(25);
        int size = 5;
        int x0 = 2;
        int y0 = 2;
        int median = 13;
        for (int i = 0; i < after.length; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    list.add(before[t.movedIndexExpanding(i, k - x0, j - y0) + i % 3]);
                }
            }
            list.sort(Comparator.naturalOrder());
            after[i] = list.get(median);
            list.clear();
        }

        raster.setPixels(0, 0, width, height, after);
        result = new SharpenMatrixFilter().apply(result);
        return result;
    }
}
