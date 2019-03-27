package ru.nsu.fit.g16205.ivanishkin.filter;

import ru.nsu.fit.g16205.ivanishkin.utils.IndexTranslator;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;

import static java.lang.Math.round;

public class FloydStainbergDitherFilter extends AbstractFilter {
    private int rLevels;
    private int gLevels;
    private int bLevels;

    public FloydStainbergDitherFilter(Map<String, Integer> params) {
        if (params.size() != 3) {
            throw new IllegalArgumentException("Parameters map should contain 3 entries");
        }
        rLevels = params.get("Red levels");
        gLevels = params.get("Green levels");
        bLevels = params.get("Blue levels");
    }

    @Override
    public BufferedImage apply(BufferedImage target) {
        prepareFor(target);
        after = Arrays.copyOf(before, before.length);

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
        IndexTranslator t = new IndexTranslator(width, height);
        for (int i = 0; i < width * height * 3; i += 3) {
            int oldPixel = after[i + colorIndex]; // intentionally after, not before
            if (oldPixel < 0) {
                oldPixel = 0;
            } else if (oldPixel > 255){
                oldPixel = 255;
            }
            int newPixel = closestColor(oldPixel, levels);
            int err = oldPixel - newPixel;
            after[i + colorIndex] = newPixel;
            int x = t.toX(i);
            int y = t.toY(i);
            if (x + 1 != width) {
                after[t.movedIndex(i, 1, 0) + colorIndex] += (int) round(err * 7. / 16);
            }
            if (x != 0 && y + 1 != height) {
                after[t.movedIndex(i, -1, 1) + colorIndex] += (int) round(err * 3. / 16);
            }
            if (y + 1 != height) {
                after[t.movedIndex(i, 0, 1) + colorIndex] += (int) round(err * 5. / 16);
            }
            if (x + 1 != width && y + 1 != height) {
                after[t.movedIndex(i, 1, 1) + colorIndex] += (int) round(err * 1. / 16);
            }
        }
    }

    private int closestColor(int oldPixel, int levels) {
        int quant = 255 / (levels - 1);
        return (int) round(1. * oldPixel / quant) * quant;
    }

}
