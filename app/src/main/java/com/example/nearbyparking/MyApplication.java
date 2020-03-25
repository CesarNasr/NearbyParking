package com.example.nearbyparking;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import database.ParkingsDatabase;

public class MyApplication extends Application {
    private static ParkingsDatabase database;
    public static final String TAG = MyApplication.class.getSimpleName();
    private static Context context;
    private static MyApplication mInstance;
    public static final String DATABASE_NAME = "parkings_database";

    public MyApplication() {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        mInstance = this;
        InstantiateDatabase();
    }


    private void InstantiateDatabase() {
        database = Room.databaseBuilder(getApplicationContext(), ParkingsDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
    }

    public static ParkingsDatabase getDatabase() {
        return MyApplication.database;
    }
}
