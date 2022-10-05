package cc.liyaya.mylove.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cc.liyaya.mylove.model.Class;
import cc.liyaya.mylove.model.Memo;


@Dao
public interface MemoDao {
    @Query("SELECT * FROM Memo")
    List<Memo> getAll();

    @Query("SELECT * FROM Memo ORDER BY date DESC")
    List<Memo> getAllSortByDate();

    @Query("SELECT * FROM Memo WHERE deleted = 0 ORDER BY date DESC")
    List<Memo> getAllSortByDateNotDelete();

    @Query("DELETE FROM Memo WHERE id = :id")
    void deleteById(long id);

    @Insert
    void insert(Memo... memos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Memo memo);

    @Update
    void update(Memo memo);

    @Query("SELECT * FROM Memo WHERE id = :id")
    Memo findById(long id);

    @Query("UPDATE Memo SET deleted = 1 WHERE id = :id")
    void updateDeletedById(long id);

    @Query("SELECT * FROM Memo WHERE changed = 1")
    List<Memo> getChanged();
}
