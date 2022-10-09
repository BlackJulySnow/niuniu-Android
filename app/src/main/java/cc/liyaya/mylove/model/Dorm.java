package cc.liyaya.mylove.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Dorm {
    @PrimaryKey(autoGenerate = true)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }

    private String name;
    private boolean submit;

}
