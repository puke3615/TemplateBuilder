package com.puke.tb.ui;

import com.puke.template.Target;
import com.puke.template.util.FileUtil;

import java.io.InputStream;

/**
 * @author zijiao
 * @version 17/2/28
 */
public class BuildGradleAccessorTest {

    public static void main(String[] args) {
        InputStream is = Target.class.getResourceAsStream("/templates/root/build.gradle.ftl");
        String buildGradleContent = FileUtil.is2Str(is);
        System.out.println(buildGradleContent);
        BuildGradleAccessor.getBuildGradleContent(System.out::println, buildGradleContent);
    }

}
