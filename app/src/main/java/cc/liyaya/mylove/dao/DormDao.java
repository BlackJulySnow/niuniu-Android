package cc.liyaya.mylove.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cc.liyaya.mylove.model.Dorm;

@Dao
public interface DormDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Dorm dorm);

    @Query("SELECT * FROM Dorm")
    List<Dorm> queryAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Dorm> dorms);
}
