package cc.liyaya.mylove.thread;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import cc.liyaya.mylove.MyApplication;
import cc.liyaya.mylove.constant.MyConstant;
import cc.liyaya.mylove.dao.ClassDao;
import cc.liyaya.mylove.database.DatabaseUsage;
import cc.liyaya.mylove.model.Class;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClassThread implements Runnable{
    private String TAG = "ClassThread";
    @Override
    public void run() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(MyConstant.GetClassURL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG,"查询课表失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    ClassDao classDao = DatabaseUsage.getInstance(MyApplication.getContext()).classDao();
                    List<Class> list = new Gson().fromJson(response.body().string(),new TypeToken<List<Class>>(){}.getType());
                    int i = 0;
                    for (Class cls : list){
                        Class res = classDao.getByDateAndNum(cls.getDate(),cls.getNum());
                        if (res == null){
                            classDao.insert(cls);
                            i = i + 1;
//                            insertDate.add(cls);
                        }else{
                            cls.setId(res.getId());
                            classDao.update(cls);
                        }
                    }
                    Log.e(TAG, i + "/" + list.size());
                }catch (IOException e){
                    Log.e(TAG,"储存课表失败");
                }

            }
        });
    }
}
