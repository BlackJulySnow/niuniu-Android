package cc.liyaya.mylove;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.huawei.hms.push.HmsMessageService;

import java.io.IOException;

import cc.liyaya.mylove.constant.MyConstant;
import cc.liyaya.mylove.tool.WeatherTool;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyHmsMessageService extends HmsMessageService {
    private String TAG = "MyHmsMessageService";
    @Override
    public void onNewToken(String token, Bundle bundle) {
        // 获取token
        Log.i(TAG, "have received refresh token " + token);

        // 判断token是否为空
        if (!TextUtils.isEmpty(token)) {
            refreshedTokenToServer(token);
        }
    }

    private void refreshedTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
        MyConstant.PushToken = token;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(MyConstant.TokenURL)
                .post(new FormBody.Builder()
                        .add("token",token)
                        .add("location", WeatherTool.Location2String(WeatherTool.getLocation()))
                        .build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG,"提交失败");
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e(TAG,response.body().string());
            }
        });
    }
}