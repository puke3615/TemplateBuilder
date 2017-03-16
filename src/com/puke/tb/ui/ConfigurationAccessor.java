package com.puke.tb.ui;

import com.puke.tb.util.Helper;
import com.puke.template.Processor;
import com.puke.template.TemplateConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ConfigurationAccessor extends JDialog implements TemplateConfig {
    private JPanel _contentPane;
    private JButton _buttonOK;
    private JButton _buttonCancel;
    private JTextField _folder;
    private JTextField _name;
    private JTextField _description;
    private JList _list;
    private JButton _add;
    private JButton _remove;
    private JButton _edit;
    private JButton _down;
    private JButton _up;
    private JTextField _category;
    private JButton _next;
    private UICallback callback;
    private String buildGradleContent;
    private Validator validator = Validator.DEFAULT;
    private final List<InputAccessor.InputData> inputDataList = new ArrayList<InputAccessor.InputData>();
    private Processor processor;

    public ConfigurationAccessor(Processor processor, FormData defaultData) {
        this.processor = processor;
        setTitle("Configure Template Data");
        setContentPane(_contentPane);
        setModal(true);
        getRootPane().setDefaultButton(_buttonOK);

        _list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    int selectedIndex = _list.getSelectedIndex();
                    System.out.println(selectedIndex);
                }
            }
        });

        _buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        _buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        _add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAdd();
            }
        });
        _remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputNotNull(new Helper.Callback<InputAccessor.InputData>() {
                    @Override
                    public void call(InputAccessor.InputData data) {
                        onRemove(data);
                    }
                });
            }
        });
        _edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputNotNull(new Helper.Callback<InputAccessor.InputData>() {
                    @Override
                    public void call(InputAccessor.InputData data) {
                        onEdit(data);
                    }
                });
            }
        });

        _up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputNotNull(new Helper.Callback<InputAccessor.InputData>() {
                    @Override
                    public void call(InputAccessor.InputData data) {
                        onUp(data);
                    }
                });
            }
        });
        _down.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputNotNull(new Helper.Callback<InputAccessor.InputData>() {
                    @Override
                    public void call(InputAccessor.InputData data) {
                        onRemove(data);
                    }
                });
            }
        });

        _next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNext(e);
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
        _contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setFormData(defaultData);
        this.processor.setTemplateConfig(this);
    }

    private void onNext(ActionEvent e) {
        FormData formData = getFormData();
        if (validator == null || validator.validate(formData)) {
            saveData(formData);
            dispose();
            TemplateTextEditor.show(processor, callback);
        }
    }

    private void onUp(InputAccessor.InputData selected) {
        int index = inputDataList.indexOf(selected);
        if (index > 0) {
            inputDataList.remove(selected);
            inputDataList.add(index - 1, selected);
            notifyDataChange();
        }
    }

    private void onDown(InputAccessor.InputData selected) {
        int index = inputDataList.indexOf(selected);
        int size = inputDataList.size();
        if (index >= 0 && index < size - 1) {
            inputDataList.remove(selected);
            inputDataList.add(index + 1, selected);
            notifyDataChange();
        }
    }

    private void inputNotNull(Helper.Callback<InputAccessor.InputData> callback) {
        InputAccessor.InputData selectedInputData = getSelectedInputData();
        if (selectedInputData != null) {
            callback.call(selectedInputData);
        }
    }

    private void onAdd() {
        InputAccessor.getInputInfo(new InputAccessor.Callback() {
            @Override
            public void call(InputAccessor.InputData inputData) {
                addData(inputData);
            }
        });
    }

    private void onRemove(InputAccessor.InputData inputData) {
        removeData(inputDataList.indexOf(inputData));
    }

    private void onEdit(final InputAccessor.InputData inputData) {
        InputAccessor.getInputInfo(new InputAccessor.Callback() {
            @Override
            public void call(InputAccessor.InputData handledInputData) {
                replaceData(inputData, handledInputData);
            }
        }, inputData);
    }

    private InputAccessor.InputData getSelectedInputData() {
        int selectedIndex = _list.getSelectedIndex();
        return selectedIndex == -1 ? null : inputDataList.get(selectedIndex);
    }

    private void setData(InputAccessor.InputData... inputDatas) {
        inputDataList.clear();
        inputDataList.addAll(Arrays.asList(inputDatas));
        notifyDataChange();
    }

    private void addData(InputAccessor.InputData... inputDatas) {
        inputDataList.addAll(Arrays.asList(inputDatas));
        notifyDataChange();
    }

    private void removeData(int index) {
        if (index >= 0 && index < inputDataList.size()) {
            inputDataList.remove(index);
            notifyDataChange();
        }
    }

    private void replaceData(InputAccessor.InputData from, InputAccessor.InputData to) {
        if (from == null || to == null) {
            return;
        }
        int fromIndex = inputDataList.indexOf(from);
        inputDataList.remove(from);
        inputDataList.add(fromIndex, to);
        notifyDataChange();
    }

    private void notifyDataChange() {
        String[] contents = new String[inputDataList.size()];
        for (int i = 0; i < inputDataList.size(); i++) {
            contents[i] = inputDataList.get(i).id;
        }
        _list.setListData(contents);

    }

    private void onOK() {
        FormData formData = getFormData();
        if (validator == null || validator.validate(formData)) {
            saveData(formData);
            dispose();
            if (callback != null) {
                callback.onComplete();
            }
        }
    }

    private void saveData(FormData formData) {
        Helper.saveTemplatePath(formData.folder);
        Helper.saveCategory(formData.category);
    }

    @NotNull
    private FormData getFormData() {
        return new FormData(getFolder(), getText(_category), getText(_name), getText(_description), getInputData());
    }

    @NotNull
    private String getInputData() {
        StringBuilder inputData = new StringBuilder();
        for (InputAccessor.InputData data : inputDataList) {
            inputData.append(data.parseInputInfo());
        }
        return inputData.toString();
    }

    private void onCancel() {
        dispose();
        if (callback != null) {
            callback.onCancel();
        }
    }

    private static String getText(JTextField textField) {
        if (textField == null) {
            return null;
        }
        return textField.getText().trim();
    }

    private static void setText(JTextField textField, String text) {
        if (textField == null) {
            return;
        }
        textField.setText(text == null ? "" : text);
    }

    @Override
    public File getTemplatePath() {
        String templateRootPath = getFolder();
        return getFinalTemplatePath(templateRootPath, getText(_name));
    }

    private static File getFinalTemplatePath(String folder, String name) {
        String templatePath = String.format("%s/%s/%s", folder, Helper.getUser(), name);
        return new File(templatePath);
    }

    @Nullable
    private String getFolder() {
        String folder = getText(_folder);
        return folder == null ? null : folder.replace('\\', '/');
    }

    @Override
    public String getModuleName() {
        return getText(_name);
    }

    @Override
    public String getModuleDesc() {
        return getText(_description);
    }

    @Override
    public String getModuleData() {
        return getInputData();
    }

    @Override
    public String getModuleCategory() {
        return getText(_category);
    }

    public static class FormData {
        private final String folder;
        private final String category;
        private final String name;
        private final String description;
        private final String inputData;

        public FormData(String folder, String category, String name, String description, String inputData) {
            this.folder = folder;
            this.category = category;
            this.name = name;
            this.description = description;
            this.inputData = inputData;
        }

        public String getFolder() {
            return folder;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getInputData() {
            return inputData;
        }

        @Override
        public String toString() {
            return "FormData{" +
                    "folder='" + folder + '\'' +
                    ", category='" + category + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", inputData='" + inputData + '\'' +
                    '}';
        }


        public String getCategory() {
            return category;
        }

    }

    public interface Validator {
        boolean validate(FormData formData);

        Validator DEFAULT = new Validator() {
            @Override
            public boolean validate(FormData formData) {
                if (Helper.isEmpty(formData.category)) {
                    ToastManager.warn("The template category is empty.");
                    return false;
                }
                if (Helper.isEmpty(formData.folder)) {
                    ToastManager.warn("The template folder is empty.");
                    return false;
                }
                File templateDir = new File(formData.folder);
                if (!templateDir.exists() || !templateDir.isDirectory()) {
                    ToastManager.warn("The template folder not found.");
                    return false;
                }
                if (Helper.isEmpty(formData.name)) {
                    ToastManager.warn("The template name is empty.");
                    return false;
                }
                File finalTemplatePath = getFinalTemplatePath(formData.folder, formData.name);
                if (finalTemplatePath.exists() && finalTemplatePath.isDirectory()) {
                    Helper.deleteFile(finalTemplatePath);
                    String message = String.format(Locale.getDefault(), "The template named %s already exists, overwrite it?", formData.name);
                    return JOptionPane.showConfirmDialog(null, message) == JOptionPane.YES_OPTION;
                }
                return true;
            }
        };
    }

    public void setCallback(UICallback callback) {
        this.callback = callback;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setFormData(FormData formData) {
        if (formData == null) {
            return;
        }
        setText(_folder, formData.folder);
        setText(_category, formData.category);
        setText(_name, formData.name);
        setText(_description, formData.description);
    }

    // wrap the show method
    public void showDialog() {
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int width = 2 * screenWidth / 5;
        int height = width * 3 / 4;
        setSize(width, height);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
