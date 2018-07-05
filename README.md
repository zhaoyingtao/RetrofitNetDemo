# RetrofitNetDemo
这是一个基于retrofit的网络请求再度封装；  
功能：  
1、可以更改baseUrl；  
2、某一个请求长时间无返回值时，取消此请求，判断是否正在请求某一个api  

一、在项目中直接引入

compile 'com.bintray.library:net_library:1.1.0'

二、代码中使用
在项目的Application的oncreate方法中初始化：  
```//初始化网络请求
  RetrofitClientUtil.initClient(this).changeApiBaseUrl("你的baseUrl");
  ```

三、其他代码使用范例  
最好复制项目中的ApiEnvironmentMgr类、BaseApiService类和BaseAppPresenter类，可以适当修改；
直接查看项目中的ApiEnvironmentMgr类（主要做切换服务器使用）和BaseApiService类（存放请求api)  
四、取消api的方法  
`RxApiManager.get().cancel("请求api的flag");`  
判断是否正在请求某一个api  
` RxApiManager.get().isRequestingApi("请求api的flag");`

五、注意⚠️  
依赖库使用了  
```
compile 'com.squareup.retrofit2:retrofit:2.3.0'
compile 'com.squareup.retrofit2:converter-gson:2.3.0'
compile 'com.squareup.retrofit2:adapter-rxjava:2.3.0'
compile 'io.reactivex:rxandroid:1.2.1'
compile 'io.reactivex:rxjava:1.3.2'
 ```
 如果您的项目也使用了这几个依赖库，可以删掉你的build.gradle中的依赖；或者找其他解决冲突方法
