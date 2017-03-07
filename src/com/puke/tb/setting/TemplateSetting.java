package com.puke.tb.setting;

import com.intellij.execution.configurations.RunConfigurationsSettings;
import com.intellij.openapi.options.UnnamedConfigurable;
import org.jetbrains.annotations.NotNull;

/**
 * @author zijiao
 * @version 17/3/6
 */
public class TemplateSetting implements RunConfigurationsSettings {

    @NotNull
    @Override
    public UnnamedConfigurable createConfigurable() {
        return new TemplateConfiguration();
    }

}
