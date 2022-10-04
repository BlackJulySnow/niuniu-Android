package cc.liyaya.mylove.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import cc.liyaya.mylove.R;
import cc.liyaya.mylove.databinding.FragmentCenterBinding;
import cc.liyaya.mylove.ui.activity.UserInfoActivity;

public class CenterFragment extends Fragment {

    private FragmentCenterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCenterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initMenu();
        binding.centerCardviewInfo.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), UserInfoActivity.class);
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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