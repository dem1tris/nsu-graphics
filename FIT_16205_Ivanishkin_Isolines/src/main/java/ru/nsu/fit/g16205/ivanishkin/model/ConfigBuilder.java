package ru.nsu.fit.g16205.ivanishkin.model;

import java.awt.*;
import java.util.List;

public class ConfigBuilder {
    private int gridWidth;
    private int gridHeight;
    private double x0;
    private double xEnd;
    private double y0;
    private double yEnd;
    private List<Color> legendColors;
    private Color isolinesColor;

    public ConfigBuilder() {}

    public ConfigBuilder(Config config) {
        this.gridWidth = config.gridWidth;
        this.gridHeight = config.gridHeight;
        this.x0 = config.x0;
        this.xEnd = config.xEnd;
        this.y0 = config.y0;
        this.yEnd = config.yEnd;
        this.legendColors = config.legendColors;
        this.isolinesColor = config.isolinesColor;
    }

    public ConfigBuilder setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
        return this;
    }

    public ConfigBuilder setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
        return this;
    }

    public ConfigBuilder setX0(double x0) {
        this.x0 = x0;
        return this;
    }

    public ConfigBuilder setY0(double y0) {
        this.y0 = y0;
        return this;
    }

    public ConfigBuilder setxEnd(double xEnd) {
        this.xEnd = xEnd;
        return this;
    }

    public ConfigBuilder setyEnd(double yEnd) {
        this.yEnd = yEnd;
        return this;
    }

    public ConfigBuilder setLegendColors(List<Color> legendColors) {
        this.legendColors = legendColors;
        return this;
    }

    public ConfigBuilder setIsolinesColor(Color isolinesColor) {
        this.isolinesColor = isolinesColor;
        return this;
    }

    public Config createConfig() {
        return new Config(gridWidth, gridHeight, x0, xEnd, y0, yEnd, legendColors, isolinesColor);
    }
}