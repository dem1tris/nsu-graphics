package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.fit.g16205.ivanishkin.model.Config;
import ru.nsu.fit.g16205.ivanishkin.model.GridFunction;
import ru.nsu.fit.g16205.ivanishkin.model.Legend;
import ru.nsu.fit.g16205.ivanishkin.model.Segment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class MapView extends JPanel {
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private static final int PADDING = 10;
    private static final int LEGEND_WIDTH = 200;

    private Config config;
    private GridFunction fun;
    private Legend legend;
    private Color isolinesColor;
    private int maxWidth = 2000;
    private int maxHeight = 2000;
    private int width;
    private int height;

    // deltas per pixel
    private double pixelDeltaX;
    private double pixelDeltaY;
    private double pixelDeltaZ;

    private double cellDeltaPixelX;
    private double cellDeltaPixelY;

    private BufferedImage map = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_4BYTE_ABGR);
    private BufferedImage legendImg = new BufferedImage(LEGEND_WIDTH, maxHeight, BufferedImage.TYPE_3BYTE_BGR);
    private BufferedImage lines = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_4BYTE_ABGR);

    private List<List<Segment>> isolines = new ArrayList<>();
    private List<Segment> temporaryIsoline = new ArrayList<>();
    private boolean isMapValid = false;
    private boolean showGrid = false;
    private boolean showIsolines = false;
    private boolean interpolationEnabled = false;
    private boolean showPoints = false;

    public void reconfigure(Config config) {
        this.config = config;
        double x0 = config.x0;
        double y0 = config.y0;
        double xEnd = config.xEnd;
        double yEnd = config.yEnd;
        int nx = config.gridWidth;
        int ny = config.gridHeight;
        isolinesColor = config.isolinesColor;

        fun = new GridFunction((x, y) -> sin(y) * cos(x), x0, y0, xEnd, yEnd, nx, ny);
        onContainerSizeChanged();
        legend = new Legend(fun.min(), fun.max(), config.legendColors);
        temporaryIsoline.clear();
        isolines.clear();
        legend.getLevels().forEach(lvl -> isolines.add(fun.getIsoline(lvl)));

        isMapValid = false;
        repaint();
    }

    private void onContainerSizeChanged() {
        width = getWidth() - LEGEND_WIDTH - PADDING;
        height = getHeight();
        pixelDeltaX = (fun.xEnd() - fun.x0()) / (width - 1);
        pixelDeltaY = (fun.yEnd() - fun.y0()) / (height - 1);
        pixelDeltaZ = (fun.max() - fun.min()) / (height - 1);
        cellDeltaPixelX = 1. * (width - 1) / (fun.nX() - 1);
        cellDeltaPixelY = 1. * (height - 1) / (fun.nY() - 1);
        isMapValid = false;
    }

    public void clearCustomIsolines() {
        isolines.retainAll(isolines.subList(0, legend.getLevelsCount()));
        temporaryIsoline.clear();
        repaint();
    }

    public void addIsoline(int x, int y) {
        if (showIsolines && x >= 0 && x < width && y >= 0 && y < height) {
            double level = fun.at(pixelToDouble(x, y));
            isolines.add(fun.getIsoline(level));
        }
    }

    public void showTemporaryIsoline(int x, int y) {
        if (showIsolines && x >= 0 && x < width && y >= 0 && y < height) {
            temporaryIsoline = fun.getIsoline(fun.at(pixelToDouble(x, height - 1 - y)));
            repaint();
        }
    }

    public MapView(final Config config) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
//                System.out.println("mouseClicked " + e.getX() + " " + e.getY());
                addIsoline(e.getX(), e.getY());
                repaint();
            }
        });
        reconfigure(config);
    }

    //todo: cleanup, proper formatting
    public String statusBarMessage(int x, int y) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            double xx = fun.x0() + x * pixelDeltaX;
            double yy = fun.yEnd() - y * pixelDeltaY;
            Color c = new Color(map.getRGB(x, y));
            return "xi = " + (int) (x / cellDeltaPixelX) +
                    ", yj = " + (int) (y / cellDeltaPixelY) +
                    ", x = " + String.format("%.3f", xx) +
                    ", y = " + String.format("%.3f", yy) +
                    ", f(x, y) = " + String.format("%.3f", fun.at(xx, yy)) +
                    ", color = (" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ")";
        } else {
            return "";
        }
    }

    public boolean isInterpolationEnabled() {
        return interpolationEnabled;
    }

    public void setInterpolationEnabled(boolean enabled) {
        if (interpolationEnabled != enabled) {
            isMapValid = false;
            this.interpolationEnabled = enabled;
            repaint();
        }
    }

    public boolean getShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean val) {
        if (showGrid != val) {
            this.showGrid = val;
            repaint();
        }
    }

    public boolean getShowPoints() {
        return showPoints;
    }

    public void setShowPoints(boolean val) {
        if (showPoints != val) {
            this.showPoints = val;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (width != getWidth() - LEGEND_WIDTH || height != getHeight()) {
            onContainerSizeChanged();
        }
        super.paintComponent(g);
        if (!isMapValid) {
            repaintMap();
            repaintLegend();
        }
        clearImg(lines);
        if (showGrid) {
            drawGrid();
        }
        if (showIsolines) {
            drawIsolines();
        }
        if (showPoints) {
            drawPoints();
        }

        g.drawImage(map, 0, 0, this);
        g.drawImage(lines, 0, 0, this);
        g.drawImage(legendImg, width + PADDING, 0, this);
    }

    private void drawPoints() {
        Graphics2D g = lines.createGraphics();
        g.setColor(Color.DARK_GRAY);
        isolines.add(temporaryIsoline);
        for (List<Segment> segments : isolines) {
            segments.forEach(s -> {
                Point start = doubleToPixel(s.start());
                Point end = doubleToPixel(s.end());
                g.fillRect(start.x - 1, start.y - 1, 3, 3);
                g.fillRect(end.x - 1, end.y - 1, 3, 3);
            });
        }
        isolines.remove(temporaryIsoline);
    }

    private void drawSegmented(Graphics g, List<Segment> segments) {
        g.setColor(isolinesColor);
        segments.forEach(s -> {
            Point start = doubleToPixel(s.start());
            Point end = doubleToPixel(s.end());
            g.drawLine(start.x, start.y, end.x, end.y);
        });
    }

    private void drawIsoline(Graphics g, double level) {
        g.setColor(isolinesColor);
        drawSegmented(g, fun.getIsoline(level));
    }

    private Point doubleToPixel(Point2D p) {
        return new Point(
                (int) round((p.getX() - fun.x0()) / pixelDeltaX),
                (int) round((fun.yEnd() - p.getY()) / pixelDeltaY)
        );
    }

    private Point2D pixelToDouble(int x, int y) {
        return new Point2D.Double(
                fun.x0() + x * pixelDeltaX,
                fun.y0() + y * pixelDeltaY
        );
    }

    private Point2D pixelToDouble(Point p) {
        return pixelToDouble(p.x, p.y);
    }

    public boolean getShowIsolines() {
        return showIsolines;
    }

    public void setShowIsolines(boolean value) {
        if (showIsolines != value) {
            this.showIsolines = value;
            repaint();
        }
    }

    private void clearImg(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g.setColor(TRANSPARENT);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    private void drawGrid() {
        Graphics2D g = lines.createGraphics();
        double coord = 0;
        for (int i = 0; i < fun.nX(); i++) {
            int pixel = (int) Math.round(coord);
            g.drawLine(pixel, 0, pixel, height - 1);
            coord += cellDeltaPixelX;
        }

        coord = 0;
        for (int i = 0; i < fun.nY(); i++) {
            int pixel = (int) Math.round(coord);
            g.drawLine(0, pixel, width - 1, pixel);
            coord += cellDeltaPixelY;
        }
    }

    private void drawIsolines() {
        Graphics2D g = lines.createGraphics();
        isolines.forEach(line -> drawSegmented(g, line));
        drawSegmented(g, temporaryIsoline);
    }

    private void repaintMap() {
        System.out.println("MapView.repaintMap");
        clearImg(map);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (interpolationEnabled) {
                    map.setRGB(j, i, legend.interpolatedAt(
                            fun.at(fun.x0() + j * pixelDeltaX, fun.yEnd() - i * pixelDeltaY)).getRGB());
                } else {
                    map.setRGB(j, i, legend.colorAt(
                            fun.at(fun.x0() + j * pixelDeltaX, fun.yEnd() - i * pixelDeltaY)).getRGB());
                }
            }
        }
        isMapValid = true;
    }

    private void repaintLegend() {
        clearImg(legendImg);
        Graphics2D g = legendImg.createGraphics();
        List<Double> levels = legend.getLevels().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        int[] indices = levels.stream()
                .mapToInt(lvl -> (int) round((fun.max() - lvl) / pixelDeltaZ))
                .toArray();
        for (int i = 0, j = 0; i < height && j < indices.length; i++) {
            Font font = g.getFont().deriveFont(16.f);
            g.setFont(font);
            if (interpolationEnabled) {
                g.setColor(legend.interpolatedAt(fun.max() - i * pixelDeltaZ));
            } else {
                g.setColor(legend.colorAt(fun.max() - i * pixelDeltaZ));
            }
            g.drawLine(0, i, LEGEND_WIDTH / 2, i);
            if (i == indices[j]) {
                g.drawLine(LEGEND_WIDTH / 2, i, LEGEND_WIDTH, i);
                int height = i == 0 ? font.getSize() : i - 3;
                g.drawString(String.format("%+.03f", levels.get(j)), LEGEND_WIDTH / 2 + 1, height);
                j++;
            }
        }
    }

    public Config getConfig() {
        return config;
    }
}
