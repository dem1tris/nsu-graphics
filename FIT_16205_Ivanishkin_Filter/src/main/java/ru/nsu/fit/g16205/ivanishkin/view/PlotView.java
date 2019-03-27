package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.fit.g16205.ivanishkin.utils.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class PlotView extends JPanel {
    protected static final int PANEL_WIDTH = 537;
    protected static final int PANEL_HEIGHT = 270;
    protected static final int HEIGHT = 250;
    protected static final int WIDTH = 535;
    protected static final int X = 2;
    protected static final int Y = 2;

    protected BufferedImage image;
    protected List<List<Pair<Integer, Integer>>> plots = new ArrayList<>();
    protected List<Color> colors = new ArrayList<>();

    public PlotView() {
        setBorder(BorderFactory.createDashedBorder(Color.BLACK));
        Dimension preferredSize = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
        setPreferredSize(preferredSize);
        setMinimumSize(preferredSize);
        setMaximumSize(preferredSize);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.err.println(e.getX() + " " + e.getY());
            }
        });
    }

    public <V> void addPlot(final List<Pair<Integer, V>> dots, Color color) {
        List<Pair<Integer, Integer>> scaled = new ArrayList<>();
        if (dots.iterator().next().getValue() instanceof Double) {
            dots.forEach(it -> {
                System.err.println(it.getKey() + " " + it.getValue());
                scaled.add(new Pair<>(it.getKey() * WIDTH / 100, (int) round((Double) it.getValue() * HEIGHT)));
            });
        } else if (dots.iterator().next().getValue() instanceof Integer) {
            dots.forEach(it -> scaled.add(new Pair<>(it.getKey() * WIDTH / 100, (Integer) it.getValue())));
        } else {
            throw new IllegalArgumentException("Illegal value type");
        }
        plots.add(scaled);
        colors.add(color);
        repaint();
    }

    public void clear() {
        plots.clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int offset = 2;
        int color = 0;
        for (List<Pair<Integer, Integer>> plot : plots) {
            g.setColor(colors.get(color));
            System.err.println(colors.get(color));
            for (int i = 0; i < plot.size() - 1; i++) {
                System.err.println(offset + plot.get(i).getKey() + ", " + (PANEL_HEIGHT - (offset + plot.get(i).getValue())));
                System.err.println(offset + plot.get(i + 1).getKey() + ", " + (PANEL_HEIGHT - (offset + plot.get(i + 1).getValue())));
                System.err.println();
                g.drawLine(offset + plot.get(i).getKey(), PANEL_HEIGHT - (offset + plot.get(i).getValue()),
                        offset + plot.get(i + 1).getKey(), PANEL_HEIGHT - (offset + plot.get(i + 1).getValue()));
            }
            offset += 3;
            color++;
        }
    }
}
