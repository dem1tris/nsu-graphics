package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class MagnifyFilter implements Filter {
    private static int[][] NEIGHBOURS = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};
    private int width;
    private int height;

    @Override
    public BufferedImage apply(BufferedImage target) {
        BufferedImage result = new BufferedImage(
                target.getColorModel(),
                target.copyData(null),
                target.isAlphaPremultiplied(),
                null);

        WritableRaster r = result.getRaster();
        width = r.getWidth();
        height = r.getHeight();
        int[] before = r.getPixels(0, 0, r.getWidth(), r.getHeight(), (int[]) null);
        int[] after = new int[before.length];
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
        for (int i = 0; i < width * (height - 1) * 3; i += 3) {
            int x = toX(i);
            int y = toY(i);
            if (x % 2 != 0 && y % 2 != 0) {
                final int j = i;
                after[i] = Arrays.stream(NEIGHBOURS).mapToInt(it -> after[relInd(j, it[0], it[1])]).sum() / 4;
                after[i + 1] = Arrays.stream(NEIGHBOURS).mapToInt(it -> after[relInd(j, it[0], it[1]) + 1]).sum() / 4;
                after[i + 2] = Arrays.stream(NEIGHBOURS).mapToInt(it -> after[relInd(j, it[0], it[1]) + 2]).sum() / 4;
            }
        }
        for (int i = width * (height - 1) * 3; i < width * height * 3; i+=3) {
            after[i] = (after[relInd(i, 0, -1)] * 2 + after[relInd(i, 0, -2)]) / 3;
            after[i + 1] = (after[relInd(i, 0, -1) + 1] * 2 + after[relInd(i, 0, -2) + 1]) / 3;
            after[i + 2] = (after[relInd(i, 0, -1) + 2] * 2 + after[relInd(i, 0, -2) + 2]) / 3;
        }
        r.setPixels(0, 0, r.getWidth(), r.getHeight(), after);
        return result;
    }

    private int toX(int i) {
        return i / 3 % width;
    }

    private int toY(int i) {
        return i / 3 / height;
    }

    /**
     * Returns index of translated pixel
     *
     * @param i  - index of current pixel
     * @param dx - translation by X
     * @param dy - translation by Y
     * @return relative index
     */
    private int relInd(int i, int dx, int dy) {
        int x = toX(i);
        int y = toY(i);
        return ((y + dy) * width + x + dx) * 3;
    }
}
