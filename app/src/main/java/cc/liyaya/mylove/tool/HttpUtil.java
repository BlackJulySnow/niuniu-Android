package cc.liyaya.mylove.tool;

import java.io.IOException;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    public static String post(String url, Map<String, String> map) throws IOException {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder body = new FormBody.Builder();
        for (Map.Entry<String,String> item : map.entrySet()){
            body.add(item.getKey(),item.getValue());
        }
        Request request = new Request.Builder()
                .url(url)
                .post(body.build())
                .build();
        return client.newCall(request).execute().body().toString();
    }
    public static void post(String url, Map<String, String> map, Callback callback){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder body = new FormBody.Builder();
        for (Map.Entry<String,String> item : map.entrySet()){
            body.add(item.getKey(),item.getValue());
        }
        Request request = new Request.Builder()
                .url(url)
                .post(body.build())
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static String postJson(String url, String json){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, MediaType.get("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response;
        String res = null;
        try {
            response = client.newCall(request).execute();
            res = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (res != null){
            return res;
        }else
            return "";
    }

    public static void postJson(String url, String json,Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, MediaType.get("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
