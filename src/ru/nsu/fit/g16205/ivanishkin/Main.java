package ru.nsu.fit.g16205.ivanishkin;

import java.awt.Container;
/*  www .  j av  a  2s  . c o  m*/
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

import java.awt.Container;
//w w w .java  2s. c  om
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;

// Пример использования диалоговых окон JOptionPane

import javax.swing.*;
import java.awt.event.*;

class JOptionPaneTest extends JFrame
{
    private        JPanel  contents       = null;
    private        JButton btnMessage1    = null;
    private        JButton btnMessage2    = null;
    private        JButton btnMessage3    = null;

    private        JButton btnConfirm1    = null;
    private        JButton btnConfirm2    = null;
    private        JButton btnConfirm3    = null;

    private        JButton btnInput1      = null;
    private        JButton btnInput2      = null;
    private        JButton btnInput3      = null;

    private      ImageIcon  icon          = null;
    private final  String   TITLE_message = "Окно сообщения";
    private final  String   TITLE_confirm = "Окно подтверждения";
    private        String[] drink         = {"Сок",
            "Минералка",
            "Лимонад"  ,
            "Пиво"};
    public JOptionPaneTest()
    {
        super("Пример использования JOptionPane");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Локализация кнопок
        UIManager.put("OptionPane.yesButtonText"   , "Да"    );
        UIManager.put("OptionPane.noButtonText"    , "Нет"   );
        UIManager.put("OptionPane.cancelButtonText", "Отмена");

        contents = new JPanel();
        // Иконка для отображения в окне сообщений
        icon = new ImageIcon("images/warning.png");

        // Кнопка формирования окна по 2-м параметрам
        btnMessage1 = new JButton("MessageDialog 2");
        // Кнопка формирования окна по 4-м параметрам
        btnMessage2 = new JButton("MessageDialog 4");
        // Кнопка формирования окна по 5-и параметрам
        btnMessage3 = new JButton("MessageDialog 5");

        // Кнопки вывода сообщений подтверждения
        btnConfirm1 = new JButton("ConfirmDialog 4+2");
        btnConfirm2 = new JButton("ConfirmDialog 5");
        btnConfirm3 = new JButton("ConfirmDialog 6");

        btnInput1 = new JButton("InputDialog 2+3");
        btnInput2 = new JButton("InputDialog 4");
        btnInput3 = new JButton("InputDialog 7");



        // Размещение кнопок в интерфейсе
        contents.add(btnMessage1);
        contents.add(btnMessage2);
        contents.add(btnMessage3);

        contents.add(btnConfirm1);
        contents.add(btnConfirm2);
        contents.add(btnConfirm3);

        contents.add(btnInput1);
        contents.add(btnInput2);
        contents.add(btnInput3);

        setContentPane(contents);
        // Вывод окна на экран
        setSize(500, 140);
        setVisible(true);
    }
}

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JOptionPaneTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
}