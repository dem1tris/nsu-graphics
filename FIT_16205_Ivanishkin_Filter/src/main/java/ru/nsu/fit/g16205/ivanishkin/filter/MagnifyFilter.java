package ru.nsu.fit.g16205.ivanishkin.filter;

import ru.nsu.fit.g16205.ivanishkin.utils.IndexTranslator;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MagnifyFilter extends AbstractFilter {
    private static int[][] NEIGHBOURS = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};
    private static int[][] LEFT_NEIGHBOURS = {{-1, -1}, {-1, 1}};
    private static int[][] NEIGHBOURS_IN_COL = {{0, 1}, {0, -1}};
    private static int[][] NEIGHBOURS_IN_ROW = {{-1, 0}, {1, 0}};
    private IndexTranslator t;


    @Override
    public BufferedImage apply(BufferedImage target) {
        prepareFor(target);
        t = new IndexTranslator(width, height);

        // no interpolation
        for (int i = 0; i < width * height * 3; i += 3) {
            int x = t.toX(i);
            int y = t.toY(i);
            if (x % 2 == 0 && y % 2 == 0) {
                x = x / 2 + width / 4;
                y = y / 2 + height / 4;
                int ind = t.movedIndex(0, x, y);
                after[i] = before[ind];
                after[i + 1] = before[ind + 1];
                after[i + 2] = before[ind + 2];
            }
        }

        // bilinear, except last row and last column
        for (int i = 0; i < width * (height - 1) * 3; i++) {
            int x = t.toX(i);
            int y = t.toY(i);
            if (x % 2 != 0 && y % 2 != 0) {
                if (x + 1 == width) { // last column, take only left neighbours
                    after[i] = averageFromNeighbours(LEFT_NEIGHBOURS, i);
                } else {
                    after[i] = averageFromNeighbours(NEIGHBOURS, i);
                }
            } else if (x % 2 == 0 && y % 2 != 0) { // odd row, linear in column
                after[i] = averageFromNeighbours(NEIGHBOURS_IN_COL, i);
            } else if (x % 2 != 0 && y % 2 == 0) { // odd column, linear in row
                if (x + 1 == width) { // last column, take 2 left neighbours in row
                    after[i] = (after[t.movedIndex(i, -1, 0) + i % 3] * 2
                            + after[t.movedIndex(i, -2, 0) + i % 3]) / 3;
                } else {
                    after[i] = averageFromNeighbours(NEIGHBOURS_IN_ROW, i);
                }
            }
        }

        // last row
        // take 2 up neighbours
        for (int i = width * (height - 1) * 3; i < width * height * 3; i++) {
            after[i] = (after[t.movedIndex(i, 0, -1) + i % 3] * 2
                    + after[t.movedIndex(i, 0, -2) + i % 3]) / 3;
        }
        raster.setPixels(0, 0, raster.getWidth(), raster.getHeight(), after);
        return result;
    }

    private int averageFromNeighbours(int[][] offsets, int subpixelIndex) {
        return Arrays.stream(offsets)
                .mapToInt(it -> after[t.movedIndex(subpixelIndex, it[0], it[1]) + subpixelIndex % 3])
                .sum() / offsets.length;
    }
}
