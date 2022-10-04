package cc.liyaya.mylove.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.text.SimpleDateFormat;
import java.util.Date;
import cc.liyaya.mylove.R;
import cc.liyaya.mylove.databinding.ActivityNoteBinding;

public class NoteActivity extends AppCompatActivity {
    public final static int NOTE_OK = 1;
    public final static int NOTE_TRASH = 2;
    private ActivityNoteBinding binding;
    private boolean edit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbarNote;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.noteDate.setText(new SimpleDateFormat("yyyy年M月d日").format(new Date()));
        Intent last = getIntent();
        edit = last.getBooleanExtra("edit",false);
        if (edit){
            binding.noteTitle.setText(last.getStringExtra("title"));
            binding.noteContext.setText(last.getStringExtra("context"));
        }
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("position",last.getIntExtra("position",-1));
            intent.putExtra("title",binding.noteTitle.getText().toString());
            intent.putExtra("context",binding.noteContext.getText().toString());
            intent.putExtra("id",last.getLongExtra("id",-1));
            setResult(NOTE_OK,intent);
            finish();
        });
//        getSupportActionBar().hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent last = getIntent();
        Intent intent = new Intent();
        intent.putExtra("position",last.getIntExtra("position",-1));
        switch (item.getItemId()){
            case R.id.note_menu_ok:
                intent.putExtra("title",binding.noteTitle.getText().toString());
                intent.putExtra("context",binding.noteContext.getText().toString());
                intent.putExtra("id",last.getLongExtra("id",-1));
                setResult(NOTE_OK,intent);
                finish();
                return true;
            case R.id.note_menu_trash:
                intent.putExtra("id",last.getLongExtra("id",-1));
                Toast.makeText(getApplicationContext(),"123",Toast.LENGTH_SHORT).show();
                setResult(NOTE_TRASH,intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
