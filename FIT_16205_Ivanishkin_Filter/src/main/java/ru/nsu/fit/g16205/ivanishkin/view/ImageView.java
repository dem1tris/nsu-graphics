package ru.nsu.fit.g16205.ivanishkin.view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class ImageView extends JPanel {
    ImageView() {
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        Dimension preferredSize = new Dimension(370, 370);
        setPreferredSize(preferredSize);
        setMinimumSize(preferredSize);
        setMaximumSize(preferredSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, 0, 2000, 4000);
    }
}
