package com.example.rahultasker.brewme;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SavedDatabaseInterface {

    @Query("SELECT * FROM SavedBrewRecord")
    List<SavedBrewRecord> getAllItems();

    @Insert
    void insertAll(SavedBrewRecord... savedBrewRecords);

    @Query("DELETE FROM SavedBrewRecord where id == :id")
    public void deleteItems(String id);

    @Update
    public void updateItems(SavedBrewRecord... savedBrewRecords);

    @Query("SELECT * FROM SavedBrewRecord")
    public List<BrewRecord> getAll();

    @Query("DELETE FROM SavedBrewRecord")
    public void dropTheTable();

    @RawQuery
    int vacuumDb(SupportSQLiteQuery supportSQLiteQuery);
}