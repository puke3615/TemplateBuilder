package com.puke.tb;

import com.puke.template.*;

/**
 * @author puke
 * @version 2018/7/30
 */
public class CustomProcessor extends Processor {

    @SafeVarargs
    public CustomProcessor(Target target, Class<? extends Operation>... operationClss) {
        super(target, operationClss);
    }

    public CustomProcessor(Target target) {
        this(target,
                FileOperation.class,
                CoverOperation.class,
                RecipeOperation.class,
                GlobalsOperation.class,
                TemplateOperation.class,
                BuildGradleOperation.class,
                FixedAndroidManifestOperation.class
        );
    }

    public static class FixedAndroidManifestOperation extends AndroidManifestOperation {

        private static final String REPLACEMENT = "xmlns:android=\"http://schemas.android.com/apk/res/android\"";
        private static final String ADDED = "package=\"${packageName}\"";

        public FixedAndroidManifestOperation(Target target) {
            super(target);
        }

        @Override
        protected String handleText(String originContent) {
            String text = super.handleText(originContent);
            if (text != null) {
                text = text.replace(REPLACEMENT, REPLACEMENT + "\n\t" + ADDED);
            }
            return text;
        }
    }

}
