package ru.nsu.fit.g16205.ivanishkin.volumeRendering;

import ru.nsu.fit.g16205.ivanishkin.filter.AbstractFilter;
import ru.nsu.fit.g16205.ivanishkin.utils.IndexTranslator;
import ru.nsu.fit.g16205.ivanishkin.utils.Pair;
import ru.nsu.fit.g16205.ivanishkin.utils.Point3;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.*;

public class Renderer extends AbstractFilter {
    private static int N = 100;
    private final int nx;
    private final int ny;
    private final int nz;
    private final Config config;
    private final List<Double> absorption;
    private final List<Double> rEmission;
    private final List<Double> gEmission;
    private final List<Double> bEmission;
    private final boolean needAbsorption;
    private final boolean needEmission;

    public Renderer(Config config, int nx, int ny, int nz, boolean absorption, boolean emission) {
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
        this.config = config;
        this.needAbsorption = absorption;
        this.needEmission = emission;
        this.absorption = interpolated(config.dotsAbsorption);
        this.rEmission = interpolated(config.dotsRedEmission);
        this.gEmission = interpolated(config.dotsGreenEmission);
        this.bEmission = interpolated(config.dotsBlueEmission);
    }

    @Override
    public BufferedImage apply(BufferedImage target) {
        prepareFor(target);
        if (!(needAbsorption || needEmission)) {
            raster.setPixels(0, 0, width, height, before);
            return result;
        }
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double delta;
        for (int x = 0; x < nx; x++) {
            for (int y = 0; y < ny; y++) {
                for (int z = 0; z < nz; z++) {
                    double vox = calculateVoxel(x, y, z);
                    if (min > vox) {
                        min = vox;
                    }
                    if (max < vox) {
                        max = vox;
                    }
                }
            }
        }
        delta = (max - min) / (N - 1);

        IndexTranslator t = new IndexTranslator(width, height);
        double dz = 1. / nz;
        List<Double>[] colors = new List[]{rEmission, gEmission, bEmission};
        for (int i = 0; i < 3; i++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    double val = before[t.toI(x, y) + i];
                    for (int z = 0; z < nz; z++) {
                        int vox = (int) round((calculateVoxel(x, y, z) - min) / delta);
                        if (needAbsorption) {
                            val = val * exp(-absorption.get(vox) * dz);
                        }
                        if (needEmission) {
                            val += colors[i].get(vox) * dz;
                        }
                    }
                    after[t.toI(x, y) + i] = (val > 255) ? 255 : (val < 0 ? 0 : (int) val);
                }
            }
        }


        raster.setPixels(0, 0, width, height, after);
        return result;
    }

    private <T> List<Double> interpolated(List<Pair<Integer, T>> dots) {
        List<Double> result = new ArrayList<>(N);

        result.add(((Number) dots.get(0).getValue()).doubleValue());
        int i = 0;
        int x = 1;
        while (result.size() < N) {
            Pair<Integer, ?> prev = dots.get(i);
            Pair<Integer, ?> next = dots.get(i + 1);
            if (prev.getKey().equals(next.getKey())) {
                result.set(x - 1, ((Number) next.getValue()).doubleValue());
                i++;
                continue;
            }
            double k = (((Number) next.getValue()).doubleValue() - ((Number) prev.getValue()).doubleValue())
                    / (next.getKey() - prev.getKey());
            double b = ((Number) prev.getValue()).doubleValue();
            for (; x < N; x++) {
                if (x < next.getKey() - 1) {
                    result.add(k * (x - (prev.getKey() - 1)) + b);
                } else {
                    result.add(((Number) next.getValue()).doubleValue());
                    i++;
                    x++;
                    break;
                }
            }
        }
        for (int j = 0; j < 100; j++) {
            System.err.println(j + ", " + result.get(j));
        }
        return result;
    }

    private double calculateVoxel(int x, int y, int z) {
        double val = 0;
        for (Map.Entry<Point3, Double> entry : config.charges.entrySet()) {
            Point3 p = entry.getKey();
            double q = entry.getValue();
            double dx = (x + 0.5) / nx - p.x;
            double dy = (y + 0.5) / ny - p.y;
            double dz = (z + 0.5) / nz - p.z;
            double dist = sqrt((dx * dx) + (dy * dy) + (dz * dz));
            dist = (dist < 0.1) ? 0.1 : dist;
            val += q / dist;
        }
        return val;
    }
}
