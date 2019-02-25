package ru.nsu.fit.g16205.ivanishkin.view;

import com.sun.istack.internal.NotNull;
import ru.nsu.fit.g16205.ivanishkin.model.Cell;
import ru.nsu.fit.g16205.ivanishkin.model.LifeRules;

import javax.swing.*;
import java.awt.event.*;
import java.util.Objects;

public class SettingsDialog extends JDialog {
    private final Runnable onSavingModified;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton XORRadioButton;
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
                return false;
            }
        });
    }

    public SettingsDialog(final Runnable onSavingModified) {
        this.onSavingModified = Objects.requireNonNull(onSavingModified);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

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

        getData();
    }

    // todo: check why OK flashing slowly
    private void onOK() {
        if (isModified()) {
            setData();
            onSavingModified.run();
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        SettingsDialog dialog = new SettingsDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void setData() {
        Hex.setShowImpact(showImpactCheckBox.isSelected());
    }

    public void getData() {
        showImpactCheckBox.setSelected(Hex.needShowImpact());
        LifeRules rules = Cell.getRules();
        lifeBeginTextField.setText(Double.toString(rules.LIVE_BEGIN));
        lifeEndTextField.setText(Double.toString(rules.LIVE_END));
        birthBeginTextField.setText(Double.toString(rules.BIRTH_BEGIN));
        birthEndTextField.setText(Double.toString(rules.BIRTH_END));
        fstImpactTextField.setText(Double.toString(rules.FST_IMPACT));
        sndImpactTextField.setText(Double.toString(rules.SND_IMPACT));
    }

    public boolean isModified() {
        System.err.println("" + (showImpactCheckBox.isSelected() != Hex.needShowImpact()));
        return showImpactCheckBox.isSelected() != Hex.needShowImpact();
    }
}
