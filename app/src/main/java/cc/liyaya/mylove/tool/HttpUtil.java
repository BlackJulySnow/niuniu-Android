package cc.liyaya.mylove.tool;

import java.io.IOException;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

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
}
