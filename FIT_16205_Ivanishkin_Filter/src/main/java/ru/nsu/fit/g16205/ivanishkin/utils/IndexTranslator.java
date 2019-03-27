package ru.nsu.fit.g16205.ivanishkin.utils;

public class IndexTranslator {
    private final int width;
    private final int height;

    public IndexTranslator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int toX(int i) {
        return (i / 3) % width;
    }

    public int toY(int i) {
        return (i / 3) / height;
    }

    public int toI(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("x = " + x + ", y = " + y);
        }
        return (y * width + x) * 3;
    }

    /**
     * Returns index of translated pixel
     *
     * @param i  - index of current pixel
     * @param dx - translation by X
     * @param dy - translation by Y
     * @return relative index
     */
    public int movedIndex(int i, int dx, int dy) {
        int x = toX(i);
        int y = toY(i);
        if (x + dx < 0 || x + dx >= width || y + dy < 0 || y + dy >= height) {
            return -1;
        }
        return toI(x + dx, y + dy);
    }

    /**
     * Returns index of translated pixel.
     * Returns pixel from border in case of out of bounds coordinates;
     *
     * @param i  - index of current pixel
     * @param dx - translation by X
     * @param dy - translation by Y
     * @return relative index
     */
    public int movedIndexExpanding(int i, int dx, int dy) {
        int x = toX(i) + dx;
        int y = toY(i) + dy;
        x = (x < 0) ? 0 : (x >= width ? width - 1 : x);
        y = (y < 0) ? 0 : (y >= height ? height - 1 : y);

        return toI(x, y);
    }
}
