package ru.nsu.fit.g16205.ivanishkin.model;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Config {
    public final int width;
    public final int height;
    public final int border;
    public final int cell;
    public final int count;
    private final List<Point> alive;

    public Config(int width, int height, int border, int cell, int count) {
        this(width, height, border, cell, count, new ArrayList<>());
    }

    public Config(int width, int height, int border, int cell, int count, List<Point> alive) {
        this.width = width;
        this.height = height;
        this.border = border;
        this.cell = cell;
        this.count = count;
        this.alive = alive;
    }

    public void add(Point p) {
        alive.add(p);
    }

    public Point[] getAlive() {
        return alive.toArray(new Point[0]);
    }

    public void print(Writer writer) {
        PrintWriter w = new PrintWriter(writer);
        w.println(width + " " + height);
        w.println(border);
        w.println(cell);
        w.println(count);
        alive.forEach(it -> w.println(it.x + " " + it.y));
        w.println();
    }

    public static Config parseFile(final File file) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        }
        List<String> lines = reader.lines()
                .map(it -> it.substring(0, it.contains("//") ? it.indexOf("//") : it.length()))
                .map(String::trim)
                .collect(Collectors.toList());

        String[] sizes = lines.get(0).split(" ");


        try {
            Config config = new Config(
                    Integer.parseInt(sizes[0]),
                    Integer.parseInt(sizes[1]),
                    Integer.parseInt(lines.get(1)),
                    Integer.parseInt(lines.get(2)),
                    Integer.parseInt(lines.get(3)));

            for (int i = 0; i < config.count; i++) {
                String[] coords = lines.get(4 + i).split(" ");
                config.add(new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
            }

            return config;
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new RuntimeException("Error while opening file", e);
        }
    }

}
