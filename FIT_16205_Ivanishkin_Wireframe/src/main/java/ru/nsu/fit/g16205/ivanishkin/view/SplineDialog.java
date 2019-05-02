package ru.nsu.fit.g16205.ivanishkin.view;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

public class SplineDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel splineView;
    private JButton addPivot;
    private JButton removePivot;
    private JButton scalePlus;
    private JButton scaleMinus;
    private JPanel buttonsPanel;
    private JPanel spinnerPanel;
    private JSpinner nSpin;
    private JSpinner mSpin;
    private JSpinner kSpin;
    private JSpinner numSpin;
    private JSpinner redSpin;
    private JSpinner aSpin;
    private JSpinner bSpin;
    private JSpinner cSpin;
    private JSpinner dSpin;
    private JSpinner greenSpin;
    private JSpinner znSpin;
    private JSpinner zfSpin;
    private JSpinner swSpin;
    private JSpinner shSpin;
    private JSpinner blueSpin;

    private final SplineView castedView;


//    private List<Point2D>

    public SplineDialog() {
        castedView = (SplineView) splineView;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        initColorSpinners();
        initAb();

        splineView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    castedView.addPivot(e.getX(), e.getY());
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    Point2D p = castedView.getPivot(e.getX(), e.getY());
                    if (p != null) {
                        castedView.removePivot(p);
                    }
                }
            }
        });

        MouseAdapter movePivots = new MouseAdapter() {
            private Point2D pivot;
            @Override
            public void mousePressed(MouseEvent e) {
                pivot = castedView.getPivot(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDragged(e);
                pivot = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (pivot != null) {
                    Point2D.Double moved = new Point2D.Double(e.getX(), e.getY());
                    castedView.replacePivot(pivot, moved);
                    pivot = moved;
                }
            }
        };
        splineView.addMouseListener(movePivots);
        splineView.addMouseMotionListener(movePivots);
    }

    private void initColorSpinners() {
        Color c = ((SplineView) splineView).getSplineColor();
        redSpin.setModel(new SpinnerNumberModel(c.getRed(), 0, 255, 5));
        greenSpin.setModel(new SpinnerNumberModel(c.getGreen(), 0, 255, 5));
        blueSpin.setModel(new SpinnerNumberModel(c.getBlue(), 0, 255, 5));

        ChangeListener listener = e -> ((SplineView) splineView).setSplineColor(
                new Color((Integer) redSpin.getValue(), (Integer) greenSpin.getValue(), (Integer) blueSpin.getValue()));

        redSpin.addChangeListener(listener);
        greenSpin.addChangeListener(listener);
        blueSpin.addChangeListener(listener);
    }

    private void initAb() {
        // todo: show in fixed precision?
        aSpin.setModel(new SpinnerNumberModel(0., 0., 1., 0.05));
        bSpin.setModel(new SpinnerNumberModel(1., 0., 1., 0.05));

        ChangeListener listener =
                e -> ((SplineView) splineView).setHighlightedSegment((Double) aSpin.getValue(), (Double) bSpin.getValue());
        aSpin.addChangeListener(listener);
        bSpin.addChangeListener(listener);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        SplineDialog dialog = new SplineDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        splineView = new SplineView();
    }
}
