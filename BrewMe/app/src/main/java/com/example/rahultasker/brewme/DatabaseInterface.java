package com.example.rahultasker.brewme;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DatabaseInterface {

    @Query("SELECT * FROM BrewRecord")
    List<BrewRecord> getAllItems();

    @Insert
    void insertAll(BrewRecord... brewRecords);

    @Query("DELETE FROM BrewRecord where id == :id")
    public void deleteItems(String id);

    @Update
    public void updateItems(BrewRecord... eartquakeRecords);

    @Query("SELECT * FROM BrewRecord")
    public List<BrewRecord> getAll();

    @Query("DELETE FROM BrewRecord")
    public void dropTheTable();

    @RawQuery
    int vacuumDb(SupportSQLiteQuery supportSQLiteQuery);
}