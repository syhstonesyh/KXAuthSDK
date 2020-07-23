package com.example.testauth.http.okhttps;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttpMange管理类
 */

public class OkHttpManager {
    private static OkHttpManager mInstance;
    private OkHttpClient mClient;
    private Handler mHnadler;
    private Gson mGson;
    private static final long cacheSize = 1024 * 1024 * 10;// 缓存文件最大限制大小20M
    //private static String cacheDirectory = Environment.getExternalStorageDirectory() + "/okttpcaches"; // 设置缓存文件路径
   // private static Cache cache = new Cache(new File(cacheDirectory), cacheSize);  //

    /**
     * 单例
     *
     * @return
     */
    public static synchronized OkHttpManager getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpManager();
        }
        return mInstance;
    }

    public Handler getmHnadler() {
        return mHnadler;
    }

    /**
     * 构造函数
     */
    private OkHttpManager() {
        initOkHttp();
        mHnadler = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    /**
     * 初始化OkHttpClient
     */
    private void initOkHttp() {
        /*mClient = new OkHttpClient().newBuilder()
                .readTimeout(10000, TimeUnit.SECONDS)
                .connectTimeout(30000, TimeUnit.SECONDS)
                .writeTimeout(30000, TimeUnit.SECONDS)
                .build();*/


        mClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketFactoryImp.createSSLSocketFactory())
                .hostnameVerifier(new SSLSocketFactoryImp.TrustAllHostnameVerifier())
                .build();
    }
    private static Map<Object, List<Call>> callList = new HashMap<>();
    private Object activity;
    public OkHttpManager tagActivity(Object activity){
        if (activity!=null){
            this.activity=activity;
        }
        return mInstance;
    }

    private static final String TAG = "OkHttpManager";
    /**
     * 请求
     *
     * @param client
     * @param callBack
     */
    public void request(BaseOkHttpClient client, final BaseCallBack callBack) {

        if (callBack == null) {
            throw new NullPointerException(" callback is null");
        }
        Call call = mClient.newCall(client.buildRequest());

        if (activity!=null){
            if (callList.get(activity)==null){
                callList.put(activity, Collections.synchronizedList(new ArrayList<Call>()));
            }
            List<Call> tmp =  callList.get(activity);
            tmp.add(call);
            callList.put(activity,tmp);
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendonFailureMessage(callBack, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (callBack.mType == null || callBack.mType == String.class) {
                        sendonSuccessMessage(callBack, result);
                    } else {
                        sendonSuccessMessage(callBack, mGson.fromJson(result, callBack.mType));
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                } else {
                    String str = "";
                    ResponseBody responseBody = response.body();
                    String s = responseBody.string();
                    if (responseBody!=null&& !TextUtils.isEmpty(s)){
                        str=s;
                    }
                    sendonErrorMessage(callBack, response.code(),str);
                }
            }
        });
    }
    MultipartBody.Builder mbBuilder;
    public OkHttpManager createBody(){
        mbBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        return this;
    }
    public OkHttpManager file(String key, List<String> paths){
        for (String path : paths){
            file(key,path);
        }
        return this;
    }
    public OkHttpManager file(String key, String path){
        if (TextUtils.isEmpty(key)){
            key = "file";
        }
        File file = new File(path);
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
        mbBuilder.addFormDataPart(key, file.getName(), image);
        return this;
    }
    public OkHttpManager param(String key, String value){
        mbBuilder.addFormDataPart(key,value);
        return this;
    }
    public void uploadImage(String urls, final BaseCallBack callBack){

        Request request = new Request.Builder()
                .url(urls)
                .post(mbBuilder.build())
                .build();
        Call call = mClient.newCall(request);
        if (activity!=null){
            if (callList.get(activity)==null){
                callList.put(activity,new ArrayList<Call>());
            }
            List<Call> tmp = callList.get(activity);
            tmp.add(call);
            callList.put(activity,tmp);
        }

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendonFailureMessage(callBack, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (callBack.mType == null || callBack.mType == String.class) {
                        sendonSuccessMessage(callBack, result);
                    } else {
                        sendonSuccessMessage(callBack, mGson.fromJson(result, callBack.mType));
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                } else {
                    String str = "";
                    ResponseBody responseBody = response.body();
                    String s = responseBody.string();
                    if (responseBody!=null&& !TextUtils.isEmpty(s)){
                        str=s;
                    }
                    sendonErrorMessage(callBack, response.code(),str);
                }
            }
        });
    }
    /**
     * 成功信息
     *
     * @param callBack
     * @param result
     */
    private void sendonSuccessMessage(final BaseCallBack callBack, final Object result) {
        mHnadler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(result);
            }
        });
    }

    /**
     * 失败信息
     *
     * @param callBack
     * @param call
     * @param e
     */
    private void sendonFailureMessage(final BaseCallBack callBack, final Call call, final IOException e) {
        mHnadler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onFailure(call, e);
            }
        });
    }

    /**
     * 错误信息
     *
     * @param callBack
     * @param code
     */
    private void sendonErrorMessage(final BaseCallBack callBack, final int code, final String str) {
        mHnadler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(code,str);
            }
        });
    }
    public void cancle(){
        callList.clear();
        mClient.dispatcher().cancelAll();
    }
    public void cancleTag(Object tags){
        if (tags!=null){
            List<Call> tmp = callList.get(tags);
            if (tmp!=null){
                for (int i=0;i<tmp.size();i++){
                    tmp.get(i).cancel();
                }
                callList.remove(tags);
            }
        }

    }

}