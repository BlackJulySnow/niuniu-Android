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
import cc.liyaya.mylove.dao.MemoDao;
import cc.liyaya.mylove.database.DatabaseUsage;
import cc.liyaya.mylove.model.Memo;
import cc.liyaya.mylove.tool.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MemoThread implements Runnable{
    private String TAG = "MemoThread";
    private MemoDao memoDao;

    @Override
    public void run() {
        memoDao = DatabaseUsage.getInstance(MyApplication.getContext()).memoDao();
        HttpUtil.post(MyConstant.MEMO_QUERY, new HashMap<>(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG,"QueryError");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str = response.body().string();
                Log.i(TAG,str);
                List<Memo> changedMemos = memoDao.getChanged();
                for (Memo memo : changedMemos){//上传修改过的
                    Log.e(TAG,new Gson().toJson(memo));
                    HttpUtil.postJson(MyConstant.MEMO_INSERT, new Gson().toJson(memo));
                    memo.setChanged(false);
                    memoDao.insert(memo);
                }
                List<Memo> remoteMemos = new Gson().fromJson(str, new TypeToken<List<Memo>>(){}.getType());
                List<Memo> localMemos = memoDao.getAll();
                boolean flags[] = new boolean[localMemos.size()];
                for (Memo remoteMemo : remoteMemos){
                    Memo localMemo = memoDao.findById(remoteMemo.getId());
                    if (localMemo == null){
                        memoDao.insert(remoteMemo);
                    }else{
                        flags[localMemos.indexOf(localMemo)] = true;
                        if (remoteMemo.getDate() > localMemo.getDate()){//远程文件比较新
                            memoDao.update(remoteMemo);
                    }else if (remoteMemo.getDate() < localMemo.getDate()){//本地文件比较新
                            HttpUtil.postJson(MyConstant.MEMO_INSERT,new Gson().toJson(localMemo));
                        }
                    }
                }
                for (int i = 0;i < flags.length;i++){
                    if (!flags[i]){
                        long id = localMemos.get(i).getId();
                        localMemos.get(i).setId(0);
                        Log.e(TAG,new Gson().toJson(localMemos.get(i)));
                        long newID = Long.parseLong(HttpUtil.postJson(MyConstant.MEMO_INSERT,new Gson().toJson(localMemos.get(i))));
                        memoDao.deleteById(id);//删除老id
                        localMemos.get(i).setId(newID);
                        memoDao.insert(localMemos.get(i));
                    }
                }
            }
        });
        memoDao.getAll();
    }
}
