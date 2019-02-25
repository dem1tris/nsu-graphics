package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.fit.g16205.ivanishkin.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Math.*;
import static ru.nsu.fit.g16205.ivanishkin.DrawingUtils.spanFill;

/**
 * Component which draws two diagonal lines and reacts on mouse clicks
 *
 * @author Dmitry Ivanishkin
 */
public class LifeView extends JPanel {
    private static final int PADDING = 10;
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private static final int lineStroke = 1;
    private static final double SQRT3 = sqrt(3);

    private BufferedImage image;
    private int widthM = 15;
    private int heightN = 15;

    private ArrayList<ArrayList<Hex>> hexes = new ArrayList<>(heightN);


    public void updateSize(int k) {
        Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        graphics.setColor(TRANSPARENT);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        Hex.setSize(k);
        initImage();
    }

    private void initImage() {
        image = new BufferedImage(
                2 * PADDING + (int) ceil((widthM + 0.5) * Hex.getHorDistance()) + 1,
                2 * PADDING + (heightN - 1) * Hex.getVertDistance() + Hex.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
    }

    public void refreshField() {
        Graphics2D g = image.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g.setColor(TRANSPARENT);
        g.fillRect(0,0,image.getWidth(), image.getHeight());
        hexes.stream().flatMap(Collection::stream).forEach(Hex::invalidate);
        repaint();
    }

    /**
     * Constructs object
     */
    public LifeView() {
        //todo: check big grid sizes startup troubles
        //todo: edit only visible part of image
        initImage();
        Hex.setShowImpact(true);
        //todo: set proper image size
        Point start = new Point(Hex.getInRadius() + PADDING, Hex.getSize() + PADDING);
        Point spawnCenter = new Point();
        Point inGridCoord = new Point(0, 0);
        for (int i = 0; i < heightN; i++) {
            spawnCenter.move(start.x + (i % 2 == 0 ? 0 : 1) * Hex.getInRadius(), start.y + i * Hex.getVertDistance());
            inGridCoord.move(0, i);
            hexes.add(new ArrayList<>(widthM));
            for (int j = 0; j < widthM; j++) {
                hexes.get(i).add(new Hex(spawnCenter, inGridCoord));
                spawnCenter.translate(Hex.getHorDistance(), 0);
                inGridCoord.translate(1, 0);
            }
            System.err.println("n " + i);
        }


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = Utils.notNullOrElse(getSelectedHexagon(e.getX(), e.getY()), new Point(-1, -1));
                JOptionPane.showMessageDialog(LifeView.this,
                        "Clicked on " + p.x + ", " + p.y + "\nCoords: " + e.getX() + ", " + e.getY());
            }
        });
        this.setLayout(new BorderLayout());
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
                h.paintHex(image);
            }
        }
        g.drawImage(image, 0, 0, null);
    }

    private Point getSelectedHexagon(int x, int y) {
        x -= PADDING;
        y -= PADDING;
        int halfWidth = Hex.getInRadius();
        int gridWidth = Hex.getHorDistance();
        // k and b from `y = kx + b`
        int b = Hex.getSize() / 2;
        double k = 1. * b / halfWidth;
        int gridHeight = b + Hex.getSize();
        // Find the row and column of the box that the point falls in.
        int row = y / gridHeight;
        int column;

        boolean rowIsOdd = row % 2 == 1;

        // Is the row has an odd number?
        if (rowIsOdd) { // Yes: Offset x to match the indent of the row
            column = (x - halfWidth) / gridWidth;
        } else { // No: Calculate normally
            column = x / gridWidth;
        }

        // Work out the position of the point relative to the box it is in
        double inBoxY = y - row * gridHeight;
        double inBoxX;

        if (rowIsOdd) {
            inBoxX = x - column * gridWidth - halfWidth;
        } else {
            inBoxX = x - column * gridWidth;
        }

        // Work out if the point is above either of the hexagon's top edges
        if (inBoxY < (-k * inBoxX) + b) { // LEFT edge
            row--;
            if (!rowIsOdd)
                column--;
        } else if (inBoxY < (k * inBoxX) - b) { // RIGHT edge
            row--;
            if (rowIsOdd)
                column++;
        }

        try {
            return hexes.get(row).get(column).getPlaceInGrid();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
