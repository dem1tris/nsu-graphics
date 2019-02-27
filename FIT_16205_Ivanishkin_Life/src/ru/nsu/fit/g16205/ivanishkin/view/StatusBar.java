package ru.nsu.fit.g16205.ivanishkin.view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StatusBar extends JPanel {
    private JLabel message = new JLabel();

    public StatusBar() {
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        message.setHorizontalAlignment(SwingConstants.LEFT);
        add(message);
    }

    public void setMessage(String text) {
        message.setText(text);
        message.repaint();
        repaint();
    }

}
