# 一个基于 Kotlin + MVVM + Jetpack + DataBinding + 协程的 玩Android 客户端
-------------------
## 为什么写这个项目
* 一开始，我只是打算写一个模板项目，把自己收集整理的项目模板、工具之类的拆分一下，好在将来的项目中直接依赖，弄完之后又觉得，就这么封装也没个demo，好像也不太好，于是就就着 WanAndroid 的开放 API，完成了这个项目。

## 项目架构
* Kotlin 语言
* MVVM 架构
* Jetpack 组件
* DataBinding
* Kotlin 依赖注入
* Retrofit + 协程 网络加载
* Glide 图片加载
* SmartRefreshLayout 刷新
* 大概就这些。。。

## 给你瞅瞅
* 首页
  ![首页](https://raw.githubusercontent.com/WangJie0822/SampleProject/master/images/homepage.png)

* 搜索
  ![搜索](https://raw.githubusercontent.com/WangJie0822/SampleProject/master/images/search.png)

* 项目
  ![项目](https://raw.githubusercontent.com/WangJie0822/SampleProject/master/images/project.png)

* 网页
  ![网页](https://raw.githubusercontent.com/WangJie0822/SampleProject/master/images/webview.png)

* 我的
  ![我的](https://raw.githubusercontent.com/WangJie0822/SampleProject/master/images/my.png)

* 登录
  ![登录](https://raw.githubusercontent.com/WangJie0822/SampleProject/master/images/login.png)

* 侧滑返回效果：

  ![侧滑返回效果](https://raw.githubusercontent.com/WangJie0822/SampleProject/master/images/swipe-back.gif)

## 最后

* 注意：由于 lib_recyclerview 库中用到了反射，所以需要添加混淆规则：
> -keep class cn.wj.android.recyclerview.\*\* { \*; } 
> -keep class * extends cn.wj.android.recyclerview.\*\* { \*; }
* apk下载地址:[https://raw.githubusercontent.com/WangJie0822/SampleProject/master/app/apk/sample_online_release_v1.0.0.apk](https://raw.githubusercontent.com/WangJie0822/SampleProject/master/demo/sample_online_release_v1.0.0.apk)
* 项目地址：[https://github.com/WangJie0822/SampleProject](https://github.com/WangJie0822/SampleProject)
* 暂时就做了这些功能，后续再加上。