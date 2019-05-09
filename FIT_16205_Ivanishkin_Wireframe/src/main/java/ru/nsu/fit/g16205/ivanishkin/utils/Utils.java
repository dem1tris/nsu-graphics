package ru.nsu.fit.g16205.ivanishkin.utils;

import ru.nsu.fit.g16205.ivanishkin.geom.PointTranslator;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

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

    public static void clearImg(BufferedImage img, Color color) {
        Graphics2D g = img.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g.setColor(color);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    public static void clearImg(BufferedImage img) {
        clearImg(img, Color.BLACK);
    }

    public static void drawSegmented(Graphics2D g, java.util.List<Point2D> points, PointTranslator t) {
        List<Point2D> translated;
        if (t != null) {
            translated = points.parallelStream().map(t::translate).collect(Collectors.toList());
        } else {
            translated = points;
        }
        for (int i = 0; i < translated.size() - 1; i++) {
            Point2D p = translated.get(i);
            Point2D pp = translated.get(i + 1);
            g.drawLine(
                    (int) (p.getX()), (int) (p.getY()),
                    (int) (pp.getX()), (int) (pp.getY()));
        }
    }
}
