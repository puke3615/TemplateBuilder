package com.puke.tb.ui;

import com.intellij.ui.JBColor;
import com.puke.template.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;

import static com.puke.tb.util.Helper.isEmpty;

public class InputAccessor extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField _id;
    private JTextField _name;
    private JTextField _defaultText;
    private JCheckBox _defaultSelected;
    private JTextField _help;
    private JComboBox _type;
    private JLabel _error;
    private Callback callback;

    public InputAccessor(InputData defaultData) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        _defaultText.setVisible(false);
        syncData(defaultData);

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// onComplete onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// onComplete onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onCancel();
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        _type.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    refreshDefaultValueUI();
                }
            }
        });

    }

    private void refreshDefaultValueUI() {
        String s = getSelectedType();
        if ("boolean".equals(s)) {
            _defaultText.setVisible(false);
            _defaultSelected.setVisible(true);
            _defaultSelected.setSelected(false);

        } else if ("string".equals(s)) {
            _defaultSelected.setVisible(false);
            _defaultText.setVisible(true);
            _defaultText.setText(null);

        }
    }

    private void syncData(InputData data) {
        if (data == null) {
            return;
        }
        setText(_id, data.id);
        setText(_name, data.name);
        setText(_help, data.help);
        String type = data.type;

        if ("boolean".equals(type)) {
            _type.setSelectedItem(type);
            refreshDefaultValueUI();
            boolean isSelected = Boolean.valueOf(data.defaultValue);
            _defaultSelected.setSelected(isSelected);

        } else if ("string".equals(type)) {
            _type.setSelectedItem(type);
            refreshDefaultValueUI();
            setText(_defaultText, data.defaultValue);

        }
    }

    private void onOK() {
        InputData inputData = new InputData(
                getText(_id), getText(_name), getSelectedType(),
                getDefaultValue(), getText(_help));
        if (validatePass(inputData)) {
            if (callback != null) {
                callback.call(inputData);
            }
            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }

    private boolean validatePass(InputData inputData) {
        if (isEmpty(inputData.id)) {
            error("id is empty.");
            return false;
        } else if (isEmpty(inputData.name)) {
            error("name is empty.");
            return false;
        } else if (isEmpty(inputData.type)) {
            error("type is empty.");
            return false;
        }
        return true;
    }

    public static class InputData {
        final String id;
        final String name;
        final String type;
        final String defaultValue;
        final String help;

        public InputData(String id, String name, String type, String defaultValue, String help) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.defaultValue = defaultValue;
            this.help = help;
        }

        public String parseInputInfo() {
            InputStream inputStream = InputAccessor.class.getResourceAsStream("/InputItem.template");
            return FileUtil.is2Str(inputStream)
                    .replace("`id`", id)
                    .replace("`name`", name)
                    .replace("`type`", type)
                    .replace("`default`", defaultValue)
                    .replace("`help`", help);
        }

        @Override
        public String toString() {
            return "InputData{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", defaultValue='" + defaultValue + '\'' +
                    ", help='" + help + '\'' +
                    '}';
        }
    }

    public interface Callback {
        void call(InputData inputData);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private void error(String message) {
        if (_error != null) {
            _error.setForeground(JBColor.RED);
            _error.setText(message == null ? "" : message);
        }
    }

    private String getSelectedType() {
        return _type.getSelectedItem().toString();
    }

    private String getDefaultValue() {
        String s = getSelectedType();
        if ("boolean".equals(s)) {
            return String.valueOf(_defaultSelected.isSelected());
        } else if ("string".equals(s)) {
            return getText(_defaultText);
        } else {
            return "";
        }

    }

    private static void setText(JTextField textField, String text) {
        if (textField != null) {
            textField.setText(text);
        }
    }

    private static String getText(JTextField textField) {
        if (textField == null) {
            return "";
        }
        return textField.getText().trim();
    }

    public static void getInputInfo(@NotNull Callback callback) {
        getInputInfo(callback, null);
    }

    public static void getInputInfo(@NotNull Callback callback, @Nullable InputData defaultData) {
        InputAccessor dialog = new InputAccessor(defaultData);
        dialog.setCallback(callback);
        dialog.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int width = screenWidth / 3;
        int height = width * 3 / 5;
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

}
