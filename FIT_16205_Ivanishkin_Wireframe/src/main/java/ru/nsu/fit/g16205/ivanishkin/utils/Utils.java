package ru.nsu.fit.g16205.ivanishkin.utils;

import java.util.Arrays;

public abstract class Utils {
    public static double[] reversed(double[] array) {
        double[] res = new double[array.length];
        for (int i = 0; i < res.length / 2; i++) {
            res[i] = array[res.length - i - 1];
            res[res.length - i - 1] = array[i];
        }
        if (array.length % 2 != 0) {
            res[array.length / 2 + 1] = array[array.length / 2 + 1];
        }
        return res;
    }
}
