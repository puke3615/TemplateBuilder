package com.puke.tb;

import com.puke.template.BuildGradleOperation;
import com.puke.template.Processor;
import com.puke.template.Target;
import com.puke.template.TemplateConfig;
import com.puke.tb.ui.ConfigurationAccessor;
import com.puke.tb.util.Helper;

import java.io.File;

/**
 * @author zijiao
 * @version 17/2/7
 */
public class TemplateConfigImpl implements TemplateConfig {

    private String templatePath;
    private String moduleCategory;
    private String moduleName;
    private String moduleDesc;
    private String moduleData;
    private final BuildGradleOperation gradleOperation;

    public TemplateConfigImpl(String projectName, Target target, Processor processor) {
        gradleOperation = processor.getOperation(BuildGradleOperation.class);

    }

    private void onCommitForm(ConfigurationAccessor.FormData formData) {
        if (formData == null) {
            return;
        }
        this.templatePath = formData.getFolder();
        this.moduleCategory = formData.getCategory();
        this.moduleName = formData.getName();
        this.moduleDesc = formData.getDescription();
        this.moduleData = formData.getInputData();
        gradleOperation.setText(formData.getBuildGradleContent());
    }

    @Override
    public File getTemplatePath() {
        String templatePath = String.format("%s/%s/%s", this.templatePath, Helper.getUser(), moduleName);
        return new File(templatePath);
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public String getModuleDesc() {
        return moduleDesc;
    }

    @Override
    public String getModuleData() {
        return moduleData;
    }

    @Override
    public String getModuleCategory() {
        return moduleCategory;
    }

}
