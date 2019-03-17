package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.cg.ExtensionFileFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Main window class
 *
 * @author Dmitry Ivanishkin
 */
public class FilterMainWindow extends AdvancedMainFrame {
    private static final int STATUSBAR_HEIGHT = 20;
    private static final String TITLE = "Filter application";
    private File file;
    private final JCheckBoxMenuItem xorItem;
    private final JToggleButton xorButton;
    private final JCheckBoxMenuItem replaceItem;
    private final JToggleButton replaceButton;
    private final ButtonGroup xorReplaceButtonGroup = new ButtonGroup();
    private final ButtonGroup xorReplaceMenuGroup = new ButtonGroup();

    private FilterView filterView;

    private JMenuItem nextItem;
    private JButton nextButton;

    private JMenuItem randomItem;
    private JButton randomButton;

    private JCheckBoxMenuItem impactItem;
    private JToggleButton impactButton;

    /**
     * Default constructor to create main window
     */
    public FilterMainWindow() {
        super(1000, 700, TITLE);

        try {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    onExit();
                }
            });

            filterView = new FilterView();
            JScrollPane scrollPane = new JScrollPane(filterView,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(6);
            scrollPane.getHorizontalScrollBar().setUnitIncrement(6);

            add(scrollPane);
            pack();
            statusBar.setPreferredSize(new Dimension(scrollPane.getWidth(), STATUSBAR_HEIGHT));

            addSubMenu("File", KeyEvent.VK_F);
            addSubMenu("Modify", KeyEvent.VK_M);
            addSubMenu("Action", KeyEvent.VK_M);
            addSubMenu("Help", KeyEvent.VK_H);

            addMenuItem("File/New", "New configuration", KeyEvent.VK_X, null, "onNew");
            addToolBarButton("File/New");

            addMenuItem("File/Open", "Open configuration", KeyEvent.VK_X, null, "onOpen");
            addToolBarButton("File/Open");

            addMenuItem("File/Save", "Save configuration", KeyEvent.VK_X, null, "onSave");
            addToolBarButton("File/Save");


            addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X, null, "onExit");
            addToolBarButton("File/Exit");

            addToolBarSeparator();

            impactItem = addCheckboxMenuItem("Modify/Show impact",
                    "Show impact value of each cell", null, "onImpact");
            impactButton = addToolBarToggleButton("Modify/Show impact");
            bindCheckboxMenuWithToggleButton(impactItem, impactButton);
            addToolBarToggleButton(impactButton);

            xorItem = addCheckboxMenuItem("Modify/XOR",
                    "Change aliveness on click", null, "onXor");
            xorButton = addToolBarToggleButton("Modify/XOR");
            bindCheckboxMenuWithToggleButton(xorItem, xorButton);
            xorReplaceMenuGroup.add(xorItem);
            xorReplaceButtonGroup.add(xorButton);
            addToolBarToggleButton(xorButton);

            replaceItem = addCheckboxMenuItem("Modify/Replace",
                    "Set cell alive on click", null, "onReplace");
            replaceButton = addToolBarToggleButton("Modify/Replace");
            bindCheckboxMenuWithToggleButton(replaceItem, replaceButton);
            xorReplaceMenuGroup.add(replaceItem);
            xorReplaceButtonGroup.add(replaceButton);
            addToolBarToggleButton(replaceButton);


            addMenuItem("Modify/Settings", "Change rules and view", KeyEvent.VK_S, null, "onSettings");
            addToolBarButton("Modify/Settings");

            addToolBarSeparator();

            addMenuItem("Action/Clear", "Set all cells not alive", KeyEvent.VK_C, null, "onClear");
            addToolBarButton("Action/Clear");

            randomItem = addMenuItem("Action/Random", "Randomly (p == 0.3) set alive cells", KeyEvent.VK_R,
                    null, "onRandom");
            randomButton = addToolBarButton("Action/Random");


            nextItem = addMenuItem("Action/Next step", "Do one step forward", KeyEvent.VK_N, null,
                    "onNext");
            nextButton = addToolBarButton("Action/Next step");

            JCheckBoxMenuItem runItem = addCheckboxMenuItem("Action/Run", "Start/stop life", null,
                    "onRun");
            JToggleButton runButton = addToolBarToggleButton("Action/Run");
            bindCheckboxMenuWithToggleButton(runItem, runButton);
            addToolBarToggleButton(runButton);

            addToolBarSeparator();

            addMenuItem("Help/About...", "Shows program version and copyright information",
                    KeyEvent.VK_A, null, "onAbout");
            addToolBarButton("Help/About...");

            addToolBarSeparator();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onRun(boolean enabled) {

    }

    public void onImpact(boolean enabled) {

    }

    public void onNext() {
    }

    public void onSettings() {
//        SettingsDialog dialog = new SettingsDialog(filterView);
//        dialog.pack();
//        dialog.setLocationRelativeTo(this);
//        dialog.setVisible(true);
    }

    public void onClear() {
    }

    public void onRandom() {
    }

    public void onXor(boolean val) {

    }

    public void onReplace(boolean val) {
        onXor(!val);
    }

    /**
     * Help/About... - shows program version and copyright information
     */
    public void onAbout() {
        JOptionPane.showMessageDialog(
                this,
                "Life, version 1.0\nCopyright © 2019 Dmitry Ivanishkin, FIT, group 16205\nhttps://github.com/dem1tris/",
                "About Life",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void onNew() {
    }

    public void onOpen() {
//        if (!saveProposal()) {
//            return;
//        }
//        JFileChooser chooser = new JFileChooser();
//        File dir = new File(System.getProperty("user.dir") + "/../FIT_16205_Ivanishkin_Life_Data/");
//        chooser.setCurrentDirectory(dir);
//        chooser.setFileFilter(new ExtensionFileFilter("txt", "Text files"));
//
//        int ret = chooser.showOpenDialog(this);
//        if (ret == JFileChooser.APPROVE_OPTION) {
//            file = chooser.getSelectedFile();
//            Config config;
//            try {
//                config = Config.parseFile(file);
//                filterView.configure(config);
//            } catch (RuntimeException e) {
//                JOptionPane.showMessageDialog(this, "Error while opening file");
//            }
//            setTitle(file.getName() + " - " + TITLE);
//            filterView.getModel().setSaved(true);
//        }
    }

    public void onSave() {
        save();
    }

    public boolean save() {
//        JFileChooser chooser = new JFileChooser() {
//            @Override
//            public void approveSelection() {
//                File f = getSelectedFile();
//                if (f.exists() && getDialogType() == SAVE_DIALOG) {
//                    int result = JOptionPane.showConfirmDialog(this, "File already exists, overwrite?",
//                            "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
//                    switch (result) {
//                        case JOptionPane.YES_OPTION:
//                            super.approveSelection();
//                            return;
//                        case JOptionPane.CANCEL_OPTION:
//                            cancelSelection();
//                            return;
//                        case JOptionPane.NO_OPTION:
//                        case JOptionPane.CLOSED_OPTION:
//                            return;
//                    }
//                }
//                super.approveSelection();
//            }
//        };
//        File dir = new File(System.getProperty("user.dir") + "/../FIT_16205_Ivanishkin_Life_Data/");
//        chooser.setCurrentDirectory(file != null ? file : dir);
//        chooser.setFileFilter(new ExtensionFileFilter("txt", "Text files"));
//
//        int ret = chooser.showSaveDialog(this);
//        if (ret == JFileChooser.APPROVE_OPTION) {
//            file = chooser.getSelectedFile();
//            if (file == null) {
//                return false;
//            }
//            List<Point> alivePlaces = filterView.getModel().getAlivePlaces();
//            Config config = new Config(filterView.getWidthM(), filterView.getHeightN(), filterView.getLineStrokeWidth(),
//                    Hex.getSize(), alivePlaces.size(), alivePlaces);
//            try (FileWriter writer = new FileWriter(file)) {
//                config.print(writer);
//            } catch (IOException e) {
//                JOptionPane.showMessageDialog(this, "Error while saving file");
//            }
//            setTitle(file.getName() + " - " + TITLE);
//            filterView.getModel().setSaved(true);
//            return true;
//        }
        return false;
    }

    boolean saveProposal() {
//        if (!filterView.getModel().isSaved()) {
//            int res = JOptionPane.showConfirmDialog(this,
//                    "Would you like to save current configuration?",
//                    "Unsaved configuration",
//                    JOptionPane.YES_NO_CANCEL_OPTION);
//            if (res == JOptionPane.YES_OPTION) {
//                return save();
//            } else if (res == JOptionPane.CLOSED_OPTION || res == JOptionPane.CANCEL_OPTION) {
//                return false;
//            }
//        }
        return true;
    }

    /**
     * File/Exit - exits application
     */
    public void onExit() {
        dispose();
        System.exit(0);
    }

    /**
     * Application main entry point
     *
     * @param args command line arguments (unused)
     */

    public static void main(String[] args) {
        FilterMainWindow mainFrame = new FilterMainWindow();
        mainFrame.setVisible(true);
    }
}