package ru.nsu.fit.g16205.ivanishkin.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class StatusBar extends JPanel {
    private static class AngledLinesWindowsCornerIcon implements Icon {
        private static final Color WHITE_LINE_COLOR = new Color(255, 255, 255);

        private static final Color GRAY_LINE_COLOR = new Color(172, 168, 153);
        private static final int WIDTH = 13;

        private static final int HEIGHT = 13;

        public int getIconHeight() {
            return WIDTH;
        }

        public int getIconWidth() {
            return HEIGHT;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {

            g.setColor(WHITE_LINE_COLOR);
            g.drawLine(0, 12, 12, 0);
            g.drawLine(5, 12, 12, 5);
            g.drawLine(10, 12, 12, 10);

            g.setColor(GRAY_LINE_COLOR);
            g.drawLine(1, 12, 12, 1);
            g.drawLine(2, 12, 12, 2);
            g.drawLine(3, 12, 12, 3);

            g.drawLine(6, 12, 12, 6);
            g.drawLine(7, 12, 12, 7);
            g.drawLine(8, 12, 12, 8);

            g.drawLine(11, 12, 12, 11);
            g.drawLine(12, 12, 12, 12);

        }
    }


    public StatusBar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 23));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel(new AngledLinesWindowsCornerIcon()), BorderLayout.SOUTH);
        rightPanel.setOpaque(false);

        add(rightPanel, BorderLayout.EAST);
        setBackground(SystemColor.control);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JOptionPane.showMessageDialog(StatusBar.this,
                        "Clicked on " + e.getX() + "," + e.getY());
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int y = 0;
        g.setColor(new Color(156, 154, 140));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(196, 194, 183));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(218, 215, 201));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(233, 231, 217));
        g.drawLine(0, y, getWidth(), y);

        y = getHeight() - 3;
        g.setColor(new Color(233, 232, 218));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(233, 231, 216));
        g.drawLine(0, y, getWidth(), y);
        y = getHeight() - 1;
        g.setColor(new Color(221, 221, 220));
        g.drawLine(0, y, getWidth(), y);

    }

}
