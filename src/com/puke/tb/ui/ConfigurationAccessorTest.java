package com.puke.tb.ui;

import com.puke.template.Processor;

/**
 * @author zijiao
 * @version 17/2/8
 */
public class ConfigurationAccessorTest {

    public static void main(String[] args) {
        Processor processor = new Processor(null);
        ConfigurationAccessor.FormData formData = new ConfigurationAccessor.FormData("abc", "456", "123", null, null);
        ConfigurationAccessor accessor = new ConfigurationAccessor(processor, formData);
        accessor.setCallback(new UICallback() {
            @Override
            public void onComplete() {
                System.out.println("onComplete.");
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel.");
            }
        });
        accessor.showDialog();
    }

}
