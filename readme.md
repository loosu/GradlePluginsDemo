## Android 之 Gradle和Gradle插件

### 一. Gradle 和 Gradle插件

- **Gradle**: Gradle是一款帮助开发人员更快更好的编译、自动化运行和分发软件的工具。Gradle具有无与伦比的通用性，可以编译Java、C++，Python或者任何其他你使用的语言。[Gradle官网链接](https://gradle.org/)。
- **Gradle**插件: Android StudioGradle的编译系统使用的就是Gradle，编译Android App的插件叫做Android Plugin for Gradle。 Android Plugin for Gradle增加了编译Android APP所需要的特性。 [gradle-plugin安卓开发者官网](https://developer.android.google.cn/studio/releases/gradle-plugin) 虽然这个插件一般都是和Android Studio同步更新，但是它可以独立于Android Studio单独运行和更新。

| 插件版本      | 所需的 Gradle 版本 |
| ------------- | ------------------ |
| 1.0.0 - 1.1.3 | 2.2.1 - 2.3        |
| 1.2.0 - 1.3.1 | 2.2.1 - 2.9        |
| 1.5.0         | 2.2.1 - 2.13       |
| 2.0.0 - 2.1.2 | 2.10 - 2.13        |
| 2.1.3 - 2.2.3 | 2.14.1+            |
| 2.3.0+        | 3.3+               |
| 3.0.0+        | 4.1+               |
| 3.1.0+        | 4.4+               |
| 3.2.0 - 3.2.1 | 4.6+               |
| 3.3.0 - 3.3.2 | 4.10.1+            |
| 3.4.0 - 3.4.1 | 5.1.1+             |
| 3.5.0-3.5.3   | 5.4.1+             |
| 3.6.0+        | 5.6.4+             |



**修改Gradle版本**：您可以在 Android Studio 的 **File** > **Project Structure** > **Project** 菜单中指定 Gradle 版本，也可以通过在 `gradle/wrapper/gradle-wrapper.properties` 文件中修改 Gradle 分发引用来指定。以下示例在 `gradle-wrapper.properties` 文件中将 Gradle 的版本设置为 6.1.1。

```groovy
...
distributionUrl=https\://services.gradle.org/distributions/gradle-6.1.1-all.zip
...
```



**修改Gradle插件版本**：可以通过在 `build.gradle` 文件中修改`com.android.tools.build:gradle`属性，以下示例在 `build.gradle` 文件中将 Android Plugin for Gradle 的版本设置为 4.0.0

```groovy
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.0.0"
		...
    }
}
```



### 二. Android Sutio 中工程架构

Android Studio 的工程架构一般如下图，其中与Gradle相关的配置文件有:

**settings.gradle**: Gradle 设置文件;

**build.gradle**: Gradle构建文件;

**gradle.propertie**s: Gradle 属性文件;

**local.properties**: Gradle (本地)属性文件;

<img src=".\readme\project_frame.webp" alt="project_frame" style="zoom: 80%;" />



#### 2.1 Gradle 设置文件 (settings.gradle)

用于配置Gradle在构建应用时，将那些模块包含在内。模块名怎么设置？？？？

```groovy
include ':app'
```



#### 2.2 Gradle 构建文件 (build.gradle)

##### 2.2.1 工程（顶级）构建文件

工程build.gradle文件，项目根目录。 用于定义适用于项目中所有模块的构建配置。默认情况下，这个顶级构建文件使用 buildscript {} 代码块来定义项目中所有模块共用的 Gradle 存储区和依赖项。以下代码示例描述的默认设置和 DSL 元素。

```groovy
/**
 * buildscript{} 是配置构建工具Gradle本身所需要的仓库和依赖的代码块.
 */
buildscript {

    /**
     * repositories{} 代码块，配置Gradle下载依赖的仓库地址.
     * Gradle 内置支持的远程仓库地址有JCenter，Maven Center 和，Ivy 4.1版本开始内置支持google()。
     * 也可以使用本地仓库或者配置我们自己的远程仓库地址。
     * 如: maven { url 'https://jitpack.io' }
     */
    repositories {
        google()
        jcenter()
    }


    /**
     * dependencies{} 代码块, 配置Gradle编译工程师需要的依赖.
     */
    dependencies {
        classpath "com.android.tools.build:gradle:4.0.0"

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

/**
 * allprojects{} 代码块, 配置整个Gradle工程都用到的仓库和依赖.
 * 如果依赖不是被所有模块都需要，那应该将依赖配到需要它的模块级的build.gradle文件中。
 */
allprojects {
    repositories {
        google()
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```



##### 2.2.2 模块构建文件

模块build.gradle文件，目录 {project}/{module}/build.gradle。用于配置适用于其所在模块的构建设置。通过配置这些构建设置来提供自定义打包选项（例如附加构建类型和产品风味），以及替换 main/ 应用清单或顶级 build.gradle 文件中的设置。

