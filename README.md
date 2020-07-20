# KXAuthSDK
#### 添加依赖
在 build.gradle 文件中，添加如下依赖即可：
```
dependencies {
     api 'im.kexin.opensdk:libkexinauth:1.0.0'
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
-keep class im.kexin.opensdk.** {
    *;
}
```