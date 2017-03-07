package com.puke.tb.setting;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author zijiao
 * @version 17/3/6
 */
public class TemplateConfiguration implements UnnamedConfigurable {

    private final SettingView settingView;

    public TemplateConfiguration() {
        this.settingView = new SettingView();
    }


    @Nullable
    @Override
    public JComponent createComponent() {
        return settingView.getPanel();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {

    }

}
