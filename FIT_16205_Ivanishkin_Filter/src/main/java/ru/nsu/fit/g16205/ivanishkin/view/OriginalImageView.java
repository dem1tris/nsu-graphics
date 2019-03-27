package ru.nsu.fit.g16205.ivanishkin.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public class OriginalImageView extends ImageView {
    private static Stroke DASHED_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 1.0f, new float[]{2f}, 0.0f);

    private boolean selectMode = false;
    private ImageView selectedView;
    private Rectangle rectangle = null;
    private int rectWidth;
    private int rectHeight;

    public OriginalImageView() {
        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
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
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (selectedView != null && selectedView.image != null) {
                    BufferedImage subimage = selectedView.image;
                    BufferedImage copied = new BufferedImage(
                            subimage.getColorModel(),
                            subimage.getRaster().createCompatibleWritableRaster(SIZE, SIZE),
                            subimage.isAlphaPremultiplied(),
                            null
                    );
                    subimage.copyData(copied.getRaster());
                    selectedView.setImage(copied);
                }
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
        if (rectangle != null) {
            Graphics2D gg = (Graphics2D) g;
            gg.setXORMode(Color.WHITE);
            Stroke saved = gg.getStroke();
            gg.setStroke(DASHED_STROKE);
            gg.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            gg.setPaintMode();
            gg.setStroke(saved);
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
