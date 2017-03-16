package com.puke.tb;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author zijiao
 * @version 17/3/16
 */
public class SelectItem {

    @NotNull
    public final String filePath;
    @NotNull
    public final PsiElement element;

    public SelectItem(String filePath, PsiElement element) {
        this.filePath = filePath;
        this.element = element;
    }

    @Override
    public int hashCode() {
        return filePath.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SelectItem) {
            return filePath.equals(((SelectItem) obj).filePath);
        }
        return super.equals(obj);
    }
}
