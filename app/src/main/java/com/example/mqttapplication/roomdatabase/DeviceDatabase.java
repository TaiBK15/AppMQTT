package com.example.mqttapplication.roomdatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {DeviceEntity.class}, version = 1)
public abstract class DeviceDatabase extends RoomDatabase {

    public abstract DeviceDao deviceDao();
    private static DeviceDatabase INSTANCE;

    public static DeviceDatabase getInstance(final Context context){

        if(INSTANCE == null){
            synchronized (DeviceDatabase.class){
                if(INSTANCE == null){
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    DeviceDatabase.class, "device_db")
                                    .build();
                }
            }
        }
        return INSTANCE;
    }

//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new GenerateAsyncTask(INSTANCE).execute();
//        }
//    };
//
//    private static class GenerateAsyncTask extends AsyncTask<Void, Void, Void>{
//        private DeviceDao dao;
//
//        private GenerateAsyncTask(DeviceDatabase db){
//            dao = db.daoDevice1();
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            dao.insert(new DeviceEntity(25, 50, 60));
//            return null;
//        }
//    }
}
