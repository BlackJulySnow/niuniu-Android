package cc.liyaya.mylove.thread;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import cc.liyaya.mylove.MyApplication;
import cc.liyaya.mylove.constant.MyConstant;
import cc.liyaya.mylove.database.DatabaseUsage;
import cc.liyaya.mylove.model.Dorm;
import cc.liyaya.mylove.model.Memo;
import cc.liyaya.mylove.tool.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DormThread implements Runnable{
    private String TAG = "DormThread";
    @Override
    public void run() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(MyConstant.DORM_QUERY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                Gson gson = new Gson();
                List<Dorm> dorms = gson.fromJson(res,new TypeToken<List<Dorm>>(){}.getType());
                DatabaseUsage.getInstance(MyApplication.getContext()).dormDao().insert(dorms);
                Log.e(TAG,res);
            }
        });
    }
}
