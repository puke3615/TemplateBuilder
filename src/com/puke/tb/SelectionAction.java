package com.puke.tb;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.puke.template.Processor;
import com.puke.template.Target;
import org.jetbrains.annotations.Nullable;
import com.puke.tb.ui.ConfigurationAccessor;
import com.puke.tb.ui.ToastManager;
import com.puke.tb.ui.UICallback;
import com.puke.tb.util.Const;
import com.puke.tb.util.Helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * @author zijiao
 * @version 17/2/7
 */
public class SelectionAction extends AnAction {

    private final TargetResolver targetResolver = new TargetResolver();

    @Override
    public void actionPerformed(AnActionEvent e) {
        Helper.execute(this::operate, e, e.getProject());
    }

    private void operate(AnActionEvent event) {
        ToastManager.setProject(event.getProject());
        Set<String> selectedFiles = collectSelectedFiles(event.getData(DataKeys.PSI_ELEMENT_ARRAY));
        if (selectedFiles.size() == 0) {
            throw new RuntimeException("No selected files found.");
        }
        Project project = event.getProject();
        String projectName = project == null ? "" : project.getName();

        Target target = targetResolver.resolveFiles(new ArrayList<>(selectedFiles));
        Processor processor = new Processor(target);

        ConfigurationAccessor.FormData formData = new ConfigurationAccessor.FormData(Const.MAC_TEMPLATE_PATH, Helper.getUser(), projectName, null, null, null);
        ConfigurationAccessor accessor = new ConfigurationAccessor(processor, formData);
        accessor.setCallback(new UICallback() {
            @Override
            public void onComplete() {
                processor.process();
                ToastManager.info("Generate completed, restart IDE please.");
            }

            @Override
            public void onCancel() {
                ToastManager.error("Operate canceled.");
            }
        });
        accessor.showDialog();
    }

    private Set<String> collectSelectedFiles(PsiElement[] elements) {
        Set<String> files = new HashSet<>();
        if (elements != null) {
            for (PsiElement element : elements) {
                PsiFile selectFile = getPsiFile(element);
                if (selectFile != null) {
                    files.add(selectFile.getVirtualFile().getPath());
                } else {
                    PsiElement[] children = element.getChildren();
                    files.addAll(collectSelectedFiles(children));
                }
            }
        }

        return files;
    }

    @Nullable
    private static PsiFile getPsiFile(@Nullable final PsiElement psiClass) {
        if (psiClass == null) {
            return null;
        }
        return psiClass.getContainingFile();
    }

}
