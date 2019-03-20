package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public class RotateFilter extends AbstractFilter {
    private int angle;

    public RotateFilter(int angle) {
        if (angle < -180 || angle > 180) {
            throw new IllegalArgumentException("Angle should be between -180 and 180, inclusively");
        }
        this.angle = angle;
    }

    @Override
    public BufferedImage apply(BufferedImage target) {
        init(target);
        if (angle == -180 || angle == 180) {
            for (int i = 0; i < width * height * 3; i++) {
                after[i] = before[toI(width - toX(i) - 1, height - toY(i) - 1) + i % 3];
            }
        } else if (angle == 90) {
            for (int i = 0; i < width * height * 3; i++) {
                after[i] = before[toI(toY(i), width - toX(i) - 1) + i % 3];
            }
        } else if (angle == -90) {
            for (int i = 0; i < width * height * 3; i++) {
                after[i] = before[toI(height - toY(i) - 1, toX(i)) + i % 3];
            }
        } else {
            double cos = cos(toRadians(angle));
            double sin = sin(toRadians(angle));
            double[][] invRotMatrix = {
                    {cos, sin},
                    {-sin, cos}
            };
            for (int i = 0; i < width * height * 3; i++) {
                double[][] m = invRotMatrix;
                int x = toX(i) - width / 2;
                int y = toY(i) - height / 2;
                after[i] = extended(
                        x * m[0][0] + y * m[0][1] + width / 2.,
                        x * m[1][0] + y * m[1][1] + height / 2.,
                        i % 3
                );
            }
        }
        raster.setPixels(0, 0, width, height, after);
        return result;
    }

    private int extended(double x, double y, int colorOffset) {
        int xx = (int) round(x + 0.5);
        int yy = (int) round(y + 0.5);
        if (xx < 0 || xx >= width || yy < 0 || yy >= height) {
            return 255;
        } else {
            return before[toI(xx, yy) + colorOffset];
        }
    }
}
