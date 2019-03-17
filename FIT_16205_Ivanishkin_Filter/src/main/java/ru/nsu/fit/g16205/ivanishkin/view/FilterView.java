package ru.nsu.fit.g16205.ivanishkin.view;


import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

/**
 * Component which draws game field and reacts on mouse clicks
 *
 * @author Dmitry Ivanishkin
 */
public class FilterView extends JPanel {
    private static final int PADDING = 20;
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    private BufferedImage original;
    private BufferedImage before;
    private BufferedImage after;


    /**
     * Constructs object
     */
    public FilterView() {
        setAlignmentX(CENTER_ALIGNMENT);
        setBorder(new BevelBorder(BevelBorder.RAISED));
        MouseAdapter listener = new MouseAdapter() {
            //todo
        };

        addMouseListener(listener);
        addMouseMotionListener(listener);

        BoxLayout mgr = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(mgr);
        add(Box.createHorizontalGlue());
        OriginalView orig = new OriginalView();
        add(orig);
        add(Box.createRigidArea(new Dimension(PADDING,0)));
        SelectedView bef = new SelectedView();
        add(bef);
        add(Box.createRigidArea(new Dimension(PADDING,0)));
        SelectedView aft = new SelectedView();
        add(aft);
        add(Box.createHorizontalGlue());

    }


    /**
     * Performs actual component drawing
     *
     * @param g Graphics object to draw component to
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

    }
}