package cc.liyaya.mylove.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Memo {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    private long date;
    private String context;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    @ColumnInfo(defaultValue = "0")
    private boolean deleted;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Memo memo = (Memo) o;
//        return id == memo.id && date == memo.date && deleted == memo.deleted && title.equals(memo.title) && context.equals(memo.context);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, title, date, context, deleted);
//    }

        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Memo memo = (Memo) o;
        return id == memo.id && date == memo.date && deleted == memo.deleted && changed == memo.changed && title.equals(memo.title) && context.equals(memo.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, date, context, deleted, changed);
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
    @ColumnInfo(defaultValue = "0")
    private boolean changed;
}
