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
    private int widthM = 200;
    private int heightN = 200;
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
        Hex.setSize(20);
        Point spawn = new Point(Hex.getSize() + PADDING, Hex.getSize() + PADDING);
        for (int i = 0; i < heightN; i++) {
            hexes.add(new ArrayList<>(widthM));
            for (int j = 0; j < widthM; j++) {
                hexes.get(i).add(new Hex(spawn));
                spawn.translate(Hex.getHorDistance(), 0);
            }
            spawn.translate(-widthM * Hex.getHorDistance() + (i % 2 == 0 ? 1 : -1) * Hex.getWidth() / 2,
                    Hex.getVertDistance());
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

    private void paintVerticals(Graphics g) {
        int startX = 0;
        int endX = startX;
        int startY;
        int endY;

        for (int i = 0; i < widthM + 1; ++i) {
            startY = bigRadius / 2;
            endY = startY + bigRadius;
            for (int j = 0; j < (heightN + 1) / 2; ++j) {
                drawLine(g, startX, startY, endX, endY);
                startY = endY + 2 * bigRadius;
                endY = startY + bigRadius;
            }
            startX += 2 * smallRadius;
            endX = startX;
        }

        startX = smallRadius;
        endX = startX;
        for (int i = 0; i < widthM; ++i) {
            startY = bigRadius * 2;
            endY = startY + bigRadius;
            for (int j = 0; j < heightN / 2; ++j) {
                drawLine(g, startX, startY, endX, endY);
                startY = endY + 2 * bigRadius;
                endY = startY + bigRadius;
            }
            startX += 2 * smallRadius;
            endX = startX;
        }
    }

    private void paintSlants(Graphics g) {
        int fromX = smallRadius;
        int fromY = 0;

        paintSlantsFrom(g, smallRadius, 0, true, false); // "\"
        paintSlantsFrom(g, 0, bigRadius * 3 / 2, false, false);
        paintSlantsFrom(g, smallRadius, 0, true, true); // "/"
        paintSlantsFrom(g, 0, bigRadius * 3 / 2, false, true);
    }

    private void paintSlantsFrom(Graphics g, int fromX, int fromY, boolean outer, boolean mirrored) {
        if (!outer && !mirrored) {
            g.setColor(Color.BLUE);
        } else if (outer && !mirrored) {
            g.setColor(Color.RED); //лишний
        } else if (!outer && mirrored) {
            g.setColor(Color.WHITE); //не хватает
        } else {
            g.setColor(Color.BLACK); //лишний
        }
        int startX = fromX;
        int endX = startX + smallRadius * (mirrored ? -1 : 1);
        int startY;
        int endY;

        for (int i = 0; i < widthM; ++i) {
            startY = fromY;
            endY = startY + bigRadius / 2;
            for (int j = 0; j < heightN / 2 + (outer ? 1 : 0); ++j) {
                drawLine(g, startX, startY, endX, endY);
                startY = startY + 3 * bigRadius;
                endY = startY + bigRadius / 2;
            }
            startX += 2 * smallRadius;
            endX = startX + smallRadius * (mirrored ? -1 : 1);
        }
    }

    /**
     * Performs actual component drawing
     *
     * @param g Graphics object to draw component to
     */
    @Override
    protected void paintComponent(final Graphics g) {
        //((Graphics2D) g).setStroke(new BasicStroke(4));
        super.paintComponent(g);
        for (ArrayList<Hex> list : hexes) {
            for (Hex h : list) {
                h.paint(g);
            }
        }
//        paintVerticals(g);
//        paintSlants(g);
//        g.drawLine(0, 0, getBounds().width - 1, getBounds().height - 1);
//        drawLine(g, 10, 0, getBounds().width - 1, getBounds().height - 1);
//        g.drawLine(0, getBounds().height - 1, getBounds().width - 1, 0);
//        drawLine(g, 10, getBounds().height - 1, getBounds().width - 1, 0);
    }
}
