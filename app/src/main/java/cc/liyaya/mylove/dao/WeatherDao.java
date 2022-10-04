package cc.liyaya.mylove.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import cc.liyaya.mylove.model.Weather;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM weather")
    List<Weather> getAll();

    @Delete
    void delete(Weather device);

    @Insert
    void insert(Weather... devices);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Weather device);

    @Update
    void update(Weather device);

    @Query("SELECT * FROM weather WHERE date <= :date")
    List<Weather> getRecent(long date);

    @Query("SELECT * FROM weather WHERE date = :data")
    Weather get(long data);
}
