package com.example.testauth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import im.kexin.opensdk.KXAuthClient;


public class MainActivity extends AppCompatActivity {
    TextView tv_msg;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_msg = findViewById(R.id.tv_msg);
        KXAuthClient.getInstance().init(this,"a123456");
    }

    public void getInfo(View view) {
        tv_msg.setText("");
        KXAuthClient.getInstance().auth(this, new KXAuthClient.OnKXAuthListener() {
            @Override
            public void onComplete(String jsonInfo) {
                Log.e(TAG, "onComplete: "+jsonInfo );
                tv_msg.setText("onComplete: "+jsonInfo);
            }

            @Override
            public void onError(int code, String error) {
                Log.e(TAG, "onError: "+code+","+error );
                tv_msg.setText("onError: "+code+","+error);
            }

            @Override
            public void onCancle() {
                Log.e(TAG, "onCancle: " );
                tv_msg.setText("onCancle: " );
            }
        });
    }


}
