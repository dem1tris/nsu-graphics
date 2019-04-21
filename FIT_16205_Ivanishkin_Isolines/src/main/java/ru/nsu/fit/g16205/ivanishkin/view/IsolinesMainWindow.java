package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.cg.ExtensionFileFilter;
import ru.nsu.fit.g16205.ivanishkin.model.Config;
import ru.nsu.fit.g16205.ivanishkin.model.ConfigBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Main window class
 *
 * @author Dmitry Ivanishkin
 */
public class IsolinesMainWindow extends AdvancedMainFrame {
    private static final int STATUSBAR_HEIGHT = 20;
    private static final String TITLE = "Isolines application";

    private File file;
    private Config config = Config.DEFAULT;
    private MapView view = new MapView(config);

    /**
     * Default constructor to create main window
     */
    public IsolinesMainWindow() {
        super(1200, 770, "Default - " + TITLE);
        setMinimumSize(new Dimension(400, 400));
        repaint();

        try {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    onExit();
                }
            });

            view.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    statusBar.setMessage(view.statusBarMessage(e.getX(), e.getY()));
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    statusBar.setMessage(view.statusBarMessage(e.getX(), e.getY()));
                    view.showTemporaryIsoline(e.getX(), e.getY());
                }
            });
            view.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    statusBar.setMessage("");
                }
            });
            add(view);
            statusBar.setPreferredSize(new Dimension(this.getWidth(), STATUSBAR_HEIGHT));
            pack();

            addSubMenu("File", KeyEvent.VK_F);
            addSubMenu("View", KeyEvent.VK_F);

            addSubMenu("Help", KeyEvent.VK_H);

            //region File
            addMenuItem("File/New", "Clear all", KeyEvent.VK_N, "new2.png", "onNew");
            addToolBarButton("File/New");

            addMenuItem("File/Open", "Open file", KeyEvent.VK_O, "open2.png", "onOpen");
            addToolBarButton("File/Open");

            addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X, "exit2.png", "onExit");
            addToolBarButton("File/Exit");

            addToolBarSeparator();
            //endregion

            JCheckBoxMenuItem interItem = addCheckboxMenuItem("View/Interpolation", "Enable/Disable color interpolation",
                    "interpolate.png", "onInterpolation");
            JToggleButton interButton = addToolBarToggleButton("View/Interpolation");
            bindCheckboxMenuWithToggleButton(interItem, interButton);

            JCheckBoxMenuItem isolinesItem = addCheckboxMenuItem("View/Isolines", "Show isolines",
                    "isolines.png", "onIsolines");
            JToggleButton isolinesButton = addToolBarToggleButton("View/Isolines");
            bindCheckboxMenuWithToggleButton(isolinesItem, isolinesButton);

            JCheckBoxMenuItem pointsItem = addCheckboxMenuItem("View/Points", "Show pivot points",
                    "points.png", "onPoints");
            JToggleButton pointsButton = addToolBarToggleButton("View/Points");
            bindCheckboxMenuWithToggleButton(pointsItem, pointsButton);

            JCheckBoxMenuItem gridItem = addCheckboxMenuItem("View/Show grid", "Show grid",
                    "grid.png", "onShowGrid");
            JToggleButton gridButton = addToolBarToggleButton("View/Show grid");
            bindCheckboxMenuWithToggleButton(gridItem, gridButton);

            addMenuItem("View/Clear", "Clear custom isolines", KeyEvent.VK_C, "clear.png", "onClear");
            addToolBarButton("View/Clear");

            addToolBarSeparator();

            addMenuItem("Help/Settings", "Settings",
                    KeyEvent.VK_S, "settings.png", "onSettings");
            addToolBarButton("Help/Settings");

            addMenuItem("Help/About...", "Shows program version and copyright information",
                    KeyEvent.VK_A, "about2.png", "onAbout");
            addToolBarButton("Help/About...");

            addToolBarSeparator();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onNew() {
        setTitle("Default - " + TITLE);
        config = Config.DEFAULT;
        view.reconfigure(config);
        repaint();
    }

    public void onOpen() {
        JFileChooser chooser = new JFileChooser();
        File dir = new File(System.getProperty("user.dir") + "/../FIT_16205_Ivanishkin_Isolines_Data/");
        chooser.setCurrentDirectory(dir);
        chooser.setFileFilter(new ExtensionFileFilter("txt", "Text files"));

        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            try {
                config = Config.from(new FileReader(file));
                view.reconfigure(config);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "File not found");
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
            setTitle(file.getName() + " - " + TITLE);
        }
    }

    public void onInterpolation(boolean val) {
        view.setInterpolationEnabled(val);
    }

    public void onIsolines(boolean val) {
        view.setShowIsolines(val);
    }

    public void onPoints(boolean val) {
        view.setShowPoints(val);
    }

    public void onShowGrid(boolean val) {
        view.setShowGrid(val);
    }

    public void onClear() {
        view.clearCustomIsolines();
    }

    public void onSettings() {
        LinkedHashMap<String, Integer> params = new LinkedHashMap<>();
        Config old = view.getConfig();
        params.put("Grid width", old.gridWidth);
        params.put("Grid height", old.gridHeight);
        params.put("x0", (int) old.x0);
        params.put("xEnd", (int) old.xEnd);
        params.put("y0", (int) old.y0);
        params.put("yEnd", (int) old.yEnd);

        Map<String, Consumer<Integer>> validators = new HashMap<>(2);
        Consumer<Integer> validator = val -> { if (val < 2) throw new IllegalArgumentException("Grid dimensions must be >= 2");};
        validators.put("Grid width", validator);
        validators.put("Grid height", validator);
        SettingsDialog settings = new SettingsDialog(params, validators);
        settings.setLocationRelativeTo(this);
        settings.pack();
        settings.setVisible(true);
        Integer[] vals = params.values().toArray(new Integer[0]);
        if (!settings.isCancelled()) {
            view.reconfigure(new ConfigBuilder(old)
                    .setGridWidth(vals[0])
                    .setGridHeight(vals[1])
                    .setX0(vals[2])
                    .setxEnd(vals[3])
                    .setY0(vals[4])
                    .setyEnd(vals[5])
                    .createConfig()
            );
        }
    }

    /**
     * File/Exit - exits application
     */
    public void onExit() {
        dispose();
        System.exit(0);
    }


    /**
     * Help/About... - shows program version and copyright information
     */
    public void onAbout() {
        JOptionPane.showMessageDialog(
                this,
                "Isolines, version 1.0\nCopyright © 2019 Dmitry Ivanishkin, NSU FIT, group 16205\nhttps://github.com/dem1tris/",
                "About Isolines",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Application main entry point
     *
     * @param args command line arguments (unused)
     */

    public static void main(String[] args) {
        IsolinesMainWindow mainFrame = new IsolinesMainWindow();
        mainFrame.setVisible(true);
    }
}