package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.cg.ExtensionFileFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Main window class
 *
 * @author Dmitry Ivanishkin
 */
public class WireframeMainWindow extends AdvancedMainFrame {
    private static final int STATUSBAR_HEIGHT = 20;
    private static final String TITLE = "Wireframe application";

    private File file;

    /**
     * Default constructor to create main window
     */
    public WireframeMainWindow() {
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


            statusBar.setPreferredSize(new Dimension(this.getWidth(), STATUSBAR_HEIGHT));
            pack();

            addSubMenu("File", KeyEvent.VK_F);
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
        repaint();
    }

    public void onOpen() {
        JFileChooser chooser = new JFileChooser();
        File dir = new File(System.getProperty("user.dir") + "/../FIT_16205_Ivanishkin_Wireframe_Data/");
        chooser.setCurrentDirectory(dir);
        chooser.setFileFilter(new ExtensionFileFilter("txt", "Text files"));

        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            setTitle(file.getName() + " - " + TITLE);
        }
    }

    public void onSettings() {
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
                "Wireframe, version 1.0\nCopyright © 2019 Dmitry Ivanishkin, NSU FIT, group 16205\nhttps://github.com/dem1tris/",
                "About Wireframe",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Application main entry point
     *
     * @param args command line arguments (unused)
     */

    public static void main(String[] args) {
        WireframeMainWindow mainFrame = new WireframeMainWindow();
        mainFrame.setVisible(true);
    }
}