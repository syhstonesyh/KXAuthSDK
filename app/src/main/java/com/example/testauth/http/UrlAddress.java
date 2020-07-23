package com.example.testauth.http;

public class UrlAddress {
    // 测试服务器
    //public static final String BASE_URL = "https://47.101.186.93/";
    // 本地服务器
    //public static final String BASE_URL_LOCAL = "https://192.168.0.107:8080/";
    // 线网环境
    public static final String BASE_URL =  "http://192.168.0.112:9200/" ;
    public static final String GET_TOKEN = BASE_URL+"oauth2/access_token";
    public static final String GET_USERINFO = BASE_URL+"oauth2/userinfo";
}
