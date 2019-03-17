package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.cg.MainFrame;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;

public class AdvancedMainFrame extends MainFrame {
    protected StatusBar statusBar;

    public AdvancedMainFrame(int x, int y, String title) {
        super(x, y, title);

        statusBar = new StatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }

    protected MouseListener tooltipToStatusBarTranslator(JComponent comp) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                statusBar.setMessage(comp.getToolTipText());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                statusBar.setMessage(null);

            }
        };
    }

    @Override
    public JMenuItem addMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod) throws SecurityException, NoSuchMethodException {
        JMenuItem jMenuItem = super.addMenuItem(title, tooltip, mnemonic, icon, actionMethod);
        jMenuItem.addMouseListener(tooltipToStatusBarTranslator(jMenuItem));
        return jMenuItem;
    }

    @Override
    public JButton createToolBarButton(String menuPath) {
        JButton toolBarButton = super.createToolBarButton(menuPath);
        toolBarButton.addMouseListener(tooltipToStatusBarTranslator(toolBarButton));
        return toolBarButton;
    }

    @SuppressWarnings("Duplicates")
    protected void bindCheckboxMenuWithToggleButton(JCheckBoxMenuItem item, JToggleButton button) {
        item.addChangeListener(evt -> {
            try {
                if (item.isSelected() && !button.isSelected()) {
                    button.setSelected(true);
                } else if (!item.isSelected() && button.isSelected()) {
                    button.setSelected(false);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        button.addChangeListener(evt -> {
            try {
                if (button.isSelected() && !item.isSelected()) {
                    item.setSelected(true);
                } else if (!button.isSelected() && item.isSelected()){
                    item.setSelected(false);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    @SuppressWarnings("Duplicates")
    public JCheckBoxMenuItem addCheckboxMenuItem(String title, String tooltip, String icon, String actionMethod)
            throws SecurityException, NoSuchMethodException {
        MenuElement element = getParentMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);

        JCheckBoxMenuItem item = new JCheckBoxMenuItem(getMenuPathName(title));
        item.setToolTipText(tooltip);
        if (icon != null)
            item.setIcon(new ImageIcon(getClass().getResource("../resources/" + icon), title));

        if (element instanceof JMenu)
            ((JMenu) element).add(item);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).add(item);
        else
            throw new InvalidParameterException("Invalid menu path: " + title);

        final Method method = getClass().getMethod(actionMethod, boolean.class);
        item.addActionListener(evt -> {
            try {
                if (item.isSelected()) {
                    method.invoke(AdvancedMainFrame.this, true);
                } else {
                    method.invoke(AdvancedMainFrame.this, false);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        item.addMouseListener(tooltipToStatusBarTranslator(item));
        return item;
    }

    public JToggleButton addToolBarToggleButton(String menuPath) {
        JToggleButton button = createToolBarToggleButton(menuPath);
        toolBar.add(button);
        return button;
    }

    public JToggleButton createToolBarToggleButton(String menuPath) {
        JMenuItem item = (JMenuItem) getMenuElement(menuPath);
        if (item == null)
            throw new InvalidParameterException("Menu path not found: " + menuPath);
        return createToolBarToggleButton(item);
    }

    @SuppressWarnings("Duplicates")
    public JToggleButton createToolBarToggleButton(JMenuItem item) {
        JToggleButton button = new JToggleButton(item.getIcon());
        for (ActionListener listener : item.getActionListeners())
            button.addActionListener(listener);
        button.setToolTipText(item.getToolTipText());
        button.addMouseListener(tooltipToStatusBarTranslator(button));
        return button;
    }

    public void addToolBarToggleButton(JToggleButton button) {
        toolBar.add(button);
    }
}