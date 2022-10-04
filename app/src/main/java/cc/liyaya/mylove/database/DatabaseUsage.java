package cc.liyaya.mylove.database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationSpec;

import cc.liyaya.mylove.dao.ClassDao;
import cc.liyaya.mylove.dao.MemoDao;
import cc.liyaya.mylove.dao.WeatherDao;
import cc.liyaya.mylove.model.Class;
import cc.liyaya.mylove.model.Memo;
import cc.liyaya.mylove.model.Weather;

@Database(entities = {Class.class, Weather.class, Memo.class}, version = 1)
public abstract class DatabaseUsage extends RoomDatabase {
    public abstract ClassDao classDao();
    public abstract WeatherDao weatherDao();
    public abstract MemoDao memoDao();
    private static final String DB_NAME = "NiuNiu.db";
    private static volatile DatabaseUsage instance;

    public static synchronized DatabaseUsage getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }
    private static DatabaseUsage create(final Context context) {
        return Room.databaseBuilder(
                context,
                DatabaseUsage.class,
                DB_NAME)
                .allowMainThreadQueries()
                .build();
    }
}
