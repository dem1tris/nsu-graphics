package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.fit.g16205.ivanishkin.controller.SliderTextFieldBinding;
import ru.nsu.fit.g16205.ivanishkin.model.Cell;
import ru.nsu.fit.g16205.ivanishkin.model.LifeModel;
import ru.nsu.fit.g16205.ivanishkin.model.LifeRules;

import javax.swing.*;
import java.awt.event.*;

public class SettingsDialog extends JDialog {
    private final LifeView view;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private ButtonGroup clickModeButtonGroup = new ButtonGroup();
    private JRadioButton xorRadioButton;
    private JRadioButton replaceRadioButton;
    private JCheckBox showImpactCheckBox;
    private JTextField fstImpactTextField;
    private JTextField sndImpactTextField;
    private JTextField lifeBeginTextField;
    private JTextField birthBeginTextField;
    private JTextField birthEndTextField;
    private JTextField lifeEndTextField;
    private JTextField widthTextField;
    private JSlider widthSlider;
    private JTextField heightTextField;
    private JSlider heightSlider;
    private JTextField cellTextField;
    private JSlider cellSlider;
    private JTextField borderTextField;
    private JSlider borderSlider;
    private JTextField delayTextField;
    private JSlider delaySlider;

    public SettingsDialog(final LifeView view) {
        this.view = view;
        setTitle("Settings");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        clickModeButtonGroup.add(xorRadioButton);
        clickModeButtonGroup.add(replaceRadioButton);

        widthSlider.setModel(new DefaultBoundedRangeModel(20, 0, 1, 200));
        heightSlider.setModel(new DefaultBoundedRangeModel(20, 0, 1, 200));
        cellSlider.setModel(new DefaultBoundedRangeModel(30, 0, 3, 200));
        borderSlider.setModel(new DefaultBoundedRangeModel(1, 0, 1, 10));
        delaySlider.setModel(new DefaultBoundedRangeModel(300, 0, 50, 4000));

        getData();
        new SliderTextFieldBinding(widthSlider, widthTextField);
        new SliderTextFieldBinding(heightSlider, heightTextField);
        new SliderTextFieldBinding(cellSlider, cellTextField);
        new SliderTextFieldBinding(borderSlider, borderTextField);
        new SliderTextFieldBinding(delaySlider, delayTextField);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // call onOK() on ENTER
        contentPane.registerKeyboardAction(e -> onOK(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER
                , 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    // todo: check why OK flashing slowly
    private void onOK() {
        try {
            setData();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return;
        }
        SwingUtilities.getAncestorOfClass(JScrollPane.class, view).revalidate();
        view.refreshField();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void setData() {
        // rules
        LifeRules rules = Cell.getRules();
        if (changed(Double.class, lifeBeginTextField, rules.LIFE_BEGIN)
                || changed(Double.class, lifeEndTextField, rules.LIFE_END)
                || changed(Double.class, birthBeginTextField, rules.BIRTH_BEGIN)
                || changed(Double.class, birthEndTextField, rules.BIRTH_END)
                || changed(Double.class, fstImpactTextField, rules.FST_IMPACT)
                || changed(Double.class, sndImpactTextField, rules.SND_IMPACT)
        ) {
            Cell.changeRules(new LifeRules(
                    Double.parseDouble(fstImpactTextField.getText()),
                    Double.parseDouble(sndImpactTextField.getText()),
                    Double.parseDouble(lifeBeginTextField.getText()),
                    Double.parseDouble(lifeEndTextField.getText()),
                    Double.parseDouble(birthBeginTextField.getText()),
                    Double.parseDouble(birthEndTextField.getText())));

        }

        // mode
        view.setXorClickMode(xorRadioButton.isSelected());

        // impact
        Hex.setShowImpact(showImpactCheckBox.isSelected());


        // field view
        if (changed(Integer.class, cellTextField, view.getCellSize())) {
            view.updateCellSize(Integer.parseInt(cellTextField.getText()));
        }
        if (changed(Integer.class, borderTextField, view.getLineStrokeWidth())) {
            view.setLineStroke(Integer.parseInt(borderTextField.getText()));
        }

        // timer
        if (changed(Integer.class, delayTextField, view.getPeriod())) {
            view.setPeriod(Integer.parseInt(delayTextField.getText()));
        }

        // field size
        if (changed(Integer.class, widthTextField, view.getWidthM())
                || changed(Integer.class, heightTextField, view.getHeightN())) {
            view.setModel(new LifeModel(Integer.parseInt(widthTextField.getText()),
                    Integer.parseInt(heightTextField.getText())));
        }


    }

    private boolean changed(Class<?> type, JTextField field, Number val) {
        try {
            if (type.equals(Integer.class)) {
                return val.intValue() != Integer.parseInt(field.getText());
            } else if (type.equals(Double.class)) {
                return val.doubleValue() != Double.parseDouble(field.getText());
            } else {
                throw new IllegalArgumentException("Unsupported type");
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void getData() {
        // impact
        showImpactCheckBox.setSelected(Hex.needShowImpact());

        // rules
        LifeRules rules = Cell.getRules();
        lifeBeginTextField.setText(Double.toString(rules.LIFE_BEGIN));
        lifeEndTextField.setText(Double.toString(rules.LIFE_END));
        birthBeginTextField.setText(Double.toString(rules.BIRTH_BEGIN));
        birthEndTextField.setText(Double.toString(rules.BIRTH_END));
        fstImpactTextField.setText(Double.toString(rules.FST_IMPACT));
        sndImpactTextField.setText(Double.toString(rules.SND_IMPACT));

        //field
        widthTextField.setText(Integer.toString(view.getWidthM()));
        heightTextField.setText(Integer.toString(view.getHeightN()));
        cellTextField.setText(Integer.toString(Hex.getSize()));
        borderTextField.setText(Integer.toString(view.getLineStrokeWidth()));
        delayTextField.setText(Integer.toString(view.getPeriod()));

        //mode
        xorRadioButton.setSelected(view.isXorClickMode());
        replaceRadioButton.setSelected(!view.isXorClickMode());

    }

    public boolean isModified() {
        LifeRules rules = Cell.getRules();
        // rules
        return !(lifeBeginTextField.getText().equals(Double.toString(rules.LIFE_BEGIN)) &&
                lifeEndTextField.getText().equals(Double.toString(rules.LIFE_END)) &&
                birthBeginTextField.getText().equals(Double.toString(rules.BIRTH_BEGIN)) &&
                birthEndTextField.getText().equals(Double.toString(rules.BIRTH_END)) &&
                fstImpactTextField.getText().equals(Double.toString(rules.FST_IMPACT)) &&
                sndImpactTextField.getText().equals(Double.toString(rules.SND_IMPACT)) &&
                // impact
                showImpactCheckBox.isSelected() != Hex.needShowImpact() &&
                // mode
                xorRadioButton.isSelected() != view.isXorClickMode() &&
                // field view
                borderTextField.getText().equals(Double.toString(view.getLineStrokeWidth())) &&
                cellTextField.getText().equals(Double.toString(Hex.getSize()))
        );
        // model parameters
    }
}
