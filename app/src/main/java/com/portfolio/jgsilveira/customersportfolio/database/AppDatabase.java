package com.portfolio.jgsilveira.customersportfolio.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.portfolio.jgsilveira.customersportfolio.R;

import static android.arch.persistence.room.Room.databaseBuilder;

public final class AppDatabase {

    private static AppDatabaseFactory DATABASE;

    private AppDatabase() {  }

    public static AppDatabaseFactory getInstance(Context context) {
        if (DATABASE == null) {
            String databaseName = context.getString(R.string.app_name);
            RoomDatabase.Builder<AppDatabaseFactory> databseBuilder =
                    databaseBuilder(context, AppDatabaseFactory.class, databaseName);
            DATABASE = databseBuilder.build();
        }
        return DATABASE;
    }

    static AppDatabaseFactory getInMemoryDatabase(Context context) {
        return Room.inMemoryDatabaseBuilder(context, AppDatabaseFactory.class)
                .allowMainThreadQueries().build();
    }

    public static void closeQuietly() {
        if (DATABASE != null) {
            DATABASE.close();
            DATABASE = null;
        }
    }

}
