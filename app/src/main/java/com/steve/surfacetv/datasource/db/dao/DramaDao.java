package com.steve.surfacetv.datasource.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.steve.surfacetv.datasource.db.entity.DramaPo;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface DramaDao {
    String TABLE_NAME = "Drama";

    @Query("SELECT * FROM " + TABLE_NAME + " ORDER BY created_at desc")
    Observable<List<DramaPo>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)//預設萬一執行出錯怎麼辦，REPLACE為覆蓋
    void insert(DramaPo dramaPO);
}