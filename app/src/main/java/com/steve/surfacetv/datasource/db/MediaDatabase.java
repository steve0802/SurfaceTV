package com.steve.surfacetv.datasource.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.steve.surfacetv.datasource.db.dao.DramaDao;
import com.steve.surfacetv.datasource.db.entity.DramaPo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DramaPo.class}, version = 1)
public abstract class MediaDatabase extends RoomDatabase {
    public static ExecutorService dbExecutorService = Executors.newFixedThreadPool(1);
    private static final String DB_NAME = "Media.db";//資料庫名稱
    private static volatile MediaDatabase instance;

    public abstract DramaDao getDramaDao();

    public static synchronized MediaDatabase getInstance(Context context){
        if(MediaDatabase.instance == null) {
            //創立資料庫
            MediaDatabase.instance = Room.databaseBuilder(context, MediaDatabase.class, DB_NAME)
                    .build();
        }

        return MediaDatabase.instance;
    }
}
