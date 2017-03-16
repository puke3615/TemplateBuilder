package com.puke.tb.ui;

import com.puke.template.TextOperation;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author zijiao
 * @version 17/3/5
 */
public class TextComponent {
    private JPanel panel1;
    private JTextArea _text;
    private boolean hasShown = false;
    private boolean isForeground = false;
    private TextOperation textOperation;

    public String getContent() {
        return _text.getText().trim();
    }

    public void onInit(final TextOperation textOperation) {
        this.textOperation = textOperation;
        panel1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                isForeground = true;
                hasShown = true;
                _text.setText(textOperation.getText());
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                isForeground = false;
                if (hasShown) {
                    textOperation.setText(getContent());
                }
            }
        });
    }

    public JPanel getPanel() {
        return panel1;
    }

    public void onOkClick() {
        if (isForeground) {
            textOperation.setText(getContent());
        }
    }
}
