package ru.nsu.fit.g16205.ivanishkin.view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.round;

public class ImageView extends JPanel {
    protected static final int SIZE = 350;

    protected BufferedImage image;
    protected Dimension paintSize = new Dimension(0, 0);
    protected double scalingFactor = 1;

    ImageView() {
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        Dimension preferredSize = new Dimension(SIZE, SIZE);
        setPreferredSize(preferredSize);
        setMinimumSize(preferredSize);
        setMaximumSize(preferredSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, paintSize.width, paintSize.height, this);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();
            if (width > SIZE || height > SIZE) {
                scalingFactor = 1. * SIZE / Math.max(width, height);
                paintSize = new Dimension((int) round(width * scalingFactor), (int) round(height * scalingFactor));
            } else {
                paintSize = new Dimension(width, height);
            }
        }

        repaint();
    }
}
