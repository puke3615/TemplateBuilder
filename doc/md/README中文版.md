[English Version](README.md)

*在阅读本文之前，默认你是了解 `Android Studio Template`的，知道为什么要使用它，如果不清楚它是什么，可以在网上自行查阅相关资料，当然也可以直接[点击这里](https://puke3615.github.io/2017/03/10/Android%20Studio%20Template/)查看一下同行们对它的评价。*

## WHAT

`TemplateBuilder`是一款能够帮助我们快速生成`Android Studio Template`的AS插件，将通过逐个文件去配置模板的方式改进为通过插件来实现，对于简单的模板制作，只需要一键即可生成。

## HOW

#### 安装

打开Android Studio，`Preferences` -- `Plugins` -- `Brown Repositories`,  搜索`TemplateBuilder` 并下载，下载之后重启IDE即可使用。

![](doc/images/img1.png)

#### 使用

这里我们就拿对一个MVP模板的抽取来举例，首先选中需要制作为模板的类文件（如果需要选中某个包下面的全部文件，可以直接选中该包），然后按下启动改插件的快捷键`ALT + T`，便能看到如下界面

![](doc/images/img3.png)

下面来说明一下该界面对应的内容

* `Template Category`  对应模板的分类，对应选择导入模板时的模板分类，这里默认值是电脑的用户名。

* `Template Name`  对应模板名称，对应选择导入模板时的模板名称，默认值是当前的Project名称。

* `Template Description`  对应模板描述信息，对应导入模板时弹出的导入界面的文字描述，默认为空。

* `Template Folder`  对应生成模板所存放的位置，如果是Mac操作系统则默认为`/Applications/Android Studio.app/Contents/plugins/android/lib/templates`， Windows系统的话由于差异比较大，就默认为空了，可以自行配置`[Android Studio安装目录]/plugins/android/lib/templates`（这里只需要配置一次即可，插件将自动保存该位置）。

* `Input data区域`  对应使用我们模板时的变量输入，如果你想增强模板的可配性将会用到它，它就对应着AS模板中`Template.xml`的`<parameter />`标签，也就是下面的每一个配置项。

  ![](doc/images/img5.png)

整个的UI界面很明了，如果你只是想简单抽取你写的这个模板，你只需要输入下`Template Name`和`Template Description`即可，当然如果你不想输入的话这也可以省略掉。接下来直接点击`Finish`，就能在IDE底部看到弹出如下图所示的提示，此时你已经制作成功，重启IDE即可使用该模板。

![](doc/images/img4.png)

到这里，如果你了解`Android Studio Template`制作流程的话，你可能觉得相对于完全自己手动去做这样固然简单，但是灵活性上降低了不少。例如模板中有组价依赖关系的时候，你需要在`build.gradle.ftl`中去添加对应的依赖；导入模板之后你想自动打开某个文件时，你需要编辑`recipe.xml.ftl`；选择模板适用于哪个版本时，你需要编辑`template.xml`等等。如果你有这方面的需求，可以点击`Next`来代替`Finish`，点击之后你就能看到如下界面，在这里提供了所有你需要关注的配置文件的编辑，编辑完成直接点击`OK`开始自动生成。

![](doc/images/img6.png)

## WHY

如果你尝试过自己去制作一个`Android Studio Template`的话，相信你一定会为模板配置、更改文件名、添加可配参数等等繁琐的操作而烦恼，因为哪怕一个很简单的模板抽取都需要把这一系列流程全都走一遍，更改好几个配置文件。

说下我的个人感受，当我发现`Android Studio Template`时我顿时感觉有太多太多可以抽取出来的模板以供日后快速使用，但当我刚开始抽取几个模板的时候就已经被深深的恶心到了，因为这个过程真心很繁琐。记得当时还专门写groovy脚本来辅助进行这些模板的配置，但也还是需要向脚本里添加一些模板文件的入参信息，依然很麻烦。后来我决定将这个制作过程尽可能通过自动化来完成，当时考虑`Gradle插件`、`Java GUI`、`AS Plugin`等多种方式来完成模板制作，最终选择了快而方便的`AS Plugin`，插件的名称就是上面所提到的`TemplateBuilder`。
