package com.example.testauth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.testauth.http.HttpsClient;
import com.example.testauth.http.UrlAddress;
import com.example.testauth.http.okhttps.OnResultListener;

import im.kexin.opensdk.client.KXAuthClient;


public class MainActivity extends AppCompatActivity {
    TextView tv_msg;
    private static final String TAG = "MainActivity";
    private static final String secret = "kx2344235";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_msg = findViewById(R.id.tv_msg);
        KXAuthClient.getInstance().init(this,"100004");
    }

    public void getInfo(View view) {
        tv_msg.setText("");
        KXAuthClient.getInstance().auth(this, new KXAuthClient.OnKXAuthListener() {
            @Override
            public void onComplete(int action, String jsonInfo) {

                JSONObject jsonObject = JSON.parseObject(jsonInfo);
                Log.e(TAG, "onComplete: "+jsonInfo );
                tv_msg.setText("getCodeSuccess: "+jsonInfo);
                getToken(jsonObject.getString("code"));
            }

            @Override
            public void onError(int action, int code, String error) {

                Log.e(TAG, "onError: "+code+","+error );
                tv_msg.setText("getCodeError: "+code+","+error);
            }

            @Override
            public void onCancle(int action) {

                Log.e(TAG, "onCancle: " );
                tv_msg.setText("getCodeCancle: " );
            }

        });
    }

    private void getToken(String code){
        //http://127.0.0.1:9200/oauth2/access_token?code=123&grant_type=authorization_code&appid=12345654&secret=dfgtyuiop
        new HttpsClient()
                .url(UrlAddress.GET_TOKEN+"?code="+code+"&grant_type=authorization_code&appid="+ KXAuthClient.getInstance().getAppId()+"&secret="+secret)
                .get()
                .request(new OnResultListener(){
                    @Override
                    public void onSuccess(String str) {
                        Log.e(TAG, "onSuccess: "+str );
                        String msg = "getTokeSuccess:"+str;
                        JSONObject jsonObject = JSON.parseObject(str);

                        JSONObject data = jsonObject.getJSONObject("data");
                        tv_msg.setText(tv_msg.getText().toString()+"\n\n"+msg);
                        getUserInfo(data.getString("token"),data.getString("openid"));
                    }

                    @Override
                    public void onFailure(String str, int errorCode) {
                        Log.e(TAG, "onFailure: "+str +","+errorCode);
                        String msg = "getTokeFailure:"+str;
                        tv_msg.setText(tv_msg.getText().toString()+"\n\n"+msg);
                    }
                });
    }

    private void getUserInfo(String token,String openid){
        new HttpsClient()
                .url(UrlAddress.GET_USERINFO+"?access_token="+token+"&open_id="+openid)
                .get().request(new OnResultListener(){
            @Override
            public void onSuccess(String str) {
                Log.e(TAG, "getUserInfoSuccess: "+str );
                tv_msg.setText(tv_msg.getText().toString()+"\n\n"+str);
            }

            @Override
            public void onFailure(String str, int errorCode) {

                Log.e(TAG, "onFailure: "+str +","+errorCode);
                String msg = "getUserInfoFail:"+str;
                tv_msg.setText(tv_msg.getText().toString()+"\n\n"+msg);
            }
        });
        //
    }

}
