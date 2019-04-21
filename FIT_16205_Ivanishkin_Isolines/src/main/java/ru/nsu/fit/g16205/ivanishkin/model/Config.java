package ru.nsu.fit.g16205.ivanishkin.model;

import java.awt.*;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Config {
    public static final Config DEFAULT = new Config(
            40, 40,
            -5., 5.,
            -5., 5.,
            Arrays.asList(
                    Color.BLUE,
                    new Color(0, 220, 255),
                    new Color(0, 255, 200),
                    Color.GREEN,
                    Color.YELLOW,
                    new Color(255, 200, 0),
                    Color.RED),
            Color.GRAY);

    public final int gridWidth;
    public final int gridHeight;
    public final double x0;
    public final double xEnd;
    public final double y0;
    public final double yEnd;
    public final List<Color> legendColors;
    public final Color isolinesColor;

    public Config(int gridWidth, int gridHeight, double x0, double xEnd, double y0, double yEnd,
                  List<Color> legendColors, Color isolinesColor) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.x0 = x0;
        this.xEnd = xEnd;
        this.y0 = y0;
        this.yEnd = yEnd;
        this.legendColors = legendColors;
        this.isolinesColor = isolinesColor;
    }

    public static Config from(final Reader reader) {
        BufferedReader r = new BufferedReader(reader);

        try {
            List<String> lines = r.lines()
                    .map(it -> it.substring(0, it.contains("//") ? it.indexOf("//") : it.length()))
                    .map(String::trim)
                    .filter(it -> !it.isEmpty())
                    .collect(Collectors.toList());

            String[] gridSizeStrings = lines.get(0).split(" ");
            lines = lines.subList(1, lines.size());

            List<String[]> colorsStrings = readCountGetSplitedStrings(lines, 2);
            lines = lines.subList(colorsStrings.size() + 1, lines.size());

            if (lines.size() != 0) {
                throw new IllegalArgumentException("Illegal number of colors");
            }

            List<Color> colors = new ArrayList<>(colorsStrings.size());

            int width = Integer.parseInt(gridSizeStrings[0]);
            int height = Integer.parseInt(gridSizeStrings[1]);

            for (String[] c : colorsStrings) {
                colors.add(new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2])));
            }

            return new ConfigBuilder(DEFAULT)
                    .setGridWidth(width)
                    .setGridHeight(height)
                    .setLegendColors(colors.subList(0, colors.size() - 1))
                    .setIsolinesColor(colors.get(colors.size() - 1))
                    .createConfig();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while reading config", e);
        }
    }

    private static List<String[]> readCountGetSplitedStrings(List<String> lines, int deltaCount) {
        int limit = Integer.parseInt(lines.get(0)) + deltaCount;
        lines = lines.subList(1, lines.size());
        return lines.stream()
                .limit(limit)
                .map(it -> it.split(" "))
                .collect(Collectors.toList());
    }
}