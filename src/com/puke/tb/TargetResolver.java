package com.puke.tb;

import com.intellij.psi.*;
import com.intellij.psi.util.InheritanceUtil;
import com.puke.tb.util.Const;
import com.puke.template.Target;
import com.puke.template.TemplateHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TargetResolver {
    public static final String SRC_MAIN_RES = "src/main/res/";
    public static final String SRC_MAIN = "src/main/";
    public static final String ANDROID_MANIFEST = "AndroidManifest.xml";

    @NotNull
    Target resolveFiles(List<SelectItem> selectedFiles) {
        Target target = new Target();
        injectTarget(target, selectedFiles, new ManifestPathResolver());
        return target;
    }

    private void injectTarget(@NotNull Target target, List<SelectItem> selectedFiles, ManifestPathResolver manifestPathResolver) {

        String manifest = null;
        List<String> javaFiles = target.getJavaFiles();
        List<String> layoutFiles = target.getLayoutFiles();
        List<String> drawableFiles = target.getDrawableFiles();
        for (SelectItem selectItem : selectedFiles) {
            String filePath = selectItem.filePath;
            if (filePath.endsWith(".java")) {
                javaFiles.add(filePath);
                manifest = manifestPathResolver.getManifestPath(filePath);
                addActivityItemIfNeed(target, selectItem, manifest);
                continue;
            }
            if (filePath.contains(SRC_MAIN_RES)) {
                if (filePath.contains("drawable")) {
                    drawableFiles.add(filePath);
                    manifest = manifestPathResolver.getManifestPath(filePath);
                    continue;
                }
                if (filePath.endsWith(".xml") && filePath.contains("layout")) {
                    layoutFiles.add(filePath);
                    manifest = manifestPathResolver.getManifestPath(filePath);
                    continue;
                }
            }
            throw new RuntimeException("Can't resolve file:\n" + filePath);
        }
        if (manifest != null) {
            target.setManifest(manifest);
        }
    }

    private void addActivityItemIfNeed(Target target, SelectItem selectItem, String manifest) {
        PsiElement element = selectItem.element;
        if (element instanceof PsiJavaFile) {
            PsiClass[] psiClasses = ((PsiJavaFile) element).getClasses();
            if (psiClasses.length > 0) {
                element = psiClasses[0];
            }
        }
        if (!(element instanceof PsiClass)) {
            return;
        }
        PsiClass psiClass = (PsiClass) element;
        if (psiClass.hasModifierProperty(PsiModifier.ABSTRACT)) {
            return;
        }
        if (!InheritanceUtil.isInheritor(psiClass, Const.TYPE_NAME_ACTIVITY)) {
            return;
        }
        final String javaFile = selectItem.filePath;
        // com.puke.template
        String packageName = TemplateHelper.getPackageName(manifest);
        // /Users/zijiao/Documents/WorkSpace/AndroidStudio/TemplateCollections/app/src/main/java/com/puke/template/test/A.java
        String activityItem = javaFile.split("src/main/java/", 2)[1] // com/puke/template/test/A.java
                .replace(".java", "") // com/puke/template/test/A
                .replace('/', '.') // com.puke.template.test.A
                .replace(packageName, ""); // .test.A
        target.getActivityItems().add(activityItem);
    }

    private static class ManifestPathResolver {

        private String manifestPath;

        String getManifestPath(String selectFilePath) {
            if (manifestPath == null) {
                if (!selectFilePath.contains(SRC_MAIN)) {
                    throw new RuntimeException("Invalid file:\n" + selectFilePath);
                }
                manifestPath = selectFilePath.split(SRC_MAIN, 2)[0] + SRC_MAIN + ANDROID_MANIFEST;
            }
            return manifestPath;
        }

    }


}