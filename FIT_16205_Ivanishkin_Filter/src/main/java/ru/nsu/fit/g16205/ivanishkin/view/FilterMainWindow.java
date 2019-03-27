package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.cg.ExtensionFileFilter;
import ru.nsu.fit.g16205.ivanishkin.filter.*;
import ru.nsu.fit.g16205.ivanishkin.volumeRendering.Config;
import ru.nsu.fit.g16205.ivanishkin.volumeRendering.Renderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Main window class
 *
 * @author Dmitry Ivanishkin
 */
public class FilterMainWindow extends AdvancedMainFrame {
    private static final int STATUSBAR_HEIGHT = 20;
    private static final String TITLE = "Filter application";
    private File file;

    private BufferedImage lastSaved;
    private final MainView mainView;
    private final OriginalImageView original;
    private final ImageView selected;
    private final ImageView filtered;
    private final PlotView absorption;
    private final PlotView emission;

    private final JToggleButton absButton;
    private final JToggleButton emButton;

    private Config config;


    /**
     * Default constructor to create main window
     */
    public FilterMainWindow() {
        super(1200, 770, "Untitled - " + TITLE);
        repaint();

        try {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    onExit();
                }
            });

            mainView = new MainView();
            original = mainView.getOriginal();
            selected = mainView.getSelected();
            filtered = mainView.getFiltered();
            absorption = mainView.getAbsorprion();
            emission = mainView.getEmission();
            JScrollPane scrollPane = new JScrollPane(mainView,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(6);
            scrollPane.getHorizontalScrollBar().setUnitIncrement(6);

            add(scrollPane);
            pack();
            statusBar.setPreferredSize(new Dimension(scrollPane.getWidth(), STATUSBAR_HEIGHT));

            addSubMenu("File", KeyEvent.VK_F);
            addSubMenu("Image", KeyEvent.VK_F);
            addSubMenu("Filters", KeyEvent.VK_F);
            addSubMenu("Volume", KeyEvent.VK_M);
            addSubMenu("Help", KeyEvent.VK_H);

            //region File
            addMenuItem("File/New", "Clear all", KeyEvent.VK_N, "new2.png", "onNew");
            addToolBarButton("File/New");

            addMenuItem("File/Open", "Open file", KeyEvent.VK_O, "open2.png", "onOpen");
            addToolBarButton("File/Open");

            addMenuItem("File/Save", "Save file", KeyEvent.VK_S, "save2.png", "onSave");
            addToolBarButton("File/Save");


            addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X, "exit2.png", "onExit");
            addToolBarButton("File/Exit");

            addToolBarSeparator();
            //endregion

            //region Image
            bindCheckboxMenuWithToggleButton(
                    addCheckboxMenuItem("Image/Select", "Select area to edit", "select2.png", "onSelect"),
                    addToolBarToggleButton("Image/Select")
            );

            addMenuItem("Image/Copy left", "Copy image to the left", KeyEvent.VK_L, "left2.png", "onLeft");
            addToolBarButton("Image/Copy left");

            addMenuItem("Image/Copy right", "Copy image to the right", KeyEvent.VK_R, "right2.png", "onRight");
            addToolBarButton("Image/Copy right");

            addToolBarSeparator();
            //endregion

            //region Filters
            addMenuItem("Filters/Grayscale", "Grayscale transformation", KeyEvent.VK_G, "grayscale2.png",
                    "onGrayscale");
            addToolBarButton("Filters/Grayscale");

            addMenuItem("Filters/Negative", "Negative transformation", KeyEvent.VK_N, "negative2.png",
                    "onNegative");
            addToolBarButton("Filters/Negative");

            addMenuItem("Filters/Magnify", "Magnifying transformation", KeyEvent.VK_M, "magnify2.png",
                    "onMagnify");
            addToolBarButton("Filters/Magnify");

            addMenuItem("Filters/Floyd-Stainberg dithering", "Floyd-Stainberg dithering", KeyEvent.VK_D,
                    "dither.png", "onFSDithering");
            addToolBarButton("Filters/Floyd-Stainberg dithering");

            addMenuItem("Filters/Ordered dithering", "Ordered dithering", KeyEvent.VK_D,
                    "odither.png", "onOrderedDithering");
            addToolBarButton("Filters/Ordered dithering");

            addMenuItem("Filters/Rotate", "Rotate image", KeyEvent.VK_D,
                    "rotate.png", "onRotate");
            addToolBarButton("Filters/Rotate");

            addMenuItem("Filters/Gamma", "Gamma correction", KeyEvent.VK_Y,
                    "gamma.png", "onGamma");
            addToolBarButton("Filters/Gamma");

            addToolBarSeparator();
            //endregion

            //region MatrixFilters
            addMenuItem("Filters/Roberts", "Roberts operator", KeyEvent.VK_R,
                    "roberts.png", "onRoberts");
            addToolBarButton("Filters/Roberts");

            addMenuItem("Filters/Sobel", "Sobel operator", KeyEvent.VK_R,
                    "sobel.png", "onSobel");
            addToolBarButton("Filters/Sobel");

            addMenuItem("Filters/Blur", "Blur filter", KeyEvent.VK_R,
                    "blur.png", "onBlur");
            addToolBarButton("Filters/Blur");

            addMenuItem("Filters/Sharpen", "Sharpen filter", KeyEvent.VK_R,
                    "sharpen.png", "onSharpen");
            addToolBarButton("Filters/Sharpen");

            addMenuItem("Filters/Emboss", "Emboss filter", KeyEvent.VK_R,
                    "emboss.png", "onEmboss");
            addToolBarButton("Filters/Emboss");

            addMenuItem("Filters/Aquarel", "Aquarel filter", KeyEvent.VK_R,
                    "aqua.png", "onAquarel");
            addToolBarButton("Filters/Aquarel");

            //endregion
            addToolBarSeparator();

            addMenuItem("Volume/Open config", "Open configuration file for volume rendering", KeyEvent.VK_R,
                    "open2.png", "onOpenConfig");
            addToolBarButton("Volume/Open config");

            JCheckBoxMenuItem absItem = addCheckboxMenuItem("Volume/Absorption", "Enable absorption",
                    "abs.png", "onAbsorption");
            absButton = addToolBarToggleButton("Volume/Absorption");
            bindCheckboxMenuWithToggleButton(absItem, absButton);

            JCheckBoxMenuItem emItem = addCheckboxMenuItem("Volume/Emission", "Enable emission",
                    "emission.png", "onEmission");
            emButton = addToolBarToggleButton("Volume/Emission");
            bindCheckboxMenuWithToggleButton(emItem, emButton);

            addMenuItem("Volume/Visualise", "Visualise volume rendering", KeyEvent.VK_R,
                    "run2.png", "onVisualise");
            addToolBarButton("Volume/Visualise");


            addToolBarSeparator();

            addMenuItem("Help/About...", "Shows program version and copyright information",
                    KeyEvent.VK_A, "about2.png", "onAbout");
            addToolBarButton("Help/About...");

            addToolBarSeparator();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onNew() {
        if (saveProposal()) {
            mainView.clear();
            setTitle("Untitled - " + TITLE);
        }
    }

    public void onOpen() {
        JFileChooser chooser = new JFileChooser();
        File dir = new File(System.getProperty("user.dir") + "/../FIT_16205_Ivanishkin_Filter_Data/");
        chooser.setCurrentDirectory(dir);
        //chooser.setFileFilter(new ExtensionFileFilter("txt", "Text files"));

        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            try {
                mainView.setImage(ImageIO.read(file));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error while opening file");
            }
            setTitle(file.getName() + " - " + TITLE);
        }
    }

    public void onSave() {
        save();
    }

    /**
     * File/Exit - exits application
     */
    public void onExit() {
        dispose();
        System.exit(0);
    }

    public void onSelect(boolean enabled) {
        original.setSelectMode(enabled, selected);
    }

    public void onLeft() {
        selected.setImage(filtered.getImage());
    }

    public void onRight() {
        filtered.setImage(selected.getImage());
    }

    public void onGrayscale() {
        filtered.setImage(new GrayscaleFilter().apply(selected.getImage()));
    }

    public void onNegative() {
        filtered.setImage(new NegativeFilter().apply(selected.getImage()));
    }

    public void onMagnify() {
        filtered.setImage(new MagnifyFilter().apply(selected.getImage()));
    }

    public void onFSDithering() {
        if (selected.getImage() != null) {
            LinkedHashMap<String, Integer> params = new LinkedHashMap<>();
            params.put("Red levels", 2);
            params.put("Green levels", 2);
            params.put("Blue levels", 2);
            JDialog settings = new FilterSettingsDialog(params);
            settings.setLocationRelativeTo(this);
            settings.pack();
            settings.setVisible(true);

            filtered.setImage(new FloydStainbergDitherFilter(params).apply(selected.getImage()));
        }
    }

    public void onOrderedDithering() {
        if (selected.getImage() != null) {
            LinkedHashMap<String, Integer> params = new LinkedHashMap<>();
            params.put("Red levels", 2);
            params.put("Green levels", 2);
            params.put("Blue levels", 2);
            JDialog settings = new FilterSettingsDialog(params);
            settings.setLocationRelativeTo(this);
            settings.pack();
            settings.setVisible(true);

            filtered.setImage(new OrderedDitherFilter(params).apply(selected.getImage()));
        }
    }

    public void onRotate() {
        if (selected.getImage() != null) {
            LinkedHashMap<String, Integer> params = new LinkedHashMap<>();
            params.put("Angle", 90);
            params.put("From", -180);
            params.put("To", 180);
            JDialog settings = new OneParameterDialog(params);
            settings.setLocationRelativeTo(this);
            settings.pack();
            settings.setVisible(true);
            filtered.setImage(new RotateFilter(params.get("Angle")).apply(selected.getImage()));
        }
    }

    public void onGamma() {
        if (selected.getImage() != null) {
            LinkedHashMap<String, Integer> params = new LinkedHashMap<>();
            params.put("Gamma, x100", 140);
            params.put("From", 0);
            params.put("To", 700);
            JDialog settings = new OneParameterDialog(params);
            settings.setLocationRelativeTo(this);
            settings.pack();
            settings.setVisible(true);
            filtered.setImage(new GammaFilter(params.get("Gamma, x100") / 100.).apply(selected.getImage()));
        }
    }

    public void onRoberts() {
        if (selected.getImage() != null) {
            LinkedHashMap<String, Integer> params = new LinkedHashMap<>();
            params.put("Threshold", 40);
            params.put("From", 0);
            params.put("To", 700);
            JDialog settings = new OneParameterDialog(params);
            settings.setLocationRelativeTo(this);
            settings.pack();
            settings.setVisible(true);
            filtered.setImage(new RobertsMatrixFilter(params.get("Threshold")).apply(selected.getImage()));
        }
    }

    public void onSobel() {
        if (selected.getImage() != null) {
            LinkedHashMap<String, Integer> params = new LinkedHashMap<>();
            params.put("Threshold", 40);
            params.put("From", 0);
            params.put("To", 700);
            JDialog settings = new OneParameterDialog(params);
            settings.setLocationRelativeTo(this);
            settings.pack();
            settings.setVisible(true);
            filtered.setImage(new SobelMatrixFilter(params.get("Threshold")).apply(selected.getImage()));
        }
    }

    public void onBlur() {
        if (selected.getImage() != null) {
            filtered.setImage(new BlurMatrixFilter().apply(selected.getImage()));
        }
    }

    public void onSharpen() {
        if (selected.getImage() != null) {
            filtered.setImage(new SharpenMatrixFilter().apply(selected.getImage()));
        }
    }

    public void onEmboss() {
        if (selected.getImage() != null) {
            filtered.setImage(new EmbossMatrixFilter().apply(selected.getImage()));
        }
    }

    public void onAquarel() {
        if (selected.getImage() != null) {
            filtered.setImage(new AquarelFilter().apply(selected.getImage()));
        }
    }

    public void onOpenConfig() {
        JFileChooser chooser = new JFileChooser();
        File dir = new File(System.getProperty("user.dir") + "/../FIT_16205_Ivanishkin_Filter_Data/");
        chooser.setCurrentDirectory(dir);
        chooser.setFileFilter(new ExtensionFileFilter("txt", "Text files"));

        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            try (FileReader reader = new FileReader(file)) {
                config = Config.from(reader);
                absorption.clear();
                emission.clear();
                absorption.addPlot(config.dotsAbsorption, Color.BLACK);
                emission.addPlot(config.dotsRedEmission, Color.RED);
                emission.addPlot(config.dotsGreenEmission, Color.GREEN);
                emission.addPlot(config.dotsBlueEmission, Color.BLUE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
            setTitle(file.getName() + " - " + TITLE);
        }
    }

    public void onAbsorption(boolean enable) {
        absButton.setSelected(enable);
    }

    public void onEmission(boolean enable) {
        emButton.setSelected(enable);
    }

    public void onVisualise() {
        if (selected.getImage() != null && config != null) {
            filtered.setImage(
                    new Renderer(config, 350, 350, 350, absButton.isSelected(), emButton.isSelected())
                            .apply(selected.getImage())
            );
        }
    }

    /**
     * Help/About... - shows program version and copyright information
     */
    public void onAbout() {
        JOptionPane.showMessageDialog(
                this,
                "Filter, version 1.0\nCopyright © 2019 Dmitry Ivanishkin, NSU FIT, group 16205\nhttps://github.com/dem1tris/",
                "About Filter",
                JOptionPane.INFORMATION_MESSAGE);
    }


    public boolean save() {
        JFileChooser chooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this, "File already exists, overwrite?",
                            "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        case JOptionPane.CANCEL_OPTION:
                            cancelSelection();
                            return;
                        case JOptionPane.NO_OPTION:
                        case JOptionPane.CLOSED_OPTION:
                            return;
                    }
                }
                super.approveSelection();
            }
        };
        File dir = new File(System.getProperty("user.dir") + "/../FIT_16205_Ivanishkin_Filter_Data/");
        chooser.setCurrentDirectory(file != null ? file : dir);
        chooser.setFileFilter(new ExtensionFileFilter("bmp", "Text files"));

        int ret = chooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            if (file == null) {
                return false;
            }
            try {
                ImageIO.write(filtered.getImage(), "bmp", file);
                lastSaved = filtered.getImage();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error while saving file");
            }
            setTitle(file.getName() + " - " + TITLE);
            return true;
        }
        return false;
    }

    private boolean saveProposal() {
        if (lastSaved != filtered.getImage()) {
            int res = JOptionPane.showConfirmDialog(this,
                    "Would you like to save current image?",
                    "Unsaved image",
                    JOptionPane.YES_NO_CANCEL_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                return save();
            } else if (res == JOptionPane.CLOSED_OPTION || res == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        return true;
    }

    /**
     * Application main entry point
     *
     * @param args command line arguments (unused)
     */

    public static void main(String[] args) throws IOException {
        FilterMainWindow mainFrame = new FilterMainWindow();
        mainFrame.setVisible(true);
    }
}