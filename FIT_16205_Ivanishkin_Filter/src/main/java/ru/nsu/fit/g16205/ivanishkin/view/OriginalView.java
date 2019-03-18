package ru.nsu.fit.g16205.ivanishkin.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public class OriginalView extends ImageView {
    private boolean selectMode = false;
    private ImageView selectedView;
    private Rectangle rectangle = null;
    private int rectWidth;
    private int rectHeight;

    public OriginalView() {
        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("OriginalView.mouseDragged");
                // todo: drag rectangle not only in its center
                if (selectMode && image != null) {
                    if (rectangle == null) {
                        rectangle = new Rectangle();
                    }
                    int x = min(max(e.getX() - rectWidth / 2, 0), paintSize.width - rectWidth);
                    int y = min(max(e.getY() - rectHeight / 2, 0), paintSize.height - rectHeight);
                    rectangle.setBounds(x, y, rectWidth, rectHeight);

                    selectedView.setImage(
                            image.getSubimage(
                                    (int) min(round(x / scalingFactor), image.getWidth() - SIZE),
                                    (int) min(round(y / scalingFactor), image.getHeight() - SIZE),
                                    SIZE,
                                    SIZE)
                    );
                    selectedView.repaint();
                    repaint();
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("OriginalView.mousePressed");
                repaint();
            }
        };

        addMouseListener(listener);
        addMouseMotionListener(listener);

    }

    @Override
    public void setImage(BufferedImage image) {
        rectangle = null;
        super.setImage(image);
        rectWidth = (int) round(min(SIZE * scalingFactor, paintSize.width));
        rectHeight = (int) round(min(SIZE * scalingFactor, paintSize.height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //todo: draw it by dotted line in xor mode
        if (rectangle != null) {
            g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
    }

    public boolean isSelectMode() {
        return selectMode;
    }

    public void setSelectMode(boolean selectMode, ImageView selectedView) {
        this.selectMode = selectMode;
        if (selectMode && selectedView == null) {
            throw new IllegalArgumentException("If selectMode is true, selectedView should not be null");
        }
        this.selectedView = selectedView;
    }
}
