package com.example.rahultasker.brewme;


import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;

// bump version number if your schema changes
@Database(entities = {SavedBrewRecord.class},version = 5)
public abstract class SavedDatabase extends RoomDatabase {

    public abstract SavedDatabaseInterface savedDatabaseInterface();

    public static final String NAME = "MySavedBreweries";

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}