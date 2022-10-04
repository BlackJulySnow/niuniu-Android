package cc.liyaya.mylove.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cc.liyaya.mylove.model.Class;

@Dao
public interface ClassDao {

    @Query("SELECT * FROM class")
    List<Class> getAll();

    @Delete
    void delete(Class clazz);

    @Insert
    void insert(Class... classes);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Class clazz);

    @Update
    void update(Class device);

    @Query("SELECT COUNT(*) FROM class WHERE date = :date and num = :num")
    int countByDateAndNum(long date,long num);
    @Query("SELECT * FROM class WHERE date = :date and num = :num")
    Class getByDateAndNum(long date,long num);
    @Query("SELECT * FROM class WHERE date = :date")
    List<Class> queryByDate(long date);
}
