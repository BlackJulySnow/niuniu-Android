package cc.liyaya.mylove.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import cc.liyaya.mylove.R;
import cc.liyaya.mylove.adapter.MemoAdapter;
import cc.liyaya.mylove.dao.MemoDao;
import cc.liyaya.mylove.database.DatabaseUsage;
import cc.liyaya.mylove.databinding.FragmentMemoBinding;
import cc.liyaya.mylove.model.Memo;
import cc.liyaya.mylove.ui.activity.NoteActivity;

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
            if (requestCode == ADD){
                Memo memo = new Memo();
                memo.setContext(context);
                memo.setTitle(title);
                memo.setDeleted(false);
                memo.setDate(System.currentTimeMillis());
                memo.setId(memoDao.insert(memo));
                memoAdapter.insertFirst(memo);
            }else if (resultCode == EDIT){
                long id = data.getLongExtra("id",-1);
                Memo memo = memoDao.findById(id);
                memo.setTitle(title);
                memo.setContext(context);
                memo.setDate(System.currentTimeMillis());
                memoDao.update(memo);
                memoAdapter.change(position, memo);
            }
        }else if(resultCode == NoteActivity.NOTE_TRASH){
            long id = data.getLongExtra("id",-1);
            if (id != -1){
                memoDao.updateDeletedById(id);
                memoAdapter.delete(position);
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void initRecycleView(){
        RecyclerView recyclerView = binding.recycle;
        List<Memo> memos = DatabaseUsage.getInstance(getContext()).memoDao().getAllSortByDateNotDelete();
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
//                menuInflater.inflate(R.menu.class_table_menu,menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
    }
}