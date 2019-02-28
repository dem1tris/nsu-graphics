package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.cg.ExtensionFileFilter;
import ru.nsu.fit.g16205.ivanishkin.model.Config;
import ru.nsu.fit.g16205.ivanishkin.model.LifeModel;

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
public class LifeMainWindow extends AdvancedMainFrame {
    private static final int STATUSBAR_HEIGHT = 20;
    private static final String TITLE = "Life application";
    private File file;
    private final JCheckBoxMenuItem xorItem;
    private final JToggleButton xorButton;
    private final JCheckBoxMenuItem replaceItem;
    private final JToggleButton replaceButton;
    private final ButtonGroup xorReplaceButtonGroup = new ButtonGroup();
    private final ButtonGroup xorReplaceMenuGroup = new ButtonGroup();

    private LifeView lifeView;

    private JMenuItem nextItem;
    private JButton nextButton;

    private JMenuItem randomItem;
    private JButton randomButton;

    private JCheckBoxMenuItem impactItem;
    private JToggleButton impactButton;

    /**
     * Default constructor to create main window
     */
    public LifeMainWindow() {
        super(1000, 700, "Untitled - " + TITLE);

        try {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    onExit();
                }
            });

            //todo: magic constants
            lifeView = new LifeView(new LifeModel(30, 20));
            JScrollPane scrollPane = new JScrollPane(lifeView,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            add(scrollPane);
            statusBar.setPreferredSize(new Dimension(scrollPane.getWidth(), STATUSBAR_HEIGHT));

            addSubMenu("File", KeyEvent.VK_F);
            addSubMenu("Modify", KeyEvent.VK_M);
            addSubMenu("Action", KeyEvent.VK_M);
            addSubMenu("Help", KeyEvent.VK_H);

            addMenuItem("File/New", "New configuration", KeyEvent.VK_X, "new2.png", "onNew");
            addToolBarButton("File/New");

            addMenuItem("File/Open", "Open configuration", KeyEvent.VK_X, "open2.png", "onOpen");
            addToolBarButton("File/Open");

            addMenuItem("File/Save", "Save configuration", KeyEvent.VK_X, "save2.png", "onSave");
            addToolBarButton("File/Save");


            addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X, "exit2.png", "onExit");
            addToolBarButton("File/Exit");

            addToolBarSeparator();

            impactItem = addCheckboxMenuItem("Modify/Show impact",
                    "Show impact value of each cell", "impact2.png", "onImpact");
            impactButton = addToolBarToggleButton("Modify/Show impact");
            bindCheckboxMenuWithToggleButton(impactItem, impactButton);
            impactButton.setSelected(Hex.needShowImpact());
            addToolBarToggleButton(impactButton);

            xorItem = addCheckboxMenuItem("Modify/XOR",
                    "Change aliveness on click", "xor2.png", "onXor");
            xorButton = addToolBarToggleButton("Modify/XOR");
            bindCheckboxMenuWithToggleButton(xorItem, xorButton);
            xorReplaceMenuGroup.add(xorItem);
            xorReplaceButtonGroup.add(xorButton);
            addToolBarToggleButton(xorButton);

            replaceItem = addCheckboxMenuItem("Modify/Replace",
                    "Set cell alive on click", "replace2.png", "onReplace");
            replaceButton = addToolBarToggleButton("Modify/Replace");
            bindCheckboxMenuWithToggleButton(replaceItem, replaceButton);
            xorReplaceMenuGroup.add(replaceItem);
            xorReplaceButtonGroup.add(replaceButton);
            addToolBarToggleButton(replaceButton);

            xorButton.setSelected(lifeView.isXorClickMode());
            replaceButton.setSelected(!lifeView.isXorClickMode());

            addMenuItem("Modify/Settings", "Change rules and view", KeyEvent.VK_S, "settings2.png", "onSettings");
            addToolBarButton("Modify/Settings");

            addToolBarSeparator();

            addMenuItem("Action/Clear", "Set all cells not alive", KeyEvent.VK_C, "clear2.png", "onClear");
            addToolBarButton("Action/Clear");

            randomItem = addMenuItem("Action/Random", "Randomly (p == 0.3) set alive cells", KeyEvent.VK_R,
                    "random2.png", "onRandom");
            randomButton = addToolBarButton("Action/Random");


            nextItem = addMenuItem("Action/Next step", "Do one step forward", KeyEvent.VK_N, "next2.png",
                    "onNext");
            nextButton = addToolBarButton("Action/Next step");

            JCheckBoxMenuItem runItem = addCheckboxMenuItem("Action/Run", "Start/stop life", "run2.png",
                    "onRun");
            JToggleButton runButton = addToolBarToggleButton("Action/Run");
            bindCheckboxMenuWithToggleButton(runItem, runButton);
            addToolBarToggleButton(runButton);

            addToolBarSeparator();

            addMenuItem("Help/About...", "Shows program version and copyright information",
                    KeyEvent.VK_A, "about2.png", "onAbout");
            addToolBarButton("Help/About...");

            addToolBarSeparator();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onRun(boolean enabled) {
        if (enabled) {
            nextButton.setEnabled(false);
            nextItem.setEnabled(false);
            randomButton.setEnabled(false);
            randomItem.setEnabled(false);
            lifeView.start();
        } else {
            nextButton.setEnabled(true);
            nextItem.setEnabled(true);
            randomButton.setEnabled(true);
            randomItem.setEnabled(true);
            lifeView.stop();
        }
    }

    public void onImpact(boolean enabled) {
        if (enabled) {
            Hex.setShowImpact(true);
        } else {
            Hex.setShowImpact(false);
        }
        lifeView.refreshField();
    }

    public void onNext() {
        lifeView.getModel().nextStep();
    }

    public void onSettings() {
        SettingsDialog dialog = new SettingsDialog(lifeView);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        impactButton.setSelected(Hex.needShowImpact());
        xorButton.setSelected(lifeView.isXorClickMode());
    }

    public void onClear() {
        lifeView.getModel().clear();
    }

    public void onRandom() {
        lifeView.getModel().random();
    }

    public void onXor(boolean val) {
        if (val) {
            lifeView.setXorClickMode(true);
        } else {
            lifeView.setXorClickMode(false);
        }
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
        file = null;
        setTitle("Untitled - " + TITLE);
        lifeView.getModel().setSaved(false);
    }

    public void onOpen() {
        if (!saveProposal()) {
            return;
        }
        JFileChooser chooser = new JFileChooser();
        File dir = new File(System.getProperty("user.dir") + "/../FIT_16205_Ivanishkin_Life_Data/");
        chooser.setCurrentDirectory(dir);
        chooser.setFileFilter(new ExtensionFileFilter("txt", "Text files"));

        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            Config config;
            try {
                config = Config.parseFile(file);
                lifeView.configure(config);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, "Error while opening file");
            }
            setTitle(file.getName() + " - " + TITLE);
            lifeView.getModel().setSaved(true);
        }
    }

    public void onSave() {
        save();
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
        File dir = new File(System.getProperty("user.dir") + "/../FIT_16205_Ivanishkin_Life_Data/");
        chooser.setCurrentDirectory(file != null ? file : dir);
        chooser.setFileFilter(new ExtensionFileFilter("txt", "Text files"));

        int ret = chooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            if (file == null) {
                return false;
            }
            List<Point> alivePlaces = lifeView.getModel().getAlivePlaces();
            Config config = new Config(lifeView.getWidthM(), lifeView.getHeightN(), lifeView.getLineStrokeWidth(),
                    Hex.getSize(), alivePlaces.size(), alivePlaces);
            try (FileWriter writer = new FileWriter(file)) {
                config.print(writer);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error while saving file");
            }
            setTitle(file.getName() + " - " + TITLE);
            lifeView.getModel().setSaved(true);
            return true;
        }
        return false;
    }

    boolean saveProposal() {
        if (!lifeView.getModel().isSaved()) {
            int res = JOptionPane.showConfirmDialog(this,
                    "Would you like to save current configuration?",
                    "Exiting",
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
     * File/Exit - exits application
     */
    public void onExit() {
        if (!saveProposal()) {
            return;
        }
        dispose();
        System.exit(0);
    }

    /**
     * Application main entry point
     *
     * @param args command line arguments (unused)
     */

    public static void main(String[] args) {
        LifeMainWindow mainFrame = new LifeMainWindow();
        mainFrame.setVisible(true);
    }
}
