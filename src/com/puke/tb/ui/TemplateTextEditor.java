package com.puke.tb.ui;

import com.puke.template.Processor;
import com.puke.template.TextOperation;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class TemplateTextEditor extends JDialog {
    private final Processor processor;
    private final UICallback callback;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTabbedPane _tab;
    private final List<TextComponent> textComponents = new ArrayList<>();

    public TemplateTextEditor(@NotNull Processor processor, @NotNull UICallback callback) {
        this.processor = processor;
        this.callback = callback;
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
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        initTab();
    }

    private void initTab() {
        _tab.removeAll();
        processor.getOperations().stream()
                .filter(operation -> operation instanceof TextOperation)
                .forEach(operation -> {
                    TextOperation textOperation = (TextOperation) operation;
                    TextComponent textComponent = new TextComponent();
                    textComponent.onInit(textOperation);
                    _tab.add(textOperation.getActualFileName(), textComponent.getPanel());
                    textComponents.add(textComponent);
                });
        _tab.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

            }
        });
    }

    private void onOK() {
        notifyOnClick();
        dispose();
        callback.onComplete();
    }

    private void notifyOnClick() {
        for (TextComponent component : textComponents) {
            component.onOkClick();
        }
    }

    private void onCancel() {
        dispose();
        callback.onCancel();
    }

    public static void show(Processor processor, UICallback callback) {
        TemplateTextEditor dialog = new TemplateTextEditor(processor, callback);
        dialog.pack();
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
