package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.fit.g16205.ivanishkin.controller.SliderTextFieldBinding;
import ru.nsu.fit.g16205.ivanishkin.model.Cell;
import ru.nsu.fit.g16205.ivanishkin.model.LifeRules;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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

    {
        widthTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                return true;
            }
        });
    }

    public SettingsDialog(final LifeView view) {
        this.view = view;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        clickModeButtonGroup.add(xorRadioButton);
        clickModeButtonGroup.add(replaceRadioButton);

        //todo: sliders can't reach end position
        widthSlider.setModel(new DefaultBoundedRangeModel(20, 1, 1, 101));
        heightSlider.setModel(new DefaultBoundedRangeModel(20, 1, 1, 101));
        cellSlider.setModel(new DefaultBoundedRangeModel(30, 1, 3, 201));
        borderSlider.setModel(new DefaultBoundedRangeModel(1, 1, 1, 11));
        delaySlider.setModel(new DefaultBoundedRangeModel(1000, 200, 200, 4001));

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
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    // todo: check why OK flashing slowly
    private void onOK() {
        if (isModified()) {
            setData();
            view.refreshField();
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void setData() {
        // impact
        Hex.setShowImpact(showImpactCheckBox.isSelected());

        // rules
        Cell.changeRules(new LifeRules(
                        Double.parseDouble(fstImpactTextField.getText()),
                        Double.parseDouble(sndImpactTextField.getText()),
                        Double.parseDouble(lifeBeginTextField.getText()),
                        Double.parseDouble(lifeEndTextField.getText()),
                        Double.parseDouble(birthBeginTextField.getText()),
                        Double.parseDouble(birthEndTextField.getText())));

        // field view
        view.setLineStroke(Integer.parseInt(borderTextField.getText()));
        view.updateCellSize(Integer.parseInt(cellTextField.getText()));

        // mode
        view.setXorClickMode(xorRadioButton.isSelected());

    }

    public void getData() {
        // impact
        showImpactCheckBox.setSelected(Hex.needShowImpact());

        // rules
        LifeRules rules = Cell.getRules();
        lifeBeginTextField.setText(Double.toString(rules.LIVE_BEGIN));
        lifeEndTextField.setText(Double.toString(rules.LIVE_END));
        birthBeginTextField.setText(Double.toString(rules.BIRTH_BEGIN));
        birthEndTextField.setText(Double.toString(rules.BIRTH_END));
        fstImpactTextField.setText(Double.toString(rules.FST_IMPACT));
        sndImpactTextField.setText(Double.toString(rules.SND_IMPACT));

        //field
        widthTextField.setText(Integer.toString(view.getWidthM()));
        heightTextField.setText(Integer.toString(view.getHeightN()));
        cellTextField.setText(Integer.toString(Hex.getSize()));
        borderTextField.setText(Integer.toString(view.getLineStroke()));

        //mode
        xorRadioButton.setSelected(view.isXorClickMode());
        replaceRadioButton.setSelected(!view.isXorClickMode());

    }

    public boolean isModified() {
        LifeRules rules = Cell.getRules();
        // rules
        return !(lifeBeginTextField.getText().equals(Double.toString(rules.LIVE_BEGIN)) &&
                lifeEndTextField.getText().equals(Double.toString(rules.LIVE_END)) &&
                birthBeginTextField.getText().equals(Double.toString(rules.BIRTH_BEGIN)) &&
                birthEndTextField.getText().equals(Double.toString(rules.BIRTH_END)) &&
                fstImpactTextField.getText().equals(Double.toString(rules.FST_IMPACT)) &&
                sndImpactTextField.getText().equals(Double.toString(rules.SND_IMPACT)) &&
                // impact
                showImpactCheckBox.isSelected() != Hex.needShowImpact() &&
                // mode
                xorRadioButton.isSelected() != view.isXorClickMode() &&
                // field view
                borderTextField.getText().equals(Double.toString(view.getLineStroke())) &&
                cellTextField.getText().equals(Double.toString(Hex.getSize()))
        );
        // model parameters
    }
}
