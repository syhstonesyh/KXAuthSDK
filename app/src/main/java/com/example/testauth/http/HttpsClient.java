package com.example.testauth.http;

import android.os.Handler;
import android.util.Log;

import com.example.testauth.http.okhttps.BaseCallBack;
import com.example.testauth.http.okhttps.BaseOkHttpClient;
import com.example.testauth.http.okhttps.OkHttpManager;
import com.example.testauth.http.okhttps.OnResultListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Http请求的工具类
 *
 * @author 石云鹤
 */
public class HttpsClient {
    public static final int ERROR_CODE_CANCLE = -999;//取消
    public static final int ERROR_CODE_NET_NoConnect = -200;//网络未连接
    public static final int ERROR_CODE_FAIL = -100;//失败
    public static final String TAG = "HttpsClient";
    private Object tag;
    private String url;
    public boolean needUData = false;//是否返回原数据
    public boolean initExtra = true;//是否添加公共字段
    private BaseOkHttpClient.Builder builder;
    private int requestCode = 0;

    Map<String, Object> map = new HashMap<>();

    public HttpsClient(Object tag) {
        this.tag = tag;
        builder = BaseOkHttpClient.newBuilder();
    }

    public HttpsClient() {
        builder = BaseOkHttpClient.newBuilder();
    }

    public HttpsClient url(String u) {
        this.url = u;
        if (tag == null) {
            tag = url;
        }
        builder.url(this.url);
        return this;
    }

    public HttpsClient requestCode(int u) {
        this.requestCode = u;
        return this;
    }

    public HttpsClient param(String k, Object v) {
        builder.addParam(k, v);
        map.put("" + k, "" + v);
        return this;
    }

    public HttpsClient param(Map<String, Object> m) {
        map = m;
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String mapKey = entry.getKey();
                Object mapValue = entry.getValue();
                builder.addParam(mapKey, mapValue);
            }
        }
        return this;
    }

    public HttpsClient get() {
        builder.get();
        return this;
    }

    public HttpsClient post() {
        builder.post();
        if (initExtra) {
            initExtra();
        }
        return this;
    }

    public HttpsClient form() {
        builder.form();
        if (initExtra) {
            initExtra();
        }
        return this;
    }

    public HttpsClient udata(boolean b) {
        needUData = b;
        return this;
    }

    public HttpsClient extra(boolean b) {
        initExtra = b;
        return this;
    }

    public void request(final OnResultListener listener) {
//        FileLog.e(TAG+"请求地址："+url);
//        FileLog.e(TAG+"请求参数："+params);
        OkHttpManager.getInstance().tagActivity(tag).request(builder.build(), new OnBaseCallBack(listener, url, needUData));
    }

    private Handler mHnadler;

    public void initExtra() {
        //param("version", AppUtils.getVersionName(BaseLibUtils.getContext()));
    }

    public static class OnBaseCallBack extends BaseCallBack {
        OnResultListener listener;
        String url;
        boolean needUDatas;

        public OnBaseCallBack(OnResultListener listener, String str) {
            this(listener, str, false);
        }

        public OnBaseCallBack(OnResultListener listener, String str, boolean b) {
            this.listener = listener;
            url = str;
            needUDatas = b;
        }

        @Override
        public void onSuccess(Object o) {

            String str = o.toString();
            Log.e(TAG, url+","+"onSuccess: " + str);
            if (listener!=null){
                listener.onSuccess(str);
            }
        }

        @Override
        public void onError(int code, String str) {
            Log.e(TAG, url+","+"onError: "+str );
            if (listener!=null){
                listener.onFailure(str,code);
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, url+","+"onFailure: "+e.getMessage() );
            if (listener!=null){
                listener.onFailure("error",-1);
            }
        }
    }

}
