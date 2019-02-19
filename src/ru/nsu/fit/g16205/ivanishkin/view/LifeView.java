package ru.nsu.fit.g16205.ivanishkin.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static java.lang.Math.*;
import static ru.nsu.fit.g16205.ivanishkin.DrawingUtils.drawLine;

/**
 * Component which draws two diagonal lines and reacts on mouse clicks
 *
 * @author Dmitry Ivanishkin
 */
public class LifeView extends JPanel {
    private static final int PADDING = 10;

    private static final double SQRT3 = sqrt(3);
    private int widthM = 4;
    private int heightN = 4;
    private int bigRadius;
    private int smallRadius;

    private ArrayList<ArrayList<Hex>> hexes = new ArrayList<>(heightN);


    public void updateSize(int k) {
        Hex.setSize(k);
    }

    /**
     * Constructs object
     */
    public LifeView() {
        Hex.setSize(30);
        Point start = new Point(Hex.getSize() + PADDING, Hex.getSize() + PADDING);
        Point spawn = new Point();
        Point inGrid = new Point(0, 0);
        for (int i = 0; i < heightN; i++) {
            spawn.move(start.x + (i % 2 == 0 ? 0 : 1) * Hex.getWidth() / 2, start.y + i * Hex.getVertDistance());
            inGrid.move(0, i);
            hexes.add(new ArrayList<>(widthM));
            for (int j = 0; j < widthM; j++) {
                hexes.get(i).add(new Hex(spawn, inGrid));
                spawn.translate(Hex.getHorDistance(), 0);
                inGrid.translate(1, 0);
            }
            System.err.println('n');
        }


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JOptionPane.showMessageDialog(LifeView.this,
                        "Clicked on " + e.getX() + "," + e.getY());
                SwingUtilities.invokeLater(() -> repaint());
            }
        });
        this.setLayout(new BorderLayout());
        this.add(new JScrollBar(Adjustable.VERTICAL, 10, 1, 0, 100), BorderLayout.EAST);
        add(new StatusBar(), BorderLayout.SOUTH);


    }

    /**
     * Performs actual component drawing
     *
     * @param g Graphics object to draw component to
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        for (ArrayList<Hex> list : hexes) {
            for (Hex h : list) {
                h.paintHex(g);
            }
        }
    }
}
