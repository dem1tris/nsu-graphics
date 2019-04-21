package ru.nsu.fit.g16205.ivanishkin.model;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.round;

public class Legend {
    private final List<Double> values;
    private final List<Color> colors;
    private final double delta;

    public Legend(double zMin, double zMax, List<Color> colors) {
        this.colors = colors;
        int nValues = colors.size() + 1;
        this.values = new ArrayList<>(nValues);

        delta = (zMax - zMin) / (nValues - 1);
        values.add(zMin);
        for (int i = 1; i < nValues - 1; i++) {
            values.add(zMin + i * delta);
        }
        values.add(zMax);
    }

    public ArrayList<Double> getLevels() {
        return new ArrayList<>(values);
    }

    public int getLevelsCount() {
        return values.size();
    }

    public Color colorAt(double val) {
        return colors.get(colorIndexAt(val));
    }

    public Color interpolatedAt(double val) {
        // shift color to the center of segment
        val = val - delta / 2;
        int left = colorIndexAt(val);
        if (left == colors.size() - 1) {
            return colorAt(val);
        } else {
            Color leftColor = colors.get(left);
            Color rightColor = colors.get(left + 1);
            double shift = (val - values.get(left)) / delta;
            shift = shift > 1 ? 1 : shift < 0 ? 0 : shift;
            int r = interpolate(leftColor.getRed(), rightColor.getRed(), shift);
            int g = interpolate(leftColor.getGreen(), rightColor.getGreen(), shift);
            int b = interpolate(leftColor.getBlue(), rightColor.getBlue(), shift);
            return ColorCache.getColor(r, g, b);
        }
    }

    //todo: private
    public int colorIndexAt(double val) {
        if (val < values.get(1)) {
            return 0;
        } else {
            int ind = (int) ((val - values.get(1)) / delta) + 1;
            return ind < colors.size() ? ind : colors.size() - 1;
        }
    }

    private int interpolate(int left, int right, double shift) {
            if (shift < 0 || shift > 1) {
                throw new IllegalArgumentException("" + shift);
            }
            int rounded = (int) round(left * (1 - shift) + right * shift);
            if (rounded < 0 || rounded > 255) {
                throw new RuntimeException();
            }
            return rounded;
        }

    private static class ColorCache {
        private static final Map<Integer, Color> cache = new HashMap<>();

        static Color getColor(int r, int g, int b) {
            int rgb = ((r & 0xFF) << 16) |
                    ((g & 0xFF) << 8) |
                    ((b & 0xFF));
            if (cache.containsKey(rgb)) {
                return cache.get(rgb);
            } else {
                Color color = new Color(r, g, b);
                cache.put(rgb, color);
                return color;
            }
        }
    }
}
