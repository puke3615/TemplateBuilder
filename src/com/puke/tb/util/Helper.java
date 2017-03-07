package com.puke.tb.util;

import com.intellij.openapi.project.Project;
import com.puke.tb.ui.ToastManager;

/**
 * @author zijiao
 * @version 17/2/7
 */
public class Helper {

    public interface Callback<T> {
        void call(T data);
    }

    public static <T> void execute(Callback<T> callback, T data, Project project) {
        try {
            callback.call(data);
        } catch (Throwable e) {
            String errorMsg = e.getMessage();
            ToastManager.error(isEmpty(errorMsg) ? "Unknown error." : errorMsg);
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

}
