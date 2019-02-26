package ru.nsu.fit.g16205.ivanishkin;

import ru.nsu.cg.MainFrame;
import ru.nsu.fit.g16205.ivanishkin.model.LifeModel;
import ru.nsu.fit.g16205.ivanishkin.view.LifeView;
import ru.nsu.fit.g16205.ivanishkin.view.SettingsDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Main window class
 *
 * @author Dmitry Ivanishkin
 */
public class LifeMainWindow extends MainFrame {
    /**
     * Default constructor to create main window
     */

    private LifeView lifeView;
    public LifeMainWindow() {
        super(600, 400, "Life application");

        try {
            addSubMenu("File", KeyEvent.VK_F);
            addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X, "Exit.gif", "onExit");
            addToolBarButton("File/Exit");
            addToolBarSeparator();

            addSubMenu("Modify", KeyEvent.VK_M);
            addMenuItem("Modify/Settings", "Change rules and view", KeyEvent.VK_S, "Settings.png","onSettings");
            addToolBarButton("Modify/Settings");
//            addMenuItem()
            addToolBarSeparator();

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "Shows program version and copyright information",
                    KeyEvent.VK_A, "About.gif", "onAbout");
            addToolBarButton("Help/About...");

            //todo: magic constants
            //todo: update scrollbars immediately after cell size changed (repaint scrollPane?)
            lifeView = new LifeView(new LifeModel(15, 10));
            JScrollPane scrollPane = new JScrollPane(lifeView,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            add(scrollPane);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onSettings() {
        SettingsDialog dialog = new SettingsDialog(lifeView);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Help/About... - shows program version and copyright information
     */
    public void onAbout() {
        JOptionPane.showMessageDialog(
                this,
                "Life, version 1.0\nCopyright © 2019 Dmitry Ivanishkin, FIT, group 16205",
                "About Life",
                JOptionPane.INFORMATION_MESSAGE);
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

    /**
     * File/Exit - exits application
     */
    public void onExit() {
        System.exit(0);
    }
}
