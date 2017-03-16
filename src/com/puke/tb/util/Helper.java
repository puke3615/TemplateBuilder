package com.puke.tb.util;

import com.intellij.ide.util.PropertiesComponent;
import com.puke.tb.ui.ToastManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author zijiao
 * @version 17/2/7
 */
public class Helper {

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
    }

    private static final String KEY_TEMPLATE_PATH = "TemplateBuilder_key_template_path";
    private static final String KEY_CATEGORY = "TemplateBuilder_key_category";

    public static void saveTemplatePath(String templatePath) {
        if (templatePath != null && templatePath.trim().length() > 0) {
            PropertiesComponent.getInstance().setValue(KEY_TEMPLATE_PATH, templatePath);
        }
    }

    public static String getTemplatePath() {
        String templatePath = PropertiesComponent.getInstance().getValue(KEY_TEMPLATE_PATH);
        if (templatePath != null && templatePath.trim().length() > 0) {
            return templatePath;
        }
        // if os is Mac (eg. "Mac OS X"), return default value.
        if (System.getProperty("os.name").startsWith("Mac")) {
            return Const.MAC_TEMPLATE_PATH;
        }
        return null;
    }

    public static void saveCategory(String category) {
        if (category != null && category.trim().length() > 0) {
            PropertiesComponent.getInstance().setValue(KEY_CATEGORY, category);
        }
    }

    public static String getCategory() {
        String category = PropertiesComponent.getInstance().getValue(KEY_CATEGORY);
        if (category != null && category.trim().length() > 0) {
            return category;
        }
        return getUser();
    }

    public interface Callback<T> {
        void call(T data);
    }

    public static <T> void execute(Callback<T> callback, T data) {
        try {
            callback.call(data);
        } catch (Throwable e) {
            String errorMsg = e.getMessage();
            if (e instanceof ArrayIndexOutOfBoundsException) {
                ToastManager.error("Please confirm that the file path contains \"src/main/java\"");
            } else {
                ToastManager.error(isEmpty(errorMsg) ? "Unknown error." : errorMsg);
            }
        }
    }

    public static boolean isEmpty(String... contents) {
        for (String content : contents) {
            if (content == null || content.length() == 0) {
                return true;
            }
        }
        return false;
    }

    public static String getUser() {
        return System.getProperty("user.name");
    }

    public static void deleteFile(@NotNull File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        } else if(file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFile(f);
                }
            }
        }
    }

}
