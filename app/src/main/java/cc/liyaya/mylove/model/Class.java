package cc.liyaya.mylove.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Class {


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long date;
    private String place;
    private String name;
    private String classroom;
    private int num;
}
