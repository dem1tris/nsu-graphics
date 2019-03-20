package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MagnifyFilter extends AbstractFilter {
    private static int[][] NEIGHBOURS = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};
    private static int[][] NEIGHBOURS_IN_COL = {{0, 1}, {0, -1}};
    private static int[][] NEIGHBOURS_IN_ROW = {{-1, 0}, {1, 0}};

    @Override
    public BufferedImage apply(BufferedImage target) {
        init(target);

        // no interpolation
        for (int i = 0; i < width * height * 3; i += 3) {
            int x = toX(i);
            int y = toY(i);
            if (x % 2 == 0 && y % 2 == 0) {
                x = x / 2 + width / 4;
                y = y / 2 + height / 4;
                int ind = relInd(0, x, y);
                after[i] = before[ind];
                after[i + 1] = before[ind + 1];
                after[i + 2] = before[ind + 2];
            }
        }

        // bilinear, except last row and last column
        for (int i = 0; i < width * (height - 1) * 3; i += 3) {
            int x = toX(i);
            int y = toY(i);
            final int j = i;
            if (x % 2 != 0 && y % 2 != 0) {
                if (x + 1 == width) { // last column, take only left neighbours
                    after[i] = Arrays.stream(NEIGHBOURS).filter(it -> it[0] != 1)
                            .mapToInt(it -> after[relInd(j, it[0], it[1])]).sum() / 2;
                    after[i + 1] = Arrays.stream(NEIGHBOURS).filter(it -> it[0] != 1)
                            .mapToInt(it -> after[relInd(j, it[0], it[1]) + 1]).sum() / 2;
                    after[i + 2] = Arrays.stream(NEIGHBOURS).filter(it -> it[0] != 1)
                            .mapToInt(it -> after[relInd(j, it[0], it[1]) + 2]).sum() / 2;
                } else {
                    after[i] = Arrays.stream(NEIGHBOURS).mapToInt(it -> after[relInd(j, it[0], it[1])]).sum() / 4;
                    after[i + 1] = Arrays.stream(NEIGHBOURS).mapToInt(it -> after[relInd(j, it[0], it[1]) + 1]).sum() / 4;
                    after[i + 2] = Arrays.stream(NEIGHBOURS).mapToInt(it -> after[relInd(j, it[0], it[1]) + 2]).sum() / 4;
                }
            } else if (x % 2 == 0 && y % 2 != 0) { // odd row, linear in column
                after[i] = Arrays.stream(NEIGHBOURS_IN_COL).mapToInt(it -> after[relInd(j, it[0], it[1])]).sum() / 2;
                after[i + 1] = Arrays.stream(NEIGHBOURS_IN_COL).mapToInt(it -> after[relInd(j, it[0], it[1]) + 1]).sum() / 2;
                after[i + 2] = Arrays.stream(NEIGHBOURS_IN_COL).mapToInt(it -> after[relInd(j, it[0], it[1]) + 2]).sum() / 2;
            } else if (x % 2 != 0 && y % 2 == 0) { // odd column, linear in row
                if (x + 1 == width) { // last column, take 2 left neighbours in row
                    after[i] = (after[relInd(i, -1, 0)] * 2 + after[relInd(i, -2, 0)]) / 3;
                    after[i + 1] = (after[relInd(i, -1, 0) + 1] * 2 + after[relInd(i, -2, 0) + 1]) / 3;
                    after[i + 2] = (after[relInd(i, -1, 0) + 2] * 2 + after[relInd(i, -2, 0) + 2]) / 3;
                } else {
                    after[i] = Arrays.stream(NEIGHBOURS_IN_ROW).mapToInt(it -> after[relInd(j, it[0], it[1])]).sum() / 2;
                    after[i + 1] = Arrays.stream(NEIGHBOURS_IN_ROW).mapToInt(it -> after[relInd(j, it[0], it[1]) + 1]).sum() / 2;
                    after[i + 2] = Arrays.stream(NEIGHBOURS_IN_ROW).mapToInt(it -> after[relInd(j, it[0], it[1]) + 2]).sum() / 2;
                }
            }
        }

        // last row
        // take 2 up neighbours
        for (int i = width * (height - 1) * 3; i < width * height * 3; i += 3) {
            after[i] = (after[relInd(i, 0, -1)] * 2 + after[relInd(i, 0, -2)]) / 3;
            after[i + 1] = (after[relInd(i, 0, -1) + 1] * 2 + after[relInd(i, 0, -2) + 1]) / 3;
            after[i + 2] = (after[relInd(i, 0, -1) + 2] * 2 + after[relInd(i, 0, -2) + 2]) / 3;
        }
        raster.setPixels(0, 0, raster.getWidth(), raster.getHeight(), after);
        return result;
    }
}
