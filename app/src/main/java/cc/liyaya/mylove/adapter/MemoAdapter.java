package cc.liyaya.mylove.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import cc.liyaya.mylove.R;
import cc.liyaya.mylove.model.Memo;
import cc.liyaya.mylove.ui.activity.NoteActivity;
import cc.liyaya.mylove.ui.fragment.MemoFragment;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MyHolder> {
    public MemoAdapter(List<Memo> memos, Fragment fragment) {
        this.memos = memos;
        this.fragment = fragment;
    }

    private List<Memo> memos;
    private Fragment fragment;

    @NonNull
    @Override
    public MemoAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoAdapter.MyHolder holder, int position) {
        holder.title.setText(memos.get(position).getTitle());
        holder.date.setText(new SimpleDateFormat("yyyy年M月d日 HH:mm").format(memos.get(position).getDate()));
        holder.noteWhole.setOnClickListener(view -> {
            Intent intent = new Intent(fragment.getContext(), NoteActivity.class);
            intent.putExtra("edit",true);
            intent.putExtra("position",position);
            intent.putExtra("id",memos.get(position).getId());
            intent.putExtra("title",memos.get(position).getTitle());
            intent.putExtra("context",memos.get(position).getContext());
            fragment.startActivityForResult(intent, MemoFragment.EDIT);
        });
    }

    @Override
    public int getItemCount() {
        if (memos == null)
            return 0;
        return memos.size();
    }
    static class MyHolder extends RecyclerView.ViewHolder {

        TextView title,date;
        CardView noteWhole;

        public MyHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recycle_memo_title);
            date = itemView.findViewById(R.id.recycle_memo_date);
            noteWhole = itemView.findViewById(R.id.noet_whole);
        }
    }

    public void insertFirst(Memo memo){
        memos.add(0,memo);
        notifyItemInserted(0);
        notifyDataSetChanged();
//        notifyItemRangeChanged(0, getItemCount());
    }

    public void change(int i,Memo memo){
        memos.set(i, memo);
        notifyItemChanged(i);
    }
    public void delete(int i){
        memos.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, getItemCount());
    }
}
