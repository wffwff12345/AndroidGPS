package com.example.location.http;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.location.model.Result;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Http 工具类
 */
public class HttpUtils {
    public static final int ERROR_NORMAL = 100;
    public static final String AUTHORIZATION = "Authorization";
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private static OkHttpClient client;
    private static HttpUtils httpUtils;
    private String auth = "BearereyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjU3MmMwNDM5LTg2ZDItNGRkNi04NWU5LTIzMWI5OGUzMGE4NSJ9.SKy57OOm7tXJG2Hu7vAtzadj81XxT88q1GrptGaQTqz4hkGbkqcqg8dPzZl3uaMhKbWf187pgs0v30iLJsbTTg";

    private static OkHttpClient HttpClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }

    public static HttpUtils Instance() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    public void postJson(String url, Object object, OnHttpCallback callback, int id, Context context){
        String json = JsonUtils.toJson(object);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().header(AUTHORIZATION,auth).url(url).post(requestBody).build();
        HttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e, id, ERROR_NORMAL);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                Result result = JsonUtils.toBean(response.body().string(), Result.class);
                callback.onResponse(result, id);
            }
        });
    }

}