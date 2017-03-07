package com.puke.tb;

import com.puke.template.Target;
import com.puke.template.TemplateHelper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TargetResolver {
    public static final String SRC_MAIN_RES = "src/main/res/";
    public static final String SRC_MAIN = "src/main/";
    public static final String ANDROID_MANIFEST = "AndroidManifest.xml";
    private final ManifestPathResolver resolver;

    public TargetResolver() {
        resolver = new ManifestPathResolver();
    }

    @NotNull
    Target resolveFiles(List<String> selectedFiles) {
        Target target = new Target();
        injectTarget(target, selectedFiles);
        return target;
    }

    private void injectTarget(@NotNull Target target, List<String> selectedFiles) {
        String manifest = null;
        List<String> javaFiles = target.getJavaFiles();
        List<String> layoutFiles = target.getLayoutFiles();
        List<String> drawableFiles = target.getDrawableFiles();
        for (String filePath : selectedFiles) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                List<String> subFiles = Arrays.asList(file.list());
                injectTarget(target, subFiles);
                continue;
            }
            if (filePath.endsWith(".java")) {
                javaFiles.add(filePath);
                manifest = resolver.getManifestPath(filePath);
                addActivityItemIfNeed(target, filePath, manifest);
                continue;
            }
            if (filePath.contains(SRC_MAIN_RES)) {
                if (filePath.contains("drawable")) {
                    drawableFiles.add(filePath);
                    manifest = resolver.getManifestPath(filePath);
                    continue;
                }
                if (filePath.endsWith(".xml") && filePath.contains("layout")) {
                    layoutFiles.add(filePath);
                    manifest = resolver.getManifestPath(filePath);
                    continue;
                }
            }
            throw new RuntimeException("Can't resolve file:\n" + filePath);
        }
        if (manifest != null) {
            target.setManifest(manifest);
        }
    }

    private void addActivityItemIfNeed(Target target, String javaFile, String manifest) {
        if (javaFile.endsWith("Activity.java") || javaFile.endsWith("Act.java")) {
            // com.puke.template
            String packageName = TemplateHelper.getPackageName(manifest);
            // /Users/zijiao/Documents/WorkSpace/AndroidStudio/TemplateCollections/app/src/main/java/com/puke/template/test/A.java
            String activityItem = javaFile.split("src/main/java/", 2)[1] // com/puke/template/test/A.java
                    .replace(".java", "") // com/puke/template/test/A
                    .replace('/', '.') // com.puke.template.test.A
                    .replace(packageName, ""); // .test.A
            target.getActivityItems().add(activityItem);
        }
    }

    private class ManifestPathResolver {

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