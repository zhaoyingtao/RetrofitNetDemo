# RetrofitNetDemo
[ ![Download](https://api.bintray.com/packages/zhaoyingtao/maven/net_library/images/download.svg) ](https://bintray.com/zhaoyingtao/maven/net_library/_latestVersion)  
这是一个基于retrofit的网络请求再度封装；  
功能：  
1、使用更方便，可以自定义，也可以使用默认的，可以更改 baseUrl；  
2、某一个请求长时间无返回值时，取消此请求，判断是否正在请求某一个 api  
3、使用默认 AddCookiesInterceptor 拦截器添加了基本的请求头信息，包含：请求方法、请求来源、版本号、版本名、网络状态、手机系统版本等，也可以参照进行日志收集，基本的需要日志都进行了归类；  
4、包含获取网络状态的功能；   
5、包含数据解析封装功能，使用的gson解析，使用超方便： 
```
//解析数组数据方法：
 List<DataBean> dataList = GsonUtils.init().fromJsonArray("json数据", DataBean.class);
//解析数据对象方法：
 DataBean dataBean = GsonUtils.init().fromJsonObject("json数据", DataBean.class);
```                  

一、在项目中直接引入

将下面的 x.y.z 更改为上面显示的版本号   
`
compile 'com.bintray.library:net_library:x.y.z'
`

二、代码中使用
在项目的 Application的oncreate 方法中初始化：  
```     
        //以下两种任选其一，没有特殊要求可以直接使用第二种
        //第一种：完全自定义网络请求
        RetrofitClientUtil.initClient(this)
                .useCookiesInterceptor(new AddCookiesInterceptor(this))//自定义的Interceptor
                .setConnectionPoolNums(8)//连接池个数
                .setConnectionPoolKeepTime(15)//连接池保活时间
                .setRequestOutTime(20)////请求超时时间
                .setRequestBaseUrl(ApiEnvironmentMgr.getBaseApiUrl())//baseUrl
                .build();
        //第二种：直接使用封装自带的网络请求
        RetrofitClientUtil.initClient(this)
                .setRequestBaseUrl(ApiEnvironmentMgr.getBaseApiUrl())
                .build();
  ```

三、其他代码使用范例  
最好复制项目中的 ApiEnvironmentMgr 类、BaseApiService 类和 BaseAppPresenter 类，可以适当修改；
直接查看项目中的 ApiEnvironmentMgr 类（主要做切换服务器使用）和 BaseApiService 类（存放请求 api) //建议在 Application 中处理，避免多次处理转化

四、取消api的方法  
`RxApiManager.get().cancel("请求api的flag");`  
判断是否正在请求某一个 api  
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
 如果您的项目也使用了这几个依赖库，可以删掉你的 build.gradle 中的依赖；或者找其他解决冲突方法   
 
 感觉对你有用，麻烦给个 star 也算支持了
