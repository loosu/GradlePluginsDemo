package com.loosu.gradle.plugins;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * 第一个Gradle插件
 */
public class HelloWorldPlugin implements Plugin<Project> {

    /**
     * callback when gradle apply.
     *
     * @param project current project obj.
     */
    @Override
    public void apply(Project project) {
        DefaultGroovyMethods.println("Hello Groovy World!!! " + project);
    }
}
