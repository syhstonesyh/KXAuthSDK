package com.example.testauth;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by syh on 2016/7/1.
 */
public class Messages implements Serializable {

    /**
     * code : 1
     * msg : success
     * encrypt : 1
     * data : ["BlWuVWPiAFxfnLKEHerBKF+bhFpYHMiWm8IBJJH6zuRIHV0NkUjqeQF9ajdS+SiYnBNqNM6EmMABQrXhOp1qFMB0eTDmfFCOMVMi2BIP7SLkvVmQV9jRqIqQU9o8Z+kUG875OyC0K1aXQxhZ0Hm2QhLc5IHlCmwVOUR3HzHGwno=","i4j64SEJM6gE76vPdsNK7xuRjwLjdeXkexCWTwC97z4EU2WnnrcKx4t5a/2EymA8EDES5SUX8hsQlxb73d6XOInS49rw3e1Xuh2+ZF0Jf7phzixyuiiZS0i8GNtuoi7wj8viqEW2Q1D1xramPTSc3tHtaMAKXTFT4tGHAq5S4WA=","kbwIhIn4ExQoFc1xqswcIxEB1+/SZmGNwDQE4585QNP3sOcGvLG7Ut9ITMMQugCfPCTvQhu625EXoSTj3LjrpNNIi2SNj6KR7hnNqbtuuSQ+v4bD0R74WssXCfOzNvUSeOSA4PRw+JB7pLWUHiH93GeA6iyCGD4jC6lzKZIfGLE="]
     * sign : 87a37c633c33a7098b72fb17ecde62ca
     */
    public static final int SUCCESS =1;
    public static final int SIGN_FAIL =-1;
    public static final int ENCRYPT_ENABLE =1;
    private int code;
    private String msg;
    private int encrypt;
    private String sign;
    private String data;
    private String status;
    private String error_code;
    public Messages(){}



    public Messages(int code, String error_code) {
        this.code = code;
        this.error_code = error_code;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        if (!TextUtils.isEmpty(status)&&status.equals("success")){
            code = SUCCESS;
        }
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(int encrypt) {
        this.encrypt = encrypt;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
