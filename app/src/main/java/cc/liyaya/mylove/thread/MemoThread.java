package cc.liyaya.mylove.thread;

import android.util.Log;

import java.io.IOException;

import cc.liyaya.mylove.constant.MyConstant;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MemoThread implements Runnable{
    private String TAG = "MemoThread";
    @Override
    public void run() {
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("token", MyConstant.PushToken)
                .add("sss", "123")
                .build();
        Request request = new Request.Builder()
                .url("http://10.0.38.246:8080/schedule/pushOne")
                .post(requestBody)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            String t = response.body().string();
            Log.e(TAG, String.valueOf(t.length()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
