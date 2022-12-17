package cc.liyaya.mylove.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import cc.liyaya.mylove.adapter.MemoAdapter;
import cc.liyaya.mylove.constant.MyConstant;
import cc.liyaya.mylove.dao.MemoDao;
import cc.liyaya.mylove.database.DatabaseUsage;
import cc.liyaya.mylove.databinding.FragmentMemoBinding;
import cc.liyaya.mylove.model.Memo;
import cc.liyaya.mylove.tool.HttpUtil;
import cc.liyaya.mylove.ui.activity.NoteActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MemoFragment extends Fragment {

    private FragmentMemoBinding binding;
    private String TAG = "DashboardFragment";
    private MemoAdapter memoAdapter;
    public static final int EDIT = 1;
    public static final int ADD = 2;
    private MemoDao memoDao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMemoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initMenu();
        memoDao = DatabaseUsage.getInstance(getContext()).memoDao();
        initRecycleView();

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), NoteActivity.class);
            startActivityForResult(intent,ADD);
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        int position = data.getIntExtra("position",-1);
        if (resultCode == NoteActivity.NOTE_OK){
            String title = data.getStringExtra("title");
            String context = data.getStringExtra("context");
            Memo memo;
            switch (requestCode){
                case ADD:
                    memo = new Memo();
                    memo.setContext(context);
                    memo.setTitle(title);
                    memo.setDeleted(false);
                    memo.setChanged(false);
                    memo.setDate(System.currentTimeMillis());
                    HttpUtil.postJson(MyConstant.MEMO_INSERT, new Gson().toJson(memo), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            memoDao.insert(memo);//没网络自动生成
                        }
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            long id = Long.parseLong(response.body().string());
                            memoDao.deleteById(memo.getId());
                            memo.setId(id);
                            memoDao.insert(memo);
                        }
                    });
                    memoAdapter.insertFirst(memo);
                    break;
                case EDIT:
                    long id = data.getLongExtra("id",-1);
                    memo = memoDao.findById(id);
                    memo.setTitle(title);
                    memo.setContext(context);
                    memo.setDate(System.currentTimeMillis());
                    HttpUtil.postJson(MyConstant.MEMO_INSERT, new Gson().toJson(memo), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {//没网络标记为修改过的
                            memo.setChanged(true);
                            memoDao.update(memo);
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            memo.setChanged(false);
                            memoDao.update(memo);
                        }
                    });
                    memoAdapter.change(position, memo);
                    break;
            }
        }else if(resultCode == NoteActivity.NOTE_TRASH){
            long id = data.getLongExtra("id",-1);
            if (id != -1){
                Memo memo = memoDao.findById(id);
                memo.setDate(System.currentTimeMillis());//改变为最新的值
                memo.setDeleted(true);
                memoDao.update(memo);
                HttpUtil.postJson(MyConstant.MEMO_INSERT, new Gson().toJson(memo), new Callback() {//删除的时候没有网络
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        memo.setChanged(false);
                        memoDao.update(memo);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        memo.setChanged(true);
                        memoDao.update(memo);
                    }
                });
                memoAdapter.delete(position);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        memoDao = null;
        memoAdapter = null;
    }
    public void initRecycleView(){
        RecyclerView recyclerView = binding.recycle;
        List<Memo> memos = memoDao.getAllSortByDateNotDelete();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        memoAdapter = new MemoAdapter(memos,this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(memoAdapter);
    }
    public void initMenu(){
        getActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
    }
}