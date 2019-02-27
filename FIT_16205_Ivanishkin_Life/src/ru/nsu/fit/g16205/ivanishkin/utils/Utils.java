package ru.nsu.fit.g16205.ivanishkin.utils;

public class Utils {
    public static <T> T notNullOrElse(T obj, T defaultObj) {
        if (obj == null) {
            return defaultObj;
        }
        return obj;
    }
}