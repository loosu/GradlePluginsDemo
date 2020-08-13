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

##### 2.2.1 工程构建文件

工程build.gradle文件，项目根目录。 用于定义适用于项目中所有模块的构建配置。默认情况下，这个顶级构建文件使用 buildscript {} 代码块来定义项目中所有模块共用的 Gradle 存储区和依赖项。以下代码示例描述的默认设置和 DSL 元素。



仓库类型：

**flatDir**

```groovy
repositories {
    flatDir name: 'libs', dirs: "$projectDir/libs"
    flatDir dirs: ["$projectDir/libs1", "$projectDir/libs2"]
}
```



**JCenter,  Center, Ivy**

```groovy
repositories {
    maven   { url "http://repo.mycompany.com/maven2" }
    jcenter { url "http://repo.mycompany.com/maven2" }
    ivy     { url "http://repo.mycompany.com/maven2" }
}
```

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

```groovy
/**
 * 引用 Android plugin for Gradle
 * 这样我们才可以在android{} 代码块中使用构建 Android APP 所需的特定编译选项。
 */
apply plugin: 'com.android.application'

/**
 * android{} 代码块, 配置构建Android工程的属性和配置.
 */
android {

    /**
     * compileSdkVersion: 编译时使用的Android API版本
     */
    compileSdkVersion 29

    /**
     * buildToolsVersion: 指定Gradle编译APP时所使用的SDK编译工具、命令行工具和编译器的版本.
     * 我们可以通过SDK Manager去下载使用到的编译工具.
     */
    buildToolsVersion "30.0.0"

    /**
     * defaultConfig{} 代码块, 默认配置和编译变体入口.
     * 还可以在这里动态的覆盖main/AndroidManifest.xml的属性。
     *  在这个区块还可以设置产品风味，给不同的风味版本配置不同的属性值。
     */
    defaultConfig {
        applicationId "com.loosu.gradleplus"
        minSdkVersion 16
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    /**
     * buildTypes {} 代码块, 中配置多个编译类型。
     * 编译系统默认定义了两个类型：debug and release.。
     * debug类型：默认的配置文件中没有展示出来，它包含了debug工具，使用debug key签名APK。
     * release类型：使用Proguard设置，默认不签名APK。
     */
    buildTypes {
        release {

            /**
             * 开启代码缩减
             */
            minifyEnabled false

            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    /**
     * productFlavors{} 代码块, 区域可以配置不同的产品风味。
     * 这里不同风味版本的配置可以覆盖掉 defaultConfig {} 中的设置。
     * 产品风味是可选项，编译系统默认不会自动创建不同的产品风味。
     * 下面例子创建了免费版和付费版两种产品风味。
     * 每种产品风味都指定了自己的应用ID，这使得两种风味的版本可以同时安装在一个设备上。
     */
    flavorDimensions 'loosu'
    productFlavors {
        free {
            dimension 'loosu'
            applicationId "com.loosu.gradleplus.free"
        }
        pay {
            dimension 'loosu'
            applicationId "com.loosu.gradleplus.pay"
        }
    }

}

/**
 * dependencies {} 代码块, 中指定本模块中所需要的依赖.
 */
dependencies {
    implementation fileTree(dir: "libs", include: ["*.jar"])
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'com.google.android.material:material:1.0.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'androidx.vectordrawable:vectordrawable:1.1.0'
    implementation 'androidx.navigation:navigation-fragment:2.1.0'
    implementation 'androidx.navigation:navigation-ui:2.1.0'
    implementation 'androidx.lifecycle:lifecycle-extensions:2.1.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'

}
```



#### 2.3 Gradle 属性文件  (.properties)

##### 2.3.1 gradle.properties

我们可以在其中配置项目范围的 Gradle 设置，例如 Gradle 后台进程的最大堆大小。如需了解详细信息，请参阅[构建环境](https://docs.gradle.org/current/userguide/build_environment.html)。

```groovy
# Project-wide Gradle settings.
org.gradle.jvmargs=-Xmx2048m
android.enableJetifier=true
```



##### 2.3.2 local.properties

为构建系统配置本地环境属性，例如 SDK 安装路径。由于该文件的内容由 Android Studio 自动生成并且专用于本地开发者环境，因此我们不应该手动修改该文件，不应该将其纳入到版本控制系统中。

```groovy
sdk.dir=C\:\\Users\\luwei\\AppData\\Local\\Android\\Sdk
```



### 三. 第一个 Android Sutdio 插件

#### 3.1 创建插件

##### 3.1.1 创建一个lib模块

和正常的模块创建步骤一样，可以通过 **File** > **New** > **New Module** > **Android Library** 创建一个模块 。
```java
module
	-libs
	-src
	-main
		-java
		-res
		-AndroidManifest.xml
	-build.gradle
```



##### 3.1.2 改造成插件模块

- 文件夹 `main/resources/gradle-plugins/` 必须安装此路径创建;
- `{plugin name}.properties` 声明插件用于 apply plugin: {plugin name}

```
module
	-src
	-main
		-java
		-resources
			-gradle-plugins
				-{plugin name}.properties	// 是用于 apply plugin: {plugin name}
	-build.gradle
```

下图是我修改后的目录结构：

 <img src=".\readme\gradle-plugins-module.jpg" alt="gradle-plugins-module"  />

创建文件夹:  src/main/resources/META-INF/gradle-plugins

创建.preperties文件：



##### 3.1.3 编写插件功能

修改build.gradle，添加插件需要用到的依赖。

build.gradle (:helloworld-gradle-plugin)

```groovy
apply plugin: 'groovy'

dependencies {
    implementation gradleApi()
    implementation localGroovy()
}
```



让插件被 apply 时，打印信息 "Hello Groovy World!!! "

main/java/com/loosu/gradle/plugins/HelloWorldPlugin.java

```java
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
```



##### 3.1.4  插件与实现类关联

/main/resources/META-INF/gradle-plugins/com.loosu.gradle.plugins.helloworld.properties

```groovy
implementation-class=com.loosu.gradle.plugins.HelloWorldPlugin
```



#### 3.2 上传插件

1. 插件是要打包成 .jar 或 .aar  才能被IDE依赖；

2. 正常情况是发布到maven cneter, 但现在为了方便在本地创建个maven仓库， 打包上传到本地.



单独执行下install任务就能完成本地打包上传工作.

build.gradle (:helloworld-gradle-plugin)

```groovy
...
apply plugin: 'maven'

group = 'com.loosu.plugins'
version = '1.0.0'

install {
    repositories {
        mavenDeployer {
            repository(url: uri('../locale-maven')) {
                pom.artifactId = 'hello-world-gradle-plugin'
            }
        }
    }
}
...
```



#### 3.3 使用插件



build.gradle (:project)

```groovy
buildscript {
	...
    repositories {
        ...
        maven {url uri('locale-maven')}
        ...
    }

    dependencies {
		...
        classpath('com.loosu.plugins:hello-world-gradle-plugin:1.0.0')
    }
}
```



build.gradle (:app)

```groovy
apply plugin: 'com.loosu.gradle.plugins.helloworld'
```

### 四. 参考资料

[Gradle和Gradle插件](https://www.jianshu.com/p/75aac767eaff)

[Gradle插件开发学习和实践](https://www.jianshu.com/p/32c150f0cb20)

[如何使用Android Studio开发Gradle插件](https://blog.csdn.net/sbsujjbcy/article/details/50782830)