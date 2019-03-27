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

    private OriginalImageView original = new OriginalImageView();
    private ImageView selected = new ImageView();
    private ImageView filtered = new ImageView();
    private PlotView absorptionPlot = new PlotView();
    private PlotView emissionPlot = new PlotView();


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

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel images = new JPanel();
        images.setLayout(new BoxLayout(images, BoxLayout.X_AXIS));
        images.add(Box.createHorizontalGlue());
        images.add(original);
        images.add(Box.createRigidArea(new Dimension(PADDING, 0)));
        images.add(selected);
        images.add(Box.createRigidArea(new Dimension(PADDING, 0)));
        images.add(filtered);
        images.add(Box.createHorizontalGlue());

        JPanel plots = new JPanel();
        plots.setLayout(new BoxLayout(plots, BoxLayout.X_AXIS));
        plots.add(Box.createHorizontalGlue());
        plots.add(absorptionPlot);
        plots.add(Box.createRigidArea(new Dimension(PADDING, 0)));
        plots.add(emissionPlot);
        plots.add(Box.createHorizontalGlue());

        add(Box.createVerticalGlue());
        add(images);
        add(Box.createRigidArea(new Dimension(0, PADDING)));
        add(plots);
        add(Box.createVerticalGlue());

    }

    public void setImage(final BufferedImage img) {
        original.setImage(img);
    }

    public void clear() {
        original.setImage(null);
        selected.setImage(null);
        filtered.setImage(null);
    }

    public OriginalImageView getOriginal() {
        return original;
    }

    public ImageView getSelected() {
        return selected;
    }

    public ImageView getFiltered() {
        return filtered;
    }

    public PlotView getAbsorprion() {
        return absorptionPlot;
    }

    public PlotView getEmission() {
        return emissionPlot;
    }
}