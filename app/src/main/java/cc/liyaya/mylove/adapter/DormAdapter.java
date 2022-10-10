package cc.liyaya.mylove.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import cc.liyaya.mylove.MyApplication;
import cc.liyaya.mylove.R;
import cc.liyaya.mylove.constant.MyConstant;
import cc.liyaya.mylove.database.DatabaseUsage;
import cc.liyaya.mylove.model.Dorm;
import cc.liyaya.mylove.tool.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DormAdapter extends RecyclerView.Adapter<DormAdapter.MyHolder> {
    public DormAdapter(List<Dorm> dorms) {
        this.dorms = dorms;
    }

    private List<Dorm> dorms;
    @NonNull
    @Override
    public DormAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dorm,parent,false);
        return new DormAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DormAdapter.MyHolder holder, int position) {
        holder.checkBox.setText(dorms.get(position).getName());
        holder.checkBox.setChecked(dorms.get(position).isSubmit());
        holder.checkBox.setOnClickListener(view -> {
            if (holder.checkBox.isChecked()){
                Toast.makeText(view.getContext(), holder.checkBox.getText() + "完成",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(view.getContext(), holder.checkBox.getText() + "未完成",Toast.LENGTH_SHORT).show();
            }
            dorms.get(position).setSubmit(holder.checkBox.isChecked());
            DatabaseUsage.getInstance(MyApplication.getContext()).dormDao().insert(dorms.get(position));
            HttpUtil.postJson(MyConstant.DORM_UPDATE, new Gson().toJson(dorms.get(position)), new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        if (dorms == null)
            return 0;
        return dorms.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        AppCompatCheckBox checkBox;

        public MyHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.dorm_checkbox);
        }
    }
}
