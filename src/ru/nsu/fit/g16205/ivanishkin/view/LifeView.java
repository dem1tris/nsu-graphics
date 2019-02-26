package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.fit.g16205.ivanishkin.Utils;
import ru.nsu.fit.g16205.ivanishkin.model.Cell;
import ru.nsu.fit.g16205.ivanishkin.model.LifeModel;
import ru.nsu.fit.g16205.ivanishkin.observer.Observable;
import ru.nsu.fit.g16205.ivanishkin.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static java.lang.Math.ceil;

/**
 * Component which draws game field and reacts on mouse clicks
 *
 * @author Dmitry Ivanishkin
 */
public class LifeView extends JPanel implements Observer {
    private static final int PADDING = 10;
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private BasicStroke lineStroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private int size = Hex.getSize();
    private LifeModel model;
    private boolean xorClickMode = true;
    private BufferedImage gridImage;
    private BufferedImage impactImage;
    private int widthM;
    private int heightN;

    private ArrayList<ArrayList<Hex>> hexes = new ArrayList<>(heightN);


    private Hex getHex(Point p) {
        return hexes.get(p.y).get(p.x);
    }

    public boolean isXorClickMode() {
        return xorClickMode;
    }

    public void setXorClickMode(boolean xorClickMode) {
        this.xorClickMode = xorClickMode;
    }

    public int getWidthM() {
        return widthM;
    }

    public void setWidthM(int widthM) {
        this.widthM = widthM;
    }

    public int getHeightN() {
        return heightN;
    }

    public void setHeightN(int heightN) {
        this.heightN = heightN;
    }

    public int getLineStroke() {
        return (int) lineStroke.getLineWidth();
    }

    public void setLineStroke(int width) {
        this.lineStroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    public int getCellSize() {
        return Hex.getSize();
    }

    public void updateCellSize(int k) {
        Hex.setSize(k);
        initImage();
    }

    private void initImage() {
        int width = 2 * PADDING + (int) ceil((widthM + 0.5) * Hex.getHorDistance()) + 1;
        int height = 2 * PADDING + (heightN - 1) * Hex.getVertDistance() + Hex.getHeight();
        gridImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        impactImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        setPreferredSize(new Dimension(width, height));
    }

    public void refreshField() {
        Graphics2D g = gridImage.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g.setColor(TRANSPARENT);
        g.fillRect(0, 0, gridImage.getWidth(), gridImage.getHeight());

        g = impactImage.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g.setColor(TRANSPARENT);
        g.fillRect(0, 0, impactImage.getWidth(), impactImage.getHeight());

        hexes.stream().flatMap(Collection::stream).forEach(Hex::invalidate);
        repaint();
    }

    /**
     * Constructs object
     */
    public LifeView(LifeModel model) {
        System.out.println(getBackground());
        this.model = model;
        model.register(this);
        widthM = model.getWidthM();
        heightN = model.getHeightN();
        //todo: check big grid sizes startup troubles
        //todo: edit only visible part of image
        initImage();
        Hex.setShowImpact(true);
        Point start = new Point(Hex.getInRadius() + PADDING, Hex.getSize() + PADDING);
        Point spawnCenter = new Point();
        Point inGridCoord = new Point(0, 0);
        for (int i = 0; i < heightN; i++) {
            spawnCenter.move(start.x + (i % 2 == 0 ? 0 : 1) * Hex.getInRadius(), start.y + i * Hex.getVertDistance());
            inGridCoord.move(0, i);
            hexes.add(new ArrayList<>(widthM));
            for (int j = 0; j < widthM; j++) {
                hexes.get(i).add(new Hex(spawnCenter, model.getCell(inGridCoord)));
                spawnCenter.translate(Hex.getHorDistance(), 0);
                inGridCoord.translate(1, 0);
            }
        }

        //todo: double-click in Replace mode
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point clicked = getSelectedHexagon(e.getX(), e.getY());
                if (Objects.nonNull(clicked)) {
                    if (xorClickMode) {
                        model.changeAlive(clicked);
                    } else {
                        System.err.println(clicked);
                        model.setAlive(clicked, true);
                    }
                }
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
                h.paintHex(gridImage, impactImage, lineStroke);
            }
        }
        //todo: this or null?
        g.drawImage(gridImage, 0, 0, this);
        g.drawImage(impactImage, 0, 0, this);
    }

    private Point getSelectedHexagon(int x, int y) {
        try {
            if (gridImage.getRGB(x, y) == Color.BLACK.getRGB()) {
                return null;
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

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

    @Override
    public void dispatchUpdates(Observable o, Object[] updated) {
        Arrays.stream(updated)
                .map(it -> (Cell) it)
                .forEach(it -> getHex(it.place()).invalidate());
        repaint();
    }
}
