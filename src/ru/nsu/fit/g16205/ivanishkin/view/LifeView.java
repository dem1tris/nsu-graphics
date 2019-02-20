package ru.nsu.fit.g16205.ivanishkin.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import static java.lang.Math.sqrt;

/**
 * Component which draws two diagonal lines and reacts on mouse clicks
 *
 * @author Dmitry Ivanishkin
 */
public class LifeView extends JPanel {
    private static final int PADDING = 10;
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private static final double SQRT3 = sqrt(3);

    private BufferedImage image;
    private int widthM = 4;
    private int heightN = 4;

    private ArrayList<ArrayList<Hex>> hexes = new ArrayList<>(heightN);


    public void updateSize(int k) {
        Hex.setSize(k);
    }

    /**
     * Constructs object
     */
    public LifeView() {
        //todo: set proper image size
        image = new BufferedImage(500, 500,
                BufferedImage.TYPE_INT_ARGB);
        Hex.setSize(30);
        Hex.setShowImpact(true);
        Point start = new Point(Hex.getSize() + PADDING, Hex.getSize() + PADDING);
        Point spawn = new Point();
        Point inGrid = new Point(0, 0);
        for (int i = 0; i < heightN; i++) {
            spawn.move(start.x + (i % 2 == 0 ? 0 : 1) * Hex.getInRadius(), start.y + i * Hex.getVertDistance());
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
        Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        graphics.setColor(TRANSPARENT);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        for (ArrayList<Hex> list : hexes) {
            for (Hex h : list) {
                h.paintHex(image);
            }
        }
        g.drawImage(image, 0, 0, null);
    }
}
