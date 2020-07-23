# KXAuthSDK
#### 准备工作
移动应用可信登录是基于OAuth2.0 协议标准构建的可信 OAuth2.0 授权登录系统。

在进行可信 OAuth2.0 授权登录接入之前，在可信开放平台注册开发者帐号，并拥有一个已审核通过的移动应用，并获得相应的 AppID 和 AppSecret，
申请可信登录且通过审核后，可开始接入流程。
```
1、目前移动应用上可信登录只提供原生的登录方式，需要用户安装可信客户端才能配合使用。

2、对于Android应用，建议总是显示可信登录按钮，当用户手机没有安装可信客户端时，请引导用户下载安装可信客户端。

3、对于iOS应用，考虑到iOS应用商店审核指南中的相关规定，建议开发者接入可信登录时，先检测用户手机是否已安装
可信客户端（使用sdk中isWXAppInstalled函数 ），对未安装的用户隐藏可信登录按钮，只提供其他登录
方式（比如手机号注册登录、游客登录等）。
```

#### 授权流程说明
可信 OAuth2.0 授权登录让可信用户使用可信身份安全登录第三方应用或网站，在可信用户授权登录已接入可信 OAuth2.0 的第三方应用后，
第三方可以获取到用户的接口调用凭证（access_token），通过 access_token 可以进行可信开放平台授权关系接口调用，
从而可实现获取可信用户基本开放信息和帮助用户实现基础开放功能等。

可信 OAuth2.0 授权登录目前支持 authorization_code 模式，适用于拥有 server 端的应用授权。该模式整体流程为：
```
1. 第三方发起可信授权登录请求，可信用户允许授权第三方应用后，可信会拉起应用或重定向到第三方网站，并且带上授权临时票据code参数；

2. 通过code参数加上AppID和AppSecret等，通过API换取access_token；

3. 通过access_token进行接口调用，获取用户基本数据资源或帮助用户实现基本操作。
```
  [授权接口详情请查看官方文档](https://developers.weixin.qq.com/doc/oplatform/Mobile_App/WeChat_Login/Development_Guide.html)
#### 添加依赖
在 build.gradle 文件中，添加如下依赖即可：
```
dependencies {
    //libkexinauth 依赖与fastjson
    implementation "com.alibaba:fastjson:1.2.58"
    implementation "com.alibaba:fastjson:1.1.71.android"
    //添加依赖
    api 'im.kexin.opensdk:libkexinauth:1.0.1'
}
```
#### 初始化
```
KXAuthClient.getInstance().init(context,"your appId");
```
#### 授权登录
```
KXAuthClient.getInstance().auth(this, new KXAuthClient.OnKXAuthListener() {
            @Override
            public void onComplete(String jsonInfo) {
                Log.e("KXAuthClient", "onComplete: "+jsonInfo );
            }

            @Override
            public void onError(int code, String error) {
                Log.e("KXAuthClient", "onError: "+code+","+error );
            }

            @Override
            public void onCancle() {
                Log.e("KXAuthClient", "onCancle: " );
            }
        });
```


#### 混淆代码
如果需要混淆代码，为了保证 sdk 的正常使用，需要混淆配置：
```
-keep class im.kexin.opensdk.client.** {
    *;
}
```