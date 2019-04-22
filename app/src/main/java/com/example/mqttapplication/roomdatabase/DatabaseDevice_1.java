package com.example.mqttapplication.roomdatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {EntityDevice_1.class}, version = 1)
public abstract class DatabaseDevice_1 extends RoomDatabase {

    public abstract DaoDevice_1 daoDevice1();
    private static DatabaseDevice_1 INSTANCE;

    public static DatabaseDevice_1 getInstance(final Context context){

        if(INSTANCE == null){
            synchronized (DatabaseDevice_1.class){
                if(INSTANCE == null){
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    DatabaseDevice_1.class, "device_db").build();
                }
            }
        }
        return INSTANCE;
    }
}
