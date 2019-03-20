package ru.nsu.fit.g16205.ivanishkin.view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

/**
 * Component which draws game field and reacts on mouse clicks
 *
 * @author Dmitry Ivanishkin
 */
public class MainView extends JPanel {
    private static final int PADDING = 20;
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    private OriginalView original = new OriginalView();
    private ImageView selected = new SelectedView();
    private ImageView filtered = new ImageView();


    /**
     * Constructs object
     */
    public MainView() {
        setAlignmentX(CENTER_ALIGNMENT);
        MouseAdapter listener = new MouseAdapter() {
            //todo
        };

        addMouseListener(listener);
        addMouseMotionListener(listener);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(Box.createHorizontalGlue());
        add(original);
        add(Box.createRigidArea(new Dimension(PADDING, 0)));
        add(selected);
        add(Box.createRigidArea(new Dimension(PADDING, 0)));
        add(filtered);
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

    public void setImage(final BufferedImage img) {
        original.setImage(img);
    }

    public void clear() {
        original.setImage(null);
        selected.setImage(null);
        filtered.setImage(null);
    }

    public OriginalView getOriginal() {
        return original;
    }

    public ImageView getSelected() {
        return selected;
    }

    public ImageView getFiltered() {
        return filtered;
    }
}