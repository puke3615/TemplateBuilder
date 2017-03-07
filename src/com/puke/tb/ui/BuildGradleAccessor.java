package com.puke.tb.ui;

import javax.swing.*;
import java.awt.event.*;

public class BuildGradleAccessor extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea _content;
    private JTextPane _exmple;
    private JTextArea repositories此处添加依赖MavenUrlTextArea;
    private Callback callback;

    public BuildGradleAccessor() {
        this(null);
    }

    public BuildGradleAccessor(String originContent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        _content.setText(originContent);

// onComplete onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// onComplete onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if (callback != null) {
            callback.call(_content.getText().trim());
        }
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void call(String result);
    }

    public static void getBuildGradleContent(Callback callback, String defaultText) {
        BuildGradleAccessor dialog = new BuildGradleAccessor(defaultText);
        dialog.setTitle("Configure build.gradle");
        dialog.setCallback(callback);
        dialog.pack();
        dialog.setSize(500, 520);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
