package ru.nsu.fit.g16205.ivanishkin.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SliderTextFieldBinding implements ActionListener, FocusListener, ChangeListener {
    private final JSlider slider;
    private final JTextField field;

    public SliderTextFieldBinding(JSlider slider, JTextField field) {
        this.slider = slider;
        this.field = field;
        slider.addChangeListener(this);
        field.addActionListener(this);
        field.addFocusListener(this);
        toSlider();
    }

    private void toSlider() {
        try {
            slider.setValue(Integer.parseInt(field.getText()));
        } catch (NumberFormatException ex) {
        }
        toField();
    }

    private void toField() {
        field.setText(Integer.toString(slider.getValue()));
    }

    // field to slider
    @Override
    public void actionPerformed(ActionEvent e) {
        toSlider();
    }

    // slider to field
    @Override
    public void stateChanged(ChangeEvent e) {
        toField();
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        toSlider();
    }
}